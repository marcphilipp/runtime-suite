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
	private final ClassLoader classLoader;

	public Classpath(String classpath, ClassLoader classLoader) {
		this.classpath = classpath;
		this.classLoader = classLoader;
	}

	public boolean isJarOrZipFile() {
		File file = classpathFile();
		return hasExtension(file, ".jar") || hasExtension(file, ".zip");
	}

	public boolean isDirectory() {
		return classpathFile().isDirectory();
	}

	public Collection<Class<?>> classes(ClassFilter filter) {
		log.debug(format("Gathering classes from %s", classpath));
		return classes(fileNames(), filter);
	}

	private Iterable<String> fileNames() {
		if (isDirectory())
			return new FilenamesInDirectory(classpathFile());
		if (isJarOrZipFile())
			return new FilenamesInJar(classpathFile());
		return emptyList();
	}
	
	private Collection<Class<?>> classes(Iterable<String> fileNames, ClassFilter filter) {
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
		return Class.forName(classNameForFile(fileName), true, classLoader);
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

	private boolean hasExtension(File file, String extension) {
		return file.getName().endsWith(extension) || file.getName().endsWith(extension.toUpperCase());
	}

	private File classpathFile() {
		return new File(classpath);
	}
}
