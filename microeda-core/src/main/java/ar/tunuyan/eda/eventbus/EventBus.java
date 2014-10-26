package ar.tunuyan.eda.eventbus;

import ar.tunuyan.eda.eventbus.handler.EventHandler;
import ar.tunuyan.eda.executor.Dispatcher;
import ar.tunuyan.eda.executor.DispatcherException;

/**
 * It defines an event-driven and reactive bus. It provides abstractions to
 * facilitate publishing and consuming events.
 * <p>
 * The bus is backed by a {@link Dispatcher} implementation which handles the
 * execution of {@link EventHandler}.
 * <p>
 * The event bus implements publish / subscribe, point to point messaging and
 * request-response messaging.
 * <p>
 * Messages sent over the event bus are represented by instances of the
 * {@link io.vertx.core.eventbus.Message} class.
 * <p>
 * For publish / subscribe, messages can be published to an address using one of
 * the {@link #publish} methods. An address is a simple {@code String} instance.
 * <p>
 * Handlers are registered against an address. There can be multiple handlers
 * registered against each address, and a particular handler can be registered
 * against multiple addresses. The event bus will route a sent message to all
 * handlers which are registered against that address.
 * <p>
 * For point to point messaging, messages can be sent to an address using one of
 * the {@link #send} methods. The messages will be delivered to a single
 * handler, if one is registered on that address. If more than one handler is
 * registered on the same address, it will round-robin to deliver the message to
 * that.
 * <p>
 * All messages sent over the bus are transient. On event of failure of all or
 * part of the event bus messages may be lost. Applications should be coded to
 * cope with lost messages, e.g. by resending them, and making application
 * services idempotent.
 * <p>
 * 
 * @author jmiddleton
 *
 */
public interface EventBus {

	/**
	 * Sends a message to the event bus. This method implements the send and
	 * forget pattern.
	 * 
	 * @param serviceName
	 * @param message
	 * @throws DispatcherException
	 */
	<T> void send(String serviceName, T message) throws DispatcherException;

	/**
	 * Sends a message to the event bus. Caller will be notified for the result
	 * of the task by {@link EventCallback#onResponse(Object)} or
	 * {@link EventCallback#onFailure(Throwable)}.
	 *
	 * @param serviceName
	 * @param message
	 * @param callback
	 * @throws DispatcherException
	 */
	<T, V> void send(String serviceName, T message, EventCallback<V> callback) throws DispatcherException;

	/**
	 * Publish a message on the event bus.
	 * 
	 * @param serviceName
	 *            The serviceName to publish it to
	 * @param message
	 *            The message
	 * @throws DispatcherException
	 */
	<T, V> void publish(String serviceName, T message) throws DispatcherException;

	/**
	 * Registers a {@link EventHandler} with a specific service name.
	 * 
	 * @param serviceName
	 *            The serviceName to register it at
	 * @param EventHandler
	 *            The EventHandler
	 */
	<T> void registerMicroService(String serviceName, EventHandler<T> MicroService);

	// TODO: unregister

	/**
	 * Unregister a service from a particular node. This is used when there is a
	 * failure executing a task.
	 * 
	 * @param key
	 * @param nodeId
	 * @return
	 */
	boolean removeNode(String key, NodeID nodeId);

}
