package ar.tunuyan.eda.eventbus;

/**
 * EventCallback allows to asynchronously get notified when the execution is
 * completed, either successfully or with error.
 *
 * @author jmiddleton
 *
 * @param <V>
 */
public interface EventCallback<V> {

	/**
	 * Called when an execution is completed successfully.
	 *
	 * @param response
	 *            result of execution
	 */
	void onResponse(V response);

	/**
	 * Called when an execution is completed with an error.
	 * 
	 * @param re
	 *            exception thrown
	 */
	void onFailure(ReplyException re);
}
