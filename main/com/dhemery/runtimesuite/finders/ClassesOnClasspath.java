package com.dhemery.runtimesuite.finders;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.dhemery.runtimesuite.ClassFilter;
import com.dhemery.runtimesuite.ClassFinder;
import com.dhemery.runtimesuite.internal.ClassesWithRunner;
import com.dhemery.runtimesuite.internal.classpath.Classpath;

/**
 * <p>
 * A finder that finds every class in the specified directories on the classpath.
 * </p>
 * <p>
 * Note: The current implementation searches directories and (optionally) jar files.
 * </p>
 * @author Dale H. Emery
 */
public class ClassesOnClasspath implements ClassFinder {

	private final String classpathList;
	private final ClassLoader classLoader;

	private boolean includeJars;

	public ClassesOnClasspath() {
		this(System.getProperty("java.class.path"));
	}

	/**
	 * @param classpathList the list of directories to search for classes,
	 * separated by {@link File#pathSeparatorChar}.
	 * Each directory must be on the classpath.
	 */
	public ClassesOnClasspath(String classpathList) {
		this(classpathList, ClassesOnClasspath.class.getClassLoader());
	}

	public ClassesOnClasspath(String classpathList, ClassLoader classLoader) {
		this.classpathList = classpathList;
		this.classLoader = classLoader;
	}

	public ClassesOnClasspath includingJars() {
		includeJars = true;
		return this;
	}

	/**
	 * Finds all classes in the directories listed in this finder's {@code classpathList}.
	 * @return a {@link Collection} of all classes in the directories listed in this finder's {@code classpathList}.
	 */
	@Override
	public Collection<Class<?>> find() {
		ClassFilter withTestRunner = new ClassesWithRunner();
		Set<Class<?>> testClasses = new HashSet<Class<?>>();
		for(String path : classpathList.split(File.pathSeparator)) {
			Classpath classpath = new Classpath(path, classLoader);
			if (include(classpath))
				testClasses.addAll(classpath.classes(withTestRunner));
		}
		return testClasses;
	}

	private boolean include(Classpath classpath) {
		return classpath.isDirectory() || includeJars && (classpath.isJarOrZipFile() || classpath.isWildcard());
	}
}
