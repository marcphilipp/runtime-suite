package com.dhemery.runtimesuite.internal.iterators;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class ConvertingIteratorTest {

	@Test
	public void convertsElementsFromInnerIterator() {
		List<String> strings = asList("a", "bb", "ccc");
		ConvertingIterator<String, Integer> converter = stringLengthConverter(strings.iterator());
		
		assertThat(converter.next(), is(1));
		assertThat(converter.next(), is(2));
		assertThat(converter.next(), is(3));
		assertFalse(converter.hasNext());
	}

	@Test
	public void supportsRemovingOfElementsOnInnerIterator() {
		List<String> strings = new LinkedList<String>(asList("a", "bb", "ccc"));
		ConvertingIterator<String, Integer> converter = stringLengthConverter(strings.iterator());
		
		converter.next();
		converter.remove();
		
		assertThat(strings, is(asList("bb", "ccc")));
	}

	private ConvertingIterator<String, Integer> stringLengthConverter(Iterator<String> iterator) {
		return new ConvertingIterator<String, Integer>(iterator) {
			@Override
			protected Integer convert(String value) {
				return value.length();
			}
		};
	}

}
