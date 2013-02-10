package com.dhemery.runtimesuite.internal.classpath;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;

import org.junit.BeforeClass;
import org.junit.Test;


@SuppressWarnings("unchecked")
public class ClasspathFromWildcardTest {

	private static ClassLoader classLoader;
	
	@BeforeClass
	public static void prepareClassLoader() throws Exception {
		URL[] urls = { url("/a.jar"), url("/b.jar") };
		classLoader = new URLClassLoader(urls);
	}

	@Test public void findsAllClassesOnASingleElementClasspath() throws Exception {
		String jarFolder = new File(url("/a.jar").getPath()).getParent() + "/*";
		Classpath classpath = new Classpath(jarFolder, classLoader);
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
											classForName("a.b.Test_ab_2"),
											classForName("b.Test_b_1"),
											classForName("b.Test_b_2"),
											classForName("b.a.Test_ba_1"),
											classForName("b.a.Test_ba_2"),
											classForName("b.b.Test_bb_1"),
											classForName("b.b.Test_bb_2"),
											classForName("b.b.a.Test_bba_1"),
											classForName("b.b.a.Test_bba_2"),
											classForName("b.b.b.Test_bbb_1"),
											classForName("b.b.b.Test_bbb_2")));
	}

	private Class<?> classForName(String className) throws ClassNotFoundException {
		return Class.forName(className, true, classLoader);
	}

	private static URL url(String resourceOnClasspath) throws Exception {
		return ClasspathFromWildcardTest.class.getResource(resourceOnClasspath);
	}
}
