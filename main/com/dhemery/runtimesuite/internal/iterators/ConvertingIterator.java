package com.dhemery.runtimesuite.internal.iterators;

import java.util.Iterator;

public abstract class ConvertingIterator<In, Out> implements Iterator<Out> {
	
	private final Iterator<In> input;

	public ConvertingIterator(Iterator<In> input) {
		this.input = input;
	}

	@Override
	public final boolean hasNext() {
		return input.hasNext();
	}

	@Override
	public final Out next() {
		return convert(input.next());
	}

	@Override
	public final void remove() {
		input.remove();
	}

	protected abstract Out convert(In value);

}
