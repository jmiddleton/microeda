package ar.tunuyan.eda.executor.local;

import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.tunuyan.eda.eventbus.Event;
import ar.tunuyan.eda.eventbus.EventCallback;
import ar.tunuyan.eda.eventbus.NodeID;
import ar.tunuyan.eda.eventbus.ReplyException;
import ar.tunuyan.eda.eventbus.Request;
import ar.tunuyan.eda.executor.Dispatcher;
import ar.tunuyan.eda.executor.DispatcherException;
import ar.tunuyan.eda.executor.MicroServiceWrapper;
import ar.tunuyan.eda.util.MultiMap;
import ar.tunuyan.eda.util.MultiMapValues;

/**
 * Local implementation of a Dispatches.
 * 
 * @author jmiddleton
 *
 */
public class LocalDispatcherImpl implements Dispatcher {

	private static final Logger logger = LoggerFactory.getLogger(LocalDispatcherImpl.class);

	private static final int MAX_SUBMIT_TRIES = 2;

	private MultiMap<String, NodeID> serviceRegistry = new MultiMapValues<String, NodeID>();

	private ExecutorService executorService;

	public LocalDispatcherImpl() {
	}

	@PostConstruct
	public void init() {
	}

	private ExecutorService getExecutionService() {
		return this.executorService;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T, V> void dispatch(Request<T, V> request, NodeID... nodes) throws DispatcherException {
		final MicroServiceWrapper callable = new MicroServiceWrapper(new Event<T>(request.getServiceName(), request.getData()));

		int tries = 0;
		while (++tries <= MAX_SUBMIT_TRIES) {
			logger.debug("Calling '{}' on cluster nodes '{}'. Event: {}", request.getServiceName(), nodes, request.getData());
			try {
				ExecutorService executorService = getExecutionService();

				CompletableFuture<V> future = (CompletableFuture<V>) CompletableFuture.supplyAsync(createSupplier(callable), executorService);

				EventCallback<V> callback = request.getCallback();
				if (callback != null) {
					// execute the callback with the result in the future.
					future.thenAcceptAsync(createConsumer(callback)).exceptionally(doHandleException(callback));
				}
				break;
			} catch (RuntimeException e) {
				logger.error("Tried to submit request, but I got an exception", e);
			}
		}

		if (tries > MAX_SUBMIT_TRIES) {
			throw new DispatcherException("Unable to submit work locally. I tried " + MAX_SUBMIT_TRIES + " times.");
		}
	}

	private <V> Function<Throwable, ? extends Void> doHandleException(EventCallback<V> callback) {
		return new Function<Throwable, Void>() {

			@Override
			public Void apply(Throwable t) {
				callback.onFailure(new ReplyException(t.getMessage(), t));
				return null;
			}
		};
	}

	private <V> Consumer<? super V> createConsumer(EventCallback<V> callback) {
		return new Consumer<V>() {

			@Override
			public void accept(V response) {
				callback.onResponse(response);
			}
		};
	}

	private Supplier<?> createSupplier(MicroServiceWrapper callable) {
		return new Supplier<Object>() {

			@Override
			public Object get() {
				return callable.call();
			}
		};
	}

	@Override
	public <T, V> boolean addPendingTask(Request<T, V> request) {
		System.out.println("----------> Request added: " + request.getData());
		return false;
	}

	@Override
	public <T, V> boolean removePending(Request<T, V> request) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <T, V> boolean cancelTask(UUID requestId, NodeID node) throws DispatcherException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<NodeID> getNodes(String serviceName) {
		return (List<NodeID>) this.serviceRegistry.get(serviceName);
	}

	@Override
	public void putNode(String serviceName, NodeID nodeID) {
		this.serviceRegistry.put(serviceName, nodeID);
	}

	@Override
	public Set<Entry<String, NodeID>> getNodesEntrySet() {
		return null; // TODO: this.serviceRegistry.entrySet();
	}

	@Override
	public boolean removeNode(String key, NodeID nodeId) {
		logger.debug("Removing node '{}' for service '{}' ...", nodeId, key);
		return this.serviceRegistry.remove(key, nodeId);
	}

	@Override
	public NodeID getOrCreateNodeID() {
		return new NodeID("localhost", -1);
	}

	public void setExecutorService(ExecutorService executorService) {
		this.executorService = executorService;
	}
}
