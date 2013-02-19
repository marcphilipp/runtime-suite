package example.suites;

import org.junit.runner.RunWith;

import com.dhemery.runtimesuite.ClassFilter;
import com.dhemery.runtimesuite.ClassFinder;
import com.dhemery.runtimesuite.Filter;
import com.dhemery.runtimesuite.Finder;
import com.dhemery.runtimesuite.RuntimeSuite;
import com.dhemery.runtimesuite.filters.IncludeClasses;
import com.dhemery.runtimesuite.finders.ListedClasses;

import example.tests.Runnable1;

@RunWith(RuntimeSuite.class)
public class SuiteWithJUnit3StyleSuiteAndClassFilter {
	@Finder public ClassFinder classFinder = new ListedClasses(SuiteWithJUnit3StyleSuite.class);
	@Filter public ClassFilter filter = new IncludeClasses(Runnable1.class);
}