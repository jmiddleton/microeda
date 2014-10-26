package ar.tunuyan.eda.selector;

import java.util.List;

import ar.tunuyan.eda.eventbus.NodeID;

public interface NodeSelector {
	List<NodeID> choose(List<NodeID> nodes);

}
