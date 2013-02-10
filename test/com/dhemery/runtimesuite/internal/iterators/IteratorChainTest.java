package com.dhemery.runtimesuite.internal.iterators;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Collections;

import org.junit.Test;

public class IteratorChainTest {

	@Test
	public void iteratesOverAllElementsInAllDelegateIterators() {
		IteratorChainBuilder<String> builder = IteratorChainBuilder.create();
		builder.append(asList("a", "b"));
		builder.append(Collections.<String>emptyList());
		builder.append(singletonList("c"));
		
		IteratorChain<String> chain = builder.toChain();
		
		assertTrue(chain.hasNext());
		assertThat(chain.next(), is("a"));
		assertTrue(chain.hasNext());
		assertThat(chain.next(), is("b"));
		assertTrue(chain.hasNext());
		assertThat(chain.next(), is("c"));
		assertFalse(chain.hasNext());
	}

}
