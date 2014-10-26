package ar.tunuyan.eda.eventbus;

import java.io.Serializable;

/**
 * Defines an Hazelcast Node ID.
 * 
 * @author jmiddleton
 *
 */
public class NodeID implements Serializable {

	private static final long serialVersionUID = 3705854545979604410L;

	private String host;
	private int port;

	public NodeID(String host, int port) {
		this.port = port;
		this.host = host;
	}

	public NodeID() {
	}

	public boolean isClient() {
		return this.port == -1;
	}

	public String toString() {
		return this.host + ":" + this.port;
	}

	public int getPort() {
		return this.port;
	}

	public String getHost() {
		return this.host;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((host == null) ? 0 : host.hashCode());
		result = prime * result + port;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NodeID other = (NodeID) obj;
		if (host == null) {
			if (other.host != null)
				return false;
		} else if (!host.equals(other.host))
			return false;
		if (port != other.port)
			return false;
		return true;
	}
}
