package example.suites;

import org.junit.runner.RunWith;

import com.dhemery.runtimesuite.ClassFinder;
import com.dhemery.runtimesuite.Filter;
import com.dhemery.runtimesuite.Finder;
import com.dhemery.runtimesuite.MethodFilter;
import com.dhemery.runtimesuite.RuntimeSuite;
import com.dhemery.runtimesuite.finders.ListedClasses;

import example.filters.MethodsThatEndWith;

@RunWith(RuntimeSuite.class)
public class SuiteWithJUnit3StyleSuiteAndFilter {
	@Finder public ClassFinder classFinder = new ListedClasses(SuiteWithJUnit3StyleSuite.class);
	@Filter public MethodFilter filter = new MethodsThatEndWith("doesNotExist");
}