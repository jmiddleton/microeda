package ar.tunuyan.eda.eventbus.handler;

import ar.tunuyan.eda.eventbus.Event;

/**
 * Defines a EventHandler. Executes the logic of the action, accepting the given
 * parameter.
 * 
 * @author jmiddleton
 */
// TODO: implement it using an annotation
public interface EventHandler<T> {

	/**
	 * Process a {@link Event}.
	 * 
	 * @param message
	 */
	void handle(Event<T> message);
}