package com.dhemery.runtimesuite.internal.classpath;

import com.dhemery.runtimesuite.ClassFilter;

class NoneClassFilter implements ClassFilter {
	@Override
	public boolean passes(Class<?> candidateClass) {
		return false;
	}
}