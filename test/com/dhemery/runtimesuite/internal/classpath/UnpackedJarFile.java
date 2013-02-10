package com.dhemery.runtimesuite.internal.classpath;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.junit.rules.TemporaryFolder;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class UnpackedJarFile implements TestRule {

	private final String resourceNameOnClasspath;
	private final TemporaryFolder temporaryFolder = new TemporaryFolder();
	
	public UnpackedJarFile(String resourceNameOnClasspath) {
		this.resourceNameOnClasspath = resourceNameOnClasspath;
	}
	
	@Override
	public Statement apply(final Statement base, final Description description) {
        return temporaryFolder.apply(new Statement() {
            @Override
            public void evaluate() throws Throwable {
            	unpackJarFile();
				base.evaluate();
            }
        }, description);
	}

	public File getDirectory() {
		return temporaryFolder.getRoot();
	}

	public URL getURL() throws MalformedURLException {
		return temporaryFolder.getRoot().toURI().toURL();
	}

	private void unpackJarFile() throws IOException {
		URL url = getClass().getResource(resourceNameOnClasspath);
		unpack(url, temporaryFolder.getRoot());
	}

	private void unpack(URL source, File target) throws IOException {
		JarFile jar = new JarFile(source.getPath());
		for (Enumeration<JarEntry> entries = jar.entries(); entries.hasMoreElements();) {
		    JarEntry entry = entries.nextElement();
		    File file = new File(target, entry.getName());
		    if (entry.isDirectory()) {
				file.mkdirs();
		    } else {
		    	writeEntryToFile(jar, entry, file);
		    }
		}
	}

	private void writeEntryToFile(JarFile jar, JarEntry entry, File file) throws IOException {
		InputStream in = null;
		FileOutputStream out = null;
		try {
			file.getParentFile().mkdirs();
			in = jar.getInputStream(entry);
			out = new FileOutputStream(file);
			while (in.available() > 0) {
		    	out.write(in.read());
		    }
		} finally {
			closeQuietly(out);
			closeQuietly(in);
		}
	}

	private void closeQuietly(Closeable stream) {
		if (stream == null) {
			return;
		}
		try {
			stream.close();
		} catch (Exception e) {
			// ignore
		}
	}
}
