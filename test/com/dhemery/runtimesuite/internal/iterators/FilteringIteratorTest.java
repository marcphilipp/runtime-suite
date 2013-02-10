package com.dhemery.runtimesuite.internal.iterators;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

public class FilteringIteratorTest {

	@Test
	public void takeOnlyFromInsideOfInnerIterator() {
		FilteringIterator<Integer> filter = evenNumbers(asList(1, 2, 3, 4, 5));
		
		assertThat(filter.next(), is(2));
		assertThat(filter.next(), is(4));
		assertFalse(filter.hasNext());
	}

	@Test
	public void takeFromBothEndsOfIterator() {
		FilteringIterator<Integer> filter = unevenNumbers(asList(1, 2, 3, 4, 5));
		
		assertTrue(filter.hasNext());
		assertThat(filter.next(), is(1));
		assertTrue(filter.hasNext());
		assertThat(filter.next(), is(3));
		assertTrue(filter.hasNext());
		assertThat(filter.next(), is(5));
		assertFalse(filter.hasNext());
	}

	private FilteringIterator<Integer> evenNumbers(List<Integer> ints) {
		return new FilteringIterator<Integer>(ints.iterator()) {
			@Override
			protected boolean passes(Integer candidate) {
				return candidate.intValue() % 2 == 0;
			}
		};
	}

	private FilteringIterator<Integer> unevenNumbers(List<Integer> ints) {
		return new FilteringIterator<Integer>(ints.iterator()) {
			@Override
			protected boolean passes(Integer candidate) {
				return candidate.intValue() % 2 == 1;
			}
		};
	}

}
