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

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

/**
 * Multimap interface. Copy from Vert.x
 * 
 * @author jmiddleton
 *
 */
public interface MultiMap<K, V> extends Iterable<Map.Entry<K, V>> {

	/**
	 * Returns the value of with the specified name. If there are more than one
	 * values for the specified name, the first value is returned.
	 *
	 * @param name
	 *            The name of the header to search
	 * @return The first header value or {@code null} if there is no such entry
	 */
	List<V> get(String name);

	/**
	 * Returns all entries it contains.
	 *
	 * @return A immutable {@link List} of the name-value entries, which will be
	 *         empty if no pairs are found
	 */
	Set<Entry<K, List<V>>> entries();

	/**
	 * Checks to see if there is a value with the specified name
	 *
	 * @param name
	 *            The name to search for
	 * @return True if at least one entry is found
	 */
	boolean contains(K name);

	/**
	 * Return true if empty
	 */
	boolean isEmpty();

	/**
	 * Gets a immutable {@link Set} of all names
	 *
	 * @return A {@link Set} of all names
	 */
	Set<K> names();

	/**
	 * Adds a new value with the specified name and value.
	 *
	 *
	 * @param name
	 *            The name
	 * @param value
	 *            The value being added
	 *
	 * @return {@code this}
	 */

	void add(K name, V value);

	/**
	 * Adds all the entries from another void to this one
	 *
	 * @return {@code this}
	 */

	void addAll(MultiMap<K, List<V>> map);

	/**
	 * Adds all the entries from a Map to this
	 *
	 * @return {@code this}
	 */

	void addAll(Map<K, V> map);

	/**
	 * Sets a value under the specified name.
	 *
	 * If there is an existing header with the same name, it is removed.
	 *
	 * @param name
	 *            The name
	 * @param value
	 *            The value
	 * @return {@code this}
	 */

	void set(K name, V value);

	/**
	 * Cleans this instance.
	 *
	 * @return {@code this}
	 */

	void setAll(MultiMap<K, List<V>> map);

	/**
	 * Cleans and set all values of the given instance
	 *
	 * @return {@code this}
	 */

	void setAll(Map<K, V> map);

	/**
	 * Removes the value with the given name
	 *
	 * @param name
	 *            The name of the value to remove
	 * @return {@code this}
	 */

	void remove(K name);

	boolean remove(K name, V value);

	/**
	 * Removes all
	 *
	 * @return {@code this}
	 */

	void clear();

	/**
	 * Return the number of names.
	 */
	int size();

	void put(K key, V value);

}
