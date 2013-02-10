package com.dhemery.runtimesuite.tests;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;

import org.junit.ClassRule;
import org.junit.Test;

import com.dhemery.runtimesuite.finders.ClassesOnClasspath;
import com.dhemery.runtimesuite.internal.classpath.UnpackedJarFile;

@SuppressWarnings("unchecked")
public class AClassesOnClasspathFinder {

	@ClassRule
	public static UnpackedJarFile firstJar = new UnpackedJarFile("/a.jar");

	@ClassRule
	public static UnpackedJarFile secondJar = new UnpackedJarFile("/b.jar");

	@ClassRule
	public static UnpackedJarFile fourthJar = new UnpackedJarFile("/d.jar");

	private ClassLoader classLoader;
	

	@Test public void findsAllClassesOnASingleElementClasspath() throws Exception {
		String classpath = firstJar.getDirectory().getAbsolutePath();
		classLoader = new URLClassLoader(new URL[] { firstJar.getURL() });
		Collection<Class<?>> foundClasses = new ClassesOnClasspath(classpath, classLoader).find();
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
	
	@Test public void findsClassesInJarWhenActivated() throws Exception {
		URL jarUrl = getClass().getResource("/a.jar");
		String classpath = jarUrl.getPath();
		classLoader = new URLClassLoader(new URL[] { jarUrl });
		Collection<Class<?>> foundClasses = new ClassesOnClasspath(classpath, classLoader).includingJars().find();
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
	
	@Test public void ignoresClassesInJarsByDefault() throws Exception {
		URL jarUrl = getClass().getResource("/a.jar");
		classLoader = new URLClassLoader(new URL[] { jarUrl });
		Collection<Class<?>> foundClasses = new ClassesOnClasspath(jarUrl.getPath(), classLoader).find();
		assertTrue(foundClasses.isEmpty());
	}

	@Test public void findsAllClassesOnAMultipleElementClasspath() throws Exception {
		String classpath = firstJar.getDirectory().getAbsolutePath()
						+ File.pathSeparator
						+ secondJar.getDirectory().getAbsolutePath();
		classLoader = new URLClassLoader(new URL[] { firstJar.getURL(), secondJar.getURL() });
		Collection<Class<?>> foundClasses = new ClassesOnClasspath(classpath, classLoader).find();
		assertThat(foundClasses, hasItems(	classForName("a.Test_a_1"),
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
	
	@Test public void ignoresNonTestClasses() throws Exception {
		// d.jar contains
		//    - ./d/Test_d_1.class
		//    - ./d/NotATest_d_2.class
		String classpath = fourthJar.getDirectory().getAbsolutePath();
		classLoader = new URLClassLoader(new URL[] { fourthJar.getURL() });
		Collection<Class<?>> foundClasses = new ClassesOnClasspath(classpath, classLoader).find();
		assertThat(foundClasses, hasItem(classForName("d.Test_d_1")));
	}
	
	@Test public void ignoresNonDirectoryClasspathElements() {
		Collection<Class<?>> foundClasses = new ClassesOnClasspath("no.such.directory").find();
		assertThat(foundClasses.size(), is(0));
	}

	private Class<?> classForName(String className) throws ClassNotFoundException {
		return Class.forName(className, true, classLoader);
	}
}
