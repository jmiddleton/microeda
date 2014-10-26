/*
 * Copyright (c) 2011-2013 The original author or authors
 * ------------------------------------------------------
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 *
 *     The Eclipse Public License is available at
 *     http://www.eclipse.org/legal/epl-v10.html
 *
 *     The Apache License v2.0 is available at
 *     http://www.opensource.org/licenses/apache2.0.php
 *
 * You may elect to redistribute this code under either of these licenses.
 */

package ar.tunuyan.eda.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @author <a href="mailto:nmaurer@redhat.com">Norman Maurer</a>
 */
public class MultiMapValues<K, V> implements MultiMap<K, V> {
	private Map<K, List<V>> iMap = new HashMap<K, List<V>>();

	@Override
	public List<V> get(String name) {
		return iMap.get(name);
	}

	@Override
	public Set<Entry<K, List<V>>> entries() {
		return iMap.entrySet();
	}

	@Override
	public boolean contains(K name) {
		return iMap.containsKey(name);
	}

	@Override
	public boolean isEmpty() {
		return iMap.isEmpty();
	}

	@Override
	public Set<K> names() {
		return iMap.keySet();
	}

	@Override
	public void add(K name, V value) {
		if (!iMap.containsKey(name)) {
			iMap.put(name, new ArrayList<V>());
		}
		List<V> values = iMap.get(name);
		values.add(value);
	}

	public void put(K name, V value) {
		add(name, value);
	}

	@Override
	public void addAll(MultiMap<K, List<V>> map) {
		for (Entry<K, List<V>> entry : map) {
			if (!iMap.containsKey(entry.getKey())) {
				iMap.put(entry.getKey(), new ArrayList<V>());
			}
			List<V> values = iMap.get(entry.getKey());
			values.addAll(entry.getValue());
		}
	}

	@Override
	public void addAll(Map<K, V> map) {
		// TODO Auto-generated method stub

	}

	@Override
	public void set(K name, V value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setAll(MultiMap<K, List<V>> map) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setAll(Map<K, V> map) {
		// TODO Auto-generated method stub

	}

	@Override
	public void remove(K name) {
		iMap.remove(name);
	}

	@Override
	public void clear() {
		iMap.clear();
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return iMap.size();
	}

	@Override
	public boolean remove(K name, V value) {
		List<V> values = iMap.get(name);
		if (values != null) {
			return values.remove(value);
		}
		return false;
	}

	@Override
	public Iterator<Entry<K, V>> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

}
