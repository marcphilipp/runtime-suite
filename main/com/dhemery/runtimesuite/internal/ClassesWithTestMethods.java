package com.dhemery.runtimesuite.internal;

import java.lang.reflect.Method;

import org.junit.Test;

import com.dhemery.runtimesuite.ClassFilter;

/**
 * <p>
 * A filter that accepts each class that declares one or more test methods.
 * This filter is applied automatically by {@link com.dhemery.runtimesuite.RuntimeSuite}
 * and is not intended for public use.
 * </p>
 * @author Dale H. Emery
 */
public class ClassesWithTestMethods implements ClassFilter {

	@Override
	public boolean passes(Class<?> candidateClass) {
		for(Method method : candidateClass.getMethods()) {
			if(method.isAnnotationPresent(Test.class)) {
				return true;
			}
		}
		return false;
	}

}
