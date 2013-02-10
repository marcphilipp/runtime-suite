package com.dhemery.runtimesuite.internal.classpath;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;

import org.junit.BeforeClass;
import org.junit.Test;

import com.dhemery.runtimesuite.ClassFilter;

@SuppressWarnings("unchecked")
public class ClasspathFromJarTest {

	private static ClassLoader classLoader;
	
	@BeforeClass
	public static void prepareClassLoader() throws Exception {
		URL[] urls = { url("/a.jar"), url("/c.jar") };
		classLoader = new URLClassLoader(urls);
	}

	@Test public void appliesClassFilter() throws Exception {
		Classpath classpath = new Classpath(url("/a.jar").getPath(), classLoader);
		assertThat(classpath.classes(new NoneClassFilter()).size(), is(0));
		assertThat(classpath.classes(new AnyClassFilter()).size(), is(not(0)));
	}

	@Test public void findsAllClassesOnASingleElementClasspath() throws Exception {
		Classpath classpath = new Classpath(url("/a.jar").getPath(), classLoader);
		Collection<Class<?>> foundClasses = classpath.classes(new AnyClassFilter());
		assertThat(foundClasses,  hasItems( classForName("a.Test_a_1"),
											classForName("a.Test_a_2"),
											classForName("a.a.Test_aa_1"),
											classForName("a.a.Test_aa_2"),
											classForName("a.a.a.Test_aaa_1"),
											classForName("a.a.a.Test_aaa_2"),
											classForName("a.a.b.Test_aab_1"),
											classForName("a.a.b.Test_aab_2"),
											classForName("a.b.Test_ab_1"),
											classForName("a.b.Test_ab_2")));
	}
	
	@Test public void ignoresNonClassFiles() throws Exception {
		// c.jar contains
		//    - ./c/Test_c_1.class
		//    - ./c/not-a-test.txt
		Classpath classpath = new Classpath(url("/c.jar").getPath(), classLoader);
		Collection<Class<?>> foundClasses = classpath.classes(new AnyClassFilter());
		assertThat(foundClasses, hasItem(classForName("c.Test_c_1")));
	}

	private Class<?> classForName(String className) throws ClassNotFoundException {
		return Class.forName(className, true, classLoader);
	}

	private static URL url(String resourceOnClasspath) throws Exception {
		return ClasspathFromJarTest.class.getResource(resourceOnClasspath);
	}

	private final class AnyClassFilter implements ClassFilter {
		@Override
		public boolean passes(Class<?> candidateClass) {
			return true;
		}
	}

	private final class NoneClassFilter implements ClassFilter {
		@Override
		public boolean passes(Class<?> candidateClass) {
			return false;
		}
	}
}
