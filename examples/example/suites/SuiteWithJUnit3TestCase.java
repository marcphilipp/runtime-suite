package example.suites;

import org.junit.runner.RunWith;

import com.dhemery.runtimesuite.ClassFinder;
import com.dhemery.runtimesuite.Finder;
import com.dhemery.runtimesuite.RuntimeSuite;
import com.dhemery.runtimesuite.finders.ListedClasses;

import example.tests.JUnit3TestCase;

@RunWith(RuntimeSuite.class)
public class SuiteWithJUnit3TestCase {
	@Finder public ClassFinder classFinder = new ListedClasses(JUnit3TestCase.class);
}