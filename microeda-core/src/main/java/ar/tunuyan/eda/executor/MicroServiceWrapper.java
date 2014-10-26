package ar.tunuyan.eda.executor;

import java.io.Serializable;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import ar.tunuyan.eda.eventbus.Event;
import ar.tunuyan.eda.eventbus.handler.EventHandler;

/**
 * Provide the mechanism to execute a {@link EventHandler} with a
 * {@link ExecutorService}. It uses Spring's {@link ApplicationContext} to
 * search the service to execute. The service must be of type
 * {@link EventHandler}.
 * 
 * @author jmiddleton
 *
 */
public class MicroServiceWrapper implements Callable<Object>, Serializable {
	private static final long serialVersionUID = 2811093604766938362L;

	private static final Logger logger = LoggerFactory.getLogger(MicroServiceWrapper.class);

	private Event<?> message;

	public MicroServiceWrapper(Event<?> message) {
		this.message = message;
	}

	public String getServiceName() {
		if (this.message == null) {
			return null;
		}
		return this.message.getServiceName();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object call() {
		final EventHandler service = SpringContextHelper.getBean(message.getServiceName(), EventHandler.class);
		final String name = service.getClass().getSimpleName();

		logger.trace("Executing '{}'. Event: {}", name, message.body());

		try {
			service.handle(message);
			logger.trace("'{}' successfully executed.", name);
			return message.getReply();

		} catch (Exception e) {
			logger.error("Error executing Microservice '{}'. Exception: '{}'", name, e.getMessage());
			throw e;
		}
	}
}
