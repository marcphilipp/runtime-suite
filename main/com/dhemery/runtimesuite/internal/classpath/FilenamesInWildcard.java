package com.dhemery.runtimesuite.internal.classpath;

import java.io.File;
import java.io.FileFilter;
import java.util.Iterator;

import com.dhemery.runtimesuite.internal.iterators.IteratorChainBuilder;

public class FilenamesInWildcard implements Iterable<String> {

	private final File jarFolder;

	public FilenamesInWildcard(String classpath) {
		jarFolder = new File(classpath.substring(0, classpath.length() - 1));
	}

	@Override
	public Iterator<String> iterator() {
		IteratorChainBuilder<String> iterators = IteratorChainBuilder.create();
		for (File file : jarFolder.listFiles(onlyJars())) {
			iterators.append(new FilenamesInJar(file));
		}
		return iterators.toChain();
	}

	private FileFilter onlyJars() {
		final FileInspector fileInspector = new FileInspector();
		return new FileFilter() {
			@Override
			public boolean accept(File file) {
				return fileInspector.isJarFile(file);
			}
		};
	}
}
