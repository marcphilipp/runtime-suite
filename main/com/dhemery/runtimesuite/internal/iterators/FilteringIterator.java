package com.dhemery.runtimesuite.internal.iterators;

import java.util.Iterator;
import java.util.NoSuchElementException;

public abstract class FilteringIterator<T> extends ReadonlyIterator<T> {
	
	private final Iterator<T> innerIterator;
	private T next;
	private boolean consumed = true;
	private boolean nextAvailable;

	public FilteringIterator(Iterator<T> innerIterator) {
		this.innerIterator = innerIterator;
	}

	@Override
	public boolean hasNext() {
		if (consumed) {
			findNext();
		}
		return nextAvailable;
	}

	@Override
	public T next() {
		if (consumed) {
			findNext();
		}
		return consume();
	}

	private T consume() {
		if (nextAvailable) {
			consumed = true;
			return next;
		}
		throw new NoSuchElementException();
	}

	private void findNext() {
		nextAvailable = false;
		while (innerIterator.hasNext()) {
			T candidate = innerIterator.next();
			if (passes(candidate)) {
				next = candidate;
				nextAvailable = true;
				consumed = false;
				break;
			}
		}
	}

	protected abstract boolean passes(T candidate);

}
