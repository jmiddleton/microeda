package ar.tunuyan.eda.executor.cluster;

import ar.tunuyan.eda.eventbus.EventCallback;
import ar.tunuyan.eda.eventbus.NodeID;
import ar.tunuyan.eda.eventbus.ReplyException;
import ar.tunuyan.eda.eventbus.Request;

import com.hazelcast.core.ExecutionCallback;
import com.hazelcast.spi.exception.RetryableHazelcastException;

/**
 * Execution callback to handle the service response or failure. <br>
 * In case of a failure, this callback will try to re-send/publish the message.
 * 
 * @author jmiddleton
 *
 * @param <T>
 * @param <V>
 */
public class ExecutionCallbackAdapter<T, V> implements ExecutionCallback<V> {

	private Request<T, V> request;
	private EventCallback<V> callback;
	private NodeID[] nodes;

	public ExecutionCallbackAdapter(Request<T, V> request, NodeID... nodes) {
		this.request = request;
		this.nodes = nodes;
		this.callback = request.getCallback();
	}

	public void onResponse(V response) {
		callback.onResponse(response);
	}

	public void onFailure(Throwable t) {
		if (t.getCause() != null && t.getCause() instanceof RetryableHazelcastException) {
			processDisconnectionFailure((RetryableHazelcastException) t.getCause());
		} else {
			callback.onFailure(new ReplyException(t));
		}
	}

	private void processDisconnectionFailure(RetryableHazelcastException tde) {
		// TODO: another option could be to add the request to a pending queue.
		for (NodeID nodeID : nodes) {
			request.getEventBus().removeNode(request.getServiceName(), nodeID);
		}

		try {
			if (request.isSend()) {
				request.getEventBus().send(request.getServiceName(), request.getData(), request.getCallback());
			} else {
				request.getEventBus().publish(request.getServiceName(), request.getData());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
