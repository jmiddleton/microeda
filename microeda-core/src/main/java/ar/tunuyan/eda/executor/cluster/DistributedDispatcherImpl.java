package ar.tunuyan.eda.executor.cluster;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.RejectedExecutionException;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.tunuyan.eda.eventbus.Event;
import ar.tunuyan.eda.eventbus.NodeID;
import ar.tunuyan.eda.eventbus.Request;
import ar.tunuyan.eda.executor.Dispatcher;
import ar.tunuyan.eda.executor.DispatcherException;
import ar.tunuyan.eda.executor.MicroServiceWrapper;

import com.hazelcast.core.ExecutionCallback;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IExecutorService;
import com.hazelcast.core.Member;
import com.hazelcast.core.MemberAttributeEvent;
import com.hazelcast.core.MemberSelector;
import com.hazelcast.core.MembershipEvent;
import com.hazelcast.core.MembershipListener;
import com.hazelcast.core.MultiMap;

/**
 * Distributed dispatcher based on Hazelcast.
 * 
 * @author jmiddleton
 *
 */
public class DistributedDispatcherImpl implements MembershipListener,
		Dispatcher {

	private static final Logger logger = LoggerFactory
			.getLogger(DistributedDispatcherImpl.class);

	private static final int MAX_SUBMIT_TRIES = 2;

	@Inject
	private HazelcastInstance hazelcastInstance;

	private MultiMap<String, NodeID> serviceRegistry;

	public DistributedDispatcherImpl() {
	}

	public DistributedDispatcherImpl(HazelcastInstance hz) {
		this.hazelcastInstance = hz;
	}

	@PostConstruct
	public void init() {
		hazelcastInstance.getCluster().addMembershipListener(this);

		serviceRegistry = hazelcastInstance.getMultiMap("servicesRegistry");
	}

	private IExecutorService getExecutionService() {
		return hazelcastInstance.getExecutorService("default");
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T, V> void dispatch(Request<T, V> request, NodeID... nodes)
			throws DispatcherException {
		final MicroServiceWrapper callable = new MicroServiceWrapper(
				new Event<T>(request.getServiceName(), request.getData()));

		int tries = 0;
		while (++tries <= MAX_SUBMIT_TRIES) {
			logger.debug("Calling '{}' on cluster nodes '{}'. Event: {}",
					request.getServiceName(), nodes, request.getData());
			try {
				IExecutorService executorService = getExecutionService();
				if (request.getCallback() != null) {
					ExecutionCallback<V> executionCallback = createCallbackAdapter(
							request, nodes);
					executorService.submit((Callable<V>) callable,
							getMemberSelector(nodes), executionCallback);
				} else {
					executorService.submitToMembers(callable,
							getMemberSelector(nodes));
				}
				break;
			} catch (RejectedExecutionException ree) {
				logger.error(
						"Tried to distribute request, but I got a rejected execution exception",
						ree);
			} catch (RuntimeException e) {
				logger.error(
						"Tried to distribute request, but I got an exception",
						e);
			}
		}

		if (tries > MAX_SUBMIT_TRIES) {
			throw new DispatcherException(
					"Unable to submit work to nodes. I tried "
							+ MAX_SUBMIT_TRIES + " times.");
		}
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
	public <T, V> boolean cancelTask(UUID requestId, NodeID node)
			throws DispatcherException {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Member selector based on members host and port.
	 * 
	 * @param node
	 * @return
	 */
	private MemberSelector getMemberSelector(final NodeID... nodes) {
		return new MemberSelector() {

			@Override
			public boolean select(Member member) {
				InetSocketAddress socket = member.getSocketAddress();
				for (int i = 0; i < nodes.length; i++) {
					NodeID node = nodes[i];
					if (node.getHost().equals(socket.getHostName())
							&& node.getPort() == socket.getPort()) {
						return true;
					}
				}
				return false;
			}
		};
	}

	private <T, V> ExecutionCallback<V> createCallbackAdapter(
			Request<T, V> request, NodeID... nodes) {
		if (request.getCallback() == null) {
			return null;
		}
		return new ExecutionCallbackAdapter<T, V>(request, nodes);
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
		return this.serviceRegistry.entrySet();
	}

	@Override
	public boolean removeNode(String key, NodeID nodeId) {
		logger.debug("Removing node '{}' for service '{}' ...", nodeId, key);
		return this.serviceRegistry.remove(key, nodeId);
	}

	@Override
	public void memberAdded(MembershipEvent membershipEvent) {
		// not needed
	}

	@Override
	public void memberRemoved(MembershipEvent membershipEvent) {
		String host = membershipEvent.getMember().getSocketAddress()
				.getHostName();
		int port = membershipEvent.getMember().getSocketAddress().getPort();

		NodeID nodeId = new NodeID(host, port);

		Set<Entry<String, NodeID>> entries = getNodesEntrySet();
		List<String> lstRemove = new ArrayList<String>();
		for (Entry<String, NodeID> entry : entries) {
			if (entry.getValue().equals(nodeId)) {
				lstRemove.add(entry.getKey());
			}
		}

		// removing node from the service registry
		for (String key : lstRemove) {
			boolean result = removeNode(key, nodeId);
			logger.debug("Removing service '{}' from node '{}'. Result: {}",
					key, nodeId, result);
		}

	}

	@Override
	public void memberAttributeChanged(MemberAttributeEvent memberAttributeEvent) {
		// not needed

	}

	@Override
	public NodeID getOrCreateNodeID() {
		try {
			Member localMember = hazelcastInstance.getCluster()
					.getLocalMember();
			return new NodeID(localMember.getSocketAddress().getHostName(),
					localMember.getSocketAddress().getPort());
		} catch (UnsupportedOperationException e) {
			return new NodeID("localhost", -1);
		}
	}

	@Override
	public boolean isDistributed() {
		return true;
	}
}
