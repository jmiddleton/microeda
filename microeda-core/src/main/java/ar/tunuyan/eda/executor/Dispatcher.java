package ar.tunuyan.eda.executor;

import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import ar.tunuyan.eda.eventbus.Event;
import ar.tunuyan.eda.eventbus.NodeID;
import ar.tunuyan.eda.eventbus.Request;
import ar.tunuyan.eda.eventbus.handler.EventHandler;

/**
 * Dispatches {@link Event} to one or more {@link EventHandler}. <br>
 * It handles registering and unregistering of {@link EventHandler}.<br>
 * In distributed environment, it will auto-register to listen for any change on
 * the cluster (add/remove member).
 * 
 * @author jmiddleton
 *
 */
public interface Dispatcher {

	<T, V> void dispatch(Request<T, V> request, NodeID... nodes) throws DispatcherException;

	<T, V> boolean addPendingTask(Request<T, V> request);

	<T, V> boolean removePending(Request<T, V> request);

	<T, V> boolean cancelTask(UUID requestId, NodeID node) throws DispatcherException;

	List<NodeID> getNodes(String serviceName);

	void putNode(String serviceName, NodeID nodeID);

	Set<Entry<String, NodeID>> getNodesEntrySet();

	boolean removeNode(String key, NodeID nodeId);

	NodeID getOrCreateNodeID();

}