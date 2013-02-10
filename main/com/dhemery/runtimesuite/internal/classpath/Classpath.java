package com.dhemery.runtimesuite.internal.classpath;

import static java.lang.String.format;
import static java.util.Collections.emptyList;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dhemery.runtimesuite.ClassFilter;

/**
 * Finds classes in a directory and its subdirectories.
 * This class is not intended for public use.
 * @author Dale H. Emery
 */
public class Classpath {
	private final Logger log = LoggerFactory.getLogger(Classpath.class);
	private final String classpath;

	public Classpath(String classpath) {
		this.classpath = classpath;
	}

	public Collection<Class<?>> classes(ClassFilter filter) {
		File directory = new File(classpath);
		if(directory.isDirectory()) {
			log.debug(format("Gathering classes from %s", classpath));
			return collectClasses(new FilenamesInDirectory(directory), filter);
		}
		return emptyList();
	}
	
	private Collection<Class<?>> collectClasses(Iterable<String> fileNames, ClassFilter filter) {
		Collection<Class<?>> classes = new ArrayList<Class<?>>();
		for(String fileName : fileNames) {
			if(isClassFile(fileName)) {
				try {
					Class<?> c = classForFile(fileName);
					if(filter.passes(c))  {
						log.trace(format("Gathered class %s", c));
						classes.add(c);
					} else {
						log.trace(format("Rejected class %s", c));
					}
				} catch (ClassNotFoundException e) {
					log.warn(format("Unable to load class from file %s", fileName));
				}
			}
		}
		return classes;
	}

	private Class<?> classForFile(String fileName) throws ClassNotFoundException {
		return Class.forName(classNameForFile(fileName));
	}

	private String classNameForFile(String fileName) {
		return convertFilePathToPackageName(stripExtension(fileName));
	}

	private String convertFilePathToPackageName(String filePath) {
		return filePath.replace(File.separatorChar, '.');
	}

	private String stripExtension(String filePath) {
		int baseNameLength = filePath.length() - ".class".length();
		return filePath.substring(0, baseNameLength);
	}

	private boolean isClassFile(String fileName) {
		return fileName.endsWith(".class");
	}
}
