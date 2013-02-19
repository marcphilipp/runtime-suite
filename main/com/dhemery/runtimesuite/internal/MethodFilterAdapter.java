package com.dhemery.runtimesuite.internal;

import java.lang.reflect.Method;

import org.junit.runner.Description;
import org.junit.runner.manipulation.Filter;

import com.dhemery.runtimesuite.MethodFilter;

public abstract class MethodFilterAdapter implements MethodFilter {

	@Override
	public final boolean passes(Method method) {
		return createFilter().shouldRun(toDescription(method));
	}

	protected abstract Filter createFilter();

	private Description toDescription(Method method) {
		return Description.createTestDescription(method.getDeclaringClass(),
				method.getName(), method.getAnnotations());
	}

}
