package com.dhemery.runtimesuite.internal.classpath;

import java.io.File;

class FileInspector {

	boolean isJarOrZipFile(File file) {
		return isJarFile(file) || isZipFile(file);
	}

	boolean isJarFile(File file) {
		return hasExtension(file, ".jar");
	}

	boolean isZipFile(File file) {
		return hasExtension(file, ".zip");
	}

	boolean hasExtension(File file, String extension) {
		return file.getName().endsWith(extension) || file.getName().endsWith(extension.toUpperCase());
	}

	boolean isClassFile(String fileName) {
		return fileName.endsWith(".class");
	}

	String stripExtension(String filePath, String extension) {
		int baseNameLength = filePath.length() - extension.length();
		return filePath.substring(0, baseNameLength);
	}

}
