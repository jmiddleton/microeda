package ar.tunuyan.eda.eventbus;

import java.util.UUID;

/**
 * This class wraps info of the callable request.
 * 
 * @author jmiddleton
 *
 * @param <T>
 *            Event body type
 * @param <V>
 *            Response type
 */
public class Request<T, V> {

	private String serviceName;
	private long created;
	private UUID id;
	private int retryCount;

	private T data;
	private EventCallback<V> callback;
	private boolean send;
	private EventBus eventBus;

	public Request(String serviceName, T data, EventCallback<V> callback, boolean send, EventBus eventBus) {
		this.created = System.currentTimeMillis();
		this.id = UUID.randomUUID();
		this.serviceName = serviceName;
		this.data = data;
		this.callback = callback;
		this.send = send;
		this.eventBus = eventBus;
	}

	public String getServiceName() {
		return serviceName;
	}

	public long getCreated() {
		return created;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public int getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}

	public EventCallback<V> getCallback() {
		return callback;
	}

	public T getData() {
		return data;
	}

	public boolean isSend() {
		return send;
	}

	public EventBus getEventBus() {
		return eventBus;
	}
}
