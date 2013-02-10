package example.suites;

import org.junit.runner.RunWith;

import com.dhemery.runtimesuite.ClassFinder;
import com.dhemery.runtimesuite.Finder;
import com.dhemery.runtimesuite.RuntimeSuite;
import com.dhemery.runtimesuite.finders.ListedClasses;

import example.tests.TheoryTest;

@RunWith(RuntimeSuite.class)
public class SuiteWithRunWithClassFromFinder {
	@Finder public ClassFinder classFinder = new ListedClasses(TheoryTest.class);
}