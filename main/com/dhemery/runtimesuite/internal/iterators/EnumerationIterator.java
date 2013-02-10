package com.dhemery.runtimesuite.internal.iterators;

import java.util.Enumeration;

public class EnumerationIterator<T> extends ReadonlyIterator<T> {
	
	private final Enumeration<T> enumeration;

	public EnumerationIterator(Enumeration<T> enumeration) {
		this.enumeration = enumeration;
	}

	@Override
	public boolean hasNext() {
		return enumeration.hasMoreElements();
	}

	@Override
	public T next() {
		return enumeration.nextElement();
	}

	public static <T> EnumerationIterator<T> toIterator(Enumeration<T> entries) {
		return new EnumerationIterator<T>(entries);
	}

}
