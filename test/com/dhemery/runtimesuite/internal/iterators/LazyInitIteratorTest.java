package com.dhemery.runtimesuite.internal.iterators;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.junit.Test;

public class LazyInitIteratorTest {

	protected boolean created = false;

	private LazyInitIterator<String> iterator = new LazyInitIterator<String>() {
		@Override
		protected Iterator<String> createInnerIterator() {
			created = true;
			return asList("a").iterator();
		}
	};
	
	@Test
	public void doesNotCreateIteratorBeforeItNeedsIt() {
		assertFalse(created);
	}
	
	@Test
	public void checksInnerIteratorOnHasNext() throws Exception {
		assertTrue(iterator.hasNext());
		assertTrue(created);
		assertThat(iterator.next(), is("a"));
		assertFalse(iterator.hasNext());
	}
	
	@Test
	public void iteratesOverInnerIterator() throws Exception {
		assertThat(iterator.next(), is("a"));
		assertFalse(iterator.hasNext());
	}

}
