package com.dhemery.runtimesuite.internal;

import org.junit.runner.Request;

import com.dhemery.runtimesuite.ClassFilter;

public class ClassesWithRunner implements ClassFilter {

	@Override
	public boolean passes(Class<?> candidateClass) {
		return Request.aClass(candidateClass).getRunner() != null;
	}

}
