package com.dhemery.runtimesuite.internal.classpath;

import static java.util.Arrays.asList;

import java.io.File;
import java.io.FileFilter;
import java.util.Iterator;

import com.dhemery.runtimesuite.internal.iterators.ConvertingIterator;
import com.dhemery.runtimesuite.internal.iterators.IteratorChainBuilder;
import com.dhemery.runtimesuite.internal.iterators.LazyInitIterator;

public class FilenamesInDirectory implements Iterable<String> {

	private final File root;

	public FilenamesInDirectory(File root) {
		this.root = root;
	}

	@Override
	public Iterator<String> iterator() {
		return stripPrefix(new RecursiveFileIterator(root));
	}

	private Iterator<String> stripPrefix(Iterator<File> iterator) {
		final int prefixLength = root.getAbsolutePath().length() + 1;
		return new ConvertingIterator<File, String>(iterator) {
			@Override
			protected String convert(File value) {
				return value.getAbsolutePath().substring(prefixLength);
			}
		};
	}
	
	private static class RecursiveFileIterator extends LazyInitIterator<File> {
		
		private static final FileFilter ONLY_DIRECTORIES = new FileFilter() {
			@Override
			public boolean accept(File file) {
				return file.isDirectory();
			}
		};

		private static final FileFilter ONLY_FILES = new FileFilter() {
			@Override
			public boolean accept(File file) {
				return file.isFile();
			}
		};

		private final File root;

		public RecursiveFileIterator(File root) {
			this.root = root;
		}

		@Override
		protected Iterator<File> createInnerIterator() {
			IteratorChainBuilder<File> iterators = IteratorChainBuilder.create();
			iterators.append(asList(root.listFiles(ONLY_FILES)));
			for (File file : root.listFiles(ONLY_DIRECTORIES)) {
				iterators.append(new RecursiveFileIterator(file));
			}
			return iterators.toChain();
		}

	}

}