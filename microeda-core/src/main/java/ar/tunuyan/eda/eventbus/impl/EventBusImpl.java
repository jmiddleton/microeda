package ar.tunuyan.eda.eventbus.impl;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import ar.tunuyan.eda.eventbus.EventBus;
import ar.tunuyan.eda.eventbus.EventCallback;
import ar.tunuyan.eda.eventbus.NodeID;
import ar.tunuyan.eda.eventbus.ReplyException;
import ar.tunuyan.eda.eventbus.Request;
import ar.tunuyan.eda.eventbus.handler.EventHandler;
import ar.tunuyan.eda.executor.Dispatcher;
import ar.tunuyan.eda.executor.DispatcherException;
import ar.tunuyan.eda.selector.NodeSelector;
import ar.tunuyan.eda.selector.impl.RoundRobinNodeSelector;

/**
 * EventBus implementation based on Reactor pattern.
 * 
 * @author jmiddleton
 *
 */
@Service
public class EventBusImpl implements EventBus, ApplicationContextAware {

	private static final Logger logger = LoggerFactory.getLogger(EventBusImpl.class);

	private ApplicationContext appContext;
	private NodeSelector nodeSelector = new RoundRobinNodeSelector();

	@Inject
	private Dispatcher dispatcher;

	private NodeID nodeID;
	private boolean clientMode = false;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostConstruct
	public void init() {
		if (this.dispatcher != null) {
			this.nodeID = this.dispatcher.getOrCreateNodeID();
			this.clientMode = this.nodeID.isClient();

			// search registered microservice in spring and register it in
			// hazelcast so everybody in the cluster know about it.
			if (!(this.clientMode && this.dispatcher.isDistributed())) {
				Map<String, EventHandler> localMicroServices = this.appContext.getBeansOfType(EventHandler.class);
				for (Entry<String, EventHandler> lmicro : localMicroServices.entrySet()) {
					registerMicroService(lmicro.getKey(), lmicro.getValue());
				}
			}
		}
	}

	public <T> void send(String serviceName, T message) throws DispatcherException {
		sendOrPublish(serviceName, message, null, true);
	}

	public <T, V> void send(String serviceName, T message, final EventCallback<V> callback) throws DispatcherException {
		sendOrPublish(serviceName, message, callback, true);
	}

	@Override
	public <T, V> void publish(String serviceName, T message) throws DispatcherException {
		sendOrPublish(serviceName, message, null, false);
	}

	private <V, T> void sendOrPublish(String serviceName, T data, final EventCallback<V> callback, boolean send)
			throws DispatcherException {
		Assert.notNull(serviceName, "Service cannot be null.");
		Assert.notNull(data, "Data cannot be null.");
		try {

			final Request<T, V> request = new Request<T, V>(serviceName, data, callback, send, this);
			List<NodeID> nodes = this.dispatcher.getNodes(serviceName);

			if (send) {
				// request-response: choose one node based on NodeSelector
				// strategy
				nodes = nodeSelector.choose(this.dispatcher.getNodes(serviceName));
				if (nodes.size() == 0) {
					String msg = "The service '" + serviceName + "' has not been deployed on the cluster yet!!!";
					if (callback != null) {
						callback.onFailure(new ReplyException("SERVICE_NOT_FOUND", msg));
					} else {
						throw new DispatcherException(msg);
					}
					return;
				}

				dispatcher.dispatch(request, nodes.get(0));
			} else {
				// publish-subscribe: publish on all the nodes where the service
				// is registered.
				dispatcher.dispatch(request, nodes.toArray(new NodeID[nodes.size()]));
			}
		} catch (RuntimeException re) {
			if (callback != null) {
				callback.onFailure(new ReplyException("UNEXPECTED_ERROR", re.getMessage(), re));
			} else {
				throw new DispatcherException(re.getMessage(), re);
			}
		}
	}

	@Override
	public <T> void registerMicroService(String serviceName, EventHandler<T> microservice) {
		// TODO: cuando la configuration es con hazelcast's ClientConfig, no
		// habria que registrar los servicios ya que otros nodos lo haran.
		// if (!isClientMode()) {
		Assert.notNull(serviceName, "Service name cannot be null.");
		Assert.notNull(microservice, "EventHandler cannot be null.");

		logger.debug("Registering service '{}' with name '{}' on cluster '{}'", microservice.getClass().getSimpleName(),
				serviceName, this.nodeID);
		this.dispatcher.putNode(serviceName, this.nodeID);

		// register the service on spring appContext
		if (!this.appContext.containsBean(serviceName)) {
			logger.debug("Registering bean '{}' on '{}' ...", microservice.getClass().getSimpleName(), serviceName);
			ConfigurableListableBeanFactory beanFactory = ((ConfigurableApplicationContext) this.appContext).getBeanFactory();
			beanFactory.registerSingleton(serviceName, microservice);
			logger.debug("Bean '{}' successfully registered.", serviceName);
		}
		// } else {
		// logger.debug("Client mode does not support service registration.");
		// }
	}

	public boolean removeNode(String key, NodeID nodeId) {
		return this.dispatcher.removeNode(key, nodeId);
	}

	@Override
	public void setApplicationContext(ApplicationContext appContext) throws BeansException {
		this.appContext = appContext;
	}

	public boolean isClientMode() {
		return clientMode;
	}

	public void setNodeSelector(NodeSelector nodeSelector) {
		this.nodeSelector = nodeSelector;
	}
}
