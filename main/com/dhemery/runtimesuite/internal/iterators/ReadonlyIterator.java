package com.dhemery.runtimesuite.internal.iterators;

import java.util.Iterator;

public abstract class ReadonlyIterator<T> implements Iterator<T> {

	@Override
	public final void remove() {
		throw new UnsupportedOperationException("Iterator is read-only");
	}

}