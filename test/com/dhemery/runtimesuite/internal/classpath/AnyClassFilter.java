package com.dhemery.runtimesuite.internal.classpath;

import com.dhemery.runtimesuite.ClassFilter;

class AnyClassFilter implements ClassFilter {
	@Override
	public boolean passes(Class<?> candidateClass) {
		return true;
	}
}