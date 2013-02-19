package com.dhemery.runtimesuite;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.junit.runner.Description;
import org.junit.runner.Request;
import org.junit.runner.Runner;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;

import com.dhemery.runtimesuite.internal.RuntimeSuiteFilter;
import com.dhemery.runtimesuite.internal.SuiteInspector;

/**
 * <p>
 * Constructs a suite of JUnit tests at runtime, and executes the tests in the suite.
 * A "runtime suite" declares finders to find candidate test classes,
 * and filters to select test classes and methods.
 * The {@code RuntimeSuite} runner executes the finders and filters,
 * and executes the test methods that survive the filters.
 * (The &#64;{@link org.junit.Test} annotation describes what constitutes a test method.)
 * </p>
 * <p>
 * To declare a runtime suite, declare a class and annotate it with
 * {@code @}{@code RunWith}{@code (RuntimeSuite.class)}.
 * </p>
 * <pre>
 * &#64;RunWith(RuntimeSuite.class)
 * public class MySuite { ... }
 * </pre>
 * <p>
 * To tell {@code RuntimeSuite} which tests to run,
 * add one or more class finders to your suite class.
 * To add a class finder, declare and initialize a public field whose type implements {@link ClassFinder},
 * and annotate it with &#64;{@link Finder}.
 * </p>
 * <pre>
 * &#64;RunWith(RuntimeSuite.class)
 * public class MySuite {
 *     ...
 *     &#64;Finder ClassFinder myFinder = new MyFinderClass();
 *     ...
 * }
 * </pre>
 * <p>
 * By default, {@code RuntimeSuite} executes every test method in every class delivered by the finders.
 * To exclude classes and methods from execution,
 * add one or more class filters and method filters to the suite.
 * To add a class filter,
 * declare and initialize a public field of type {@link ClassFilter},
 * and annotate it with &#64;{@link Filter}.
 * To add a method filter,
 * declare and initialize a public field of type {@link MethodFilter},
 * and annotate it with &#64;{@code Filter}.
 * </p>
 * <pre>
 * &#64;RunWith(RuntimeSuite.class)
 * public class MySuite {
 *     ...
 *     &#64;Filter ClassFilter myClasses = new MyClassFilter();
 *     &#64;Filter ClassFilter myMethods = new MyMethodFilter();
 *     ...
 * }
 * </pre>
 * <p>
 * {@code RuntimeSuite} applies each class filter to select classes,
 * and each method filter to select test methods from the selected classes.
 * It then runs the selected test methods from the selected classes.
 * </p>
 * <p>
 * The order in which {@code RuntimeSuite} executes finders and filters
 * does not depend on the order in which they are declared. 
 * </p>
 * <p>
 * {@code RuntimeSuite} automatically filters out classes that have no test methods.
 * </p>
 * <p>
 * {@code RuntimeSuite} acts on each found class only once,
 * regardless of how many times it appears in the lists returned from the finders.
 * </p>
 * @author Dale H. Emery
 * @see org.junit.runners.RunWith
 */
public class RuntimeSuite extends ParentRunner<Runner> {

	private final SuiteInspector inspector;
	private final List<Runner> runners;

	/**
	 * Called reflectively by JUnit to initialize a suite before running its tests.
	 * @param suiteClass the suite class to execute.
	 * @throws InitializationError
	 */
	public RuntimeSuite(Class<?> suiteClass) throws InitializationError {
		super(suiteClass);
		inspector = new SuiteInspector(suiteClass);
		runners = runnersFor(classesInSuite());
	}

	private Set<Class<?>> classesInSuite() throws InitializationError {
		Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
		for(ClassFinder finder : inspector.classFinders()) {
			classes.addAll(finder.find());
		}
		return classes;
	}

	protected Description describeChild(Runner child) {
		return child.getDescription();
	}

	protected List<Runner> getChildren() {	
		return runners;
	}

	protected void runChild(Runner child, RunNotifier notifier) {
		child.run(notifier);
	}

	private List<Runner> runnersFor(Collection<Class<?>> classes) throws InitializationError {
		RuntimeSuiteFilter filter = new RuntimeSuiteFilter(inspector);
		List<Runner> runners = new ArrayList<Runner>();
		for(Class<?> c : classes) {
			try {
				Runner runner = Request.aClass(c).getRunner();
				filter.apply(runner);
				runners.add(runner);
			} catch (NoTestsRemainException e) {
				// ignore
			}
		}
		return runners;
	}
}
