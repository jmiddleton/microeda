package ar.tunuyan.eda.selector.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import ar.tunuyan.eda.eventbus.NodeID;
import ar.tunuyan.eda.selector.NodeSelector;

/**
 * Round robin selector.
 * 
 * @author jmiddleton
 *
 */
public class RoundRobinNodeSelector implements NodeSelector {
	private AtomicInteger pos = new AtomicInteger(0);

	public RoundRobinNodeSelector() {
	}

	@Override
	public List<NodeID> choose(List<NodeID> nodes) {
		final List<NodeID> tempNodes = new ArrayList<NodeID>();

		if (nodes != null) {
			while (true) {
				int size = nodes.size();
				if (size == 0) {
					break;
				}
				int p = pos.getAndIncrement();
				if (p >= size - 1) {
					pos.set(0);
				}
				try {
					tempNodes.add(nodes.get(p));
					break;
				} catch (IndexOutOfBoundsException e) {
					// Can happen
					pos.set(0);
				}
			}
		}
		return tempNodes;
	}

}
