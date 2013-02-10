package com.dhemery.runtimesuite.internal.iterators;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class IteratorChainBuilder<T> {

	public static <T> IteratorChainBuilder<T> create() {
		return new IteratorChainBuilder<T>();
	}

	private List<Iterator<T>> chainedIterators = new LinkedList<Iterator<T>>();
	
	public IteratorChainBuilder<T> append(Iterable<T> iterable) {
		return append(iterable.iterator());
	}
	
	public IteratorChainBuilder<T> append(Iterator<T> iterator) {
		chainedIterators.add(iterator);
		return this;
	}
	
	public IteratorChain<T> toChain() {
		return new IteratorChain<T>(chainedIterators);
	}
}
