package example.suites;

import org.junit.runner.RunWith;

import com.dhemery.runtimesuite.ClassFinder;
import com.dhemery.runtimesuite.Filter;
import com.dhemery.runtimesuite.Finder;
import com.dhemery.runtimesuite.RuntimeSuite;
import com.dhemery.runtimesuite.filters.ExcludeClasses;
import com.dhemery.runtimesuite.finders.ListedClasses;

import example.tests.Runnable1;
import example.tests.Runnable2;
import example.tests.Runnable3;



@RunWith(RuntimeSuite.class)
public class SuiteWithClassFilterDeclaredAsSubtype {
	@Finder public ClassFinder finder = new ListedClasses(Runnable1.class, Runnable2.class, Runnable3.class);
	@Filter public ExcludeClasses filter = new ExcludeClasses(Runnable2.class);
}