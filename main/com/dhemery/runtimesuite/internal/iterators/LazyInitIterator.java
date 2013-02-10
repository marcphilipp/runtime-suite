package com.dhemery.runtimesuite.internal.iterators;

import java.util.Iterator;

public abstract class LazyInitIterator<T> extends ReadonlyIterator<T> {

	private Iterator<T> innerIterator;

	@Override
	public final boolean hasNext() {
		return getInnerIterator().hasNext();
	}

	@Override
	public final T next() {
		return getInnerIterator().next();
	}

	private Iterator<T> getInnerIterator() {
		if (innerIterator == null) {
			innerIterator = createInnerIterator();
		}
		return innerIterator;
	}

	protected abstract Iterator<T> createInnerIterator();
}
