package com.dhemery.runtimesuite.internal.iterators;

import java.util.Collections;
import java.util.Iterator;

public class IteratorChain<T> extends ReadonlyIterator<T> {

	private final Iterator<Iterator<T>> delegateIterator;
	private Iterator<T> currentIterator = Collections.<T>emptyList().iterator();
	
	public IteratorChain(Iterable<Iterator<T>> delegates) {
		this.delegateIterator = delegates.iterator();
	}

	@Override
	public boolean hasNext() {
		nextIterator();
		return currentIterator.hasNext();
	}

	@Override
	public T next() {
		nextIterator();
		return currentIterator.next();
	}

	private void nextIterator() {
		while (!currentIterator.hasNext() && delegateIterator.hasNext()) {
			currentIterator = delegateIterator.next();
		}
	}
	
}
