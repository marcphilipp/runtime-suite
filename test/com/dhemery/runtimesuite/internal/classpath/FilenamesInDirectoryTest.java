package com.dhemery.runtimesuite.internal.classpath;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

import java.util.Iterator;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class FilenamesInDirectoryTest {
	
	@Rule
	public TemporaryFolder root = new TemporaryFolder();

	@Test
	public void recursivelyIteratesOverFilenamesInRootFolder() throws Exception {
		root.newFile("a1.txt");
		root.newFolder("b");
		root.newFile("b/b1.txt");
		root.newFolder("b/b_c");
		root.newFile("b/b_c/bc.txt");
		root.newFile("a2.txt");
		
		Iterator<String> iterator = new FilenamesInDirectory(root.getRoot()).iterator();
		assertThat(iterator.next(), is("a1.txt"));
		assertThat(iterator.next(), is("a2.txt"));
		assertThat(iterator.next(), is("b/b1.txt"));
		assertThat(iterator.next(), is("b/b_c/bc.txt"));
		assertFalse(iterator.hasNext());
	}

}
