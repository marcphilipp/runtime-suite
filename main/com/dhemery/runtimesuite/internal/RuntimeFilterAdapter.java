package com.dhemery.runtimesuite.internal;

import java.lang.reflect.Method;
import java.util.List;

import org.junit.runner.Description;
import org.junit.runner.manipulation.Filter;
import org.junit.runners.model.InitializationError;

import com.dhemery.runtimesuite.ClassFilter;
import com.dhemery.runtimesuite.MethodFilter;

public class RuntimeFilterAdapter extends Filter {

	private List<ClassFilter> classFilters;
	private List<MethodFilter> methodFilters;

	public RuntimeFilterAdapter(SuiteInspector inspector) throws InitializationError {
		classFilters = inspector.classFilters();
		methodFilters = inspector.methodFilters();
	}

	@Override
	public String describe() {
		return "RuntimeFilter";
	}

	@Override
	public boolean shouldRun(Description description) {
		return (passesClassFilters(description) && passedMethodFilters(description)) || atLeastOneChildPasses(description);
	}

	private boolean passedMethodFilters(Description description) {
		if (description.getMethodName() != null) {
			Method[] methods = description.getTestClass().getMethods();
			for (Method method : methods) {
				if (method.getName().equals(description.getMethodName())) {
					return passesFilters(method);
				}
			}
		}
		return true;
	}

	private boolean atLeastOneChildPasses(Description description) {
		for (Description child : description.getChildren()) {
			if (shouldRun(child))
				return true;
		}
		return false;
	}

	private boolean passesClassFilters(Description description) {
		for(ClassFilter filter : classFilters) {
			if(!filter.passes(description.getTestClass()))
				return false;
		}
		return true;
	}

	private boolean passesFilters(Method method) {
		for(MethodFilter filter : methodFilters) {
			if(!filter.passes(method))
				return false;
		}
		return true;
	}
}
