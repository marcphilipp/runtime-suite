package com.dhemery.runtimesuite.internal.classpath;

import static com.dhemery.runtimesuite.internal.iterators.EnumerationIterator.toIterator;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.dhemery.runtimesuite.internal.iterators.ConvertingIterator;
import com.dhemery.runtimesuite.internal.iterators.FilteringIterator;

public class FilenamesInJar implements Iterable<String> {

	private final JarFile jarFile;

	public FilenamesInJar(File jarFile) {
		try {
			this.jarFile = new JarFile(jarFile);
		} catch (IOException e) {
			throw new IllegalArgumentException("Cannot read JAR file", e);
		}
	}

	@Override
	public Iterator<String> iterator() {
		return toName(onlyFiles(toIterator(jarFile.entries())));
	}

	private FilteringIterator<JarEntry> onlyFiles(Iterator<JarEntry> iterator) {
		return new FilteringIterator<JarEntry>(iterator) {
			@Override
			protected boolean passes(JarEntry jarEntry) {
				return !jarEntry.isDirectory();
			}
		};
	}

	private Iterator<String> toName(Iterator<JarEntry> iterator) {
		return new ConvertingIterator<JarEntry, String>(iterator) {
			@Override
			protected String convert(JarEntry jarEntry) {
				return jarEntry.getName();
			}
		};
	}

}
