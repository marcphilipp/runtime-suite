package com.dhemery.runtimesuite.tests;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import com.dhemery.runtimesuite.helpers.IsCollectionThat;
import com.dhemery.runtimesuite.helpers.SuiteRunListener;

import example.suites.SuiteThatFiltersOutAllClassesButOne;
import example.suites.SuiteThatFindsATestClassSeveralTimes;
import example.suites.SuiteWithAllBadTests;
import example.suites.SuiteWithClassFilterDeclaredAsSubtype;
import example.suites.SuiteWithClassFinderDeclaredAsSubtype;
import example.suites.SuiteWithGoodAndBadClassFilters;
import example.suites.SuiteWithGoodAndBadClassFinders;
import example.suites.SuiteWithJUnit3StyleSuite;
import example.suites.SuiteWithJUnit3StyleSuiteAndFilter;
import example.suites.SuiteWithJUnit3TestCase;
import example.suites.SuiteWithMethodFilters;
import example.suites.SuiteWithNoMethodFilters;
import example.suites.SuiteWithRunWithClassFromFinder;
import example.suites.SuiteWithTwoFinders;

public class ARuntimeSuite {
	private JUnitCore runner;
	private SuiteRunListener executed;

	@Before
	public void setUp() {
		executed = new SuiteRunListener();
		runner = new JUnitCore();
		runner.addListener(executed);
	}
	
	@Test
	public void runsTestsFromAllFinders() {
		runner.run(SuiteWithTwoFinders.class);
		assertThat(executed.tests, hasItems("runnable2", "runnable2")); 
	}

	@Test
	public void runsEachTestOnceEvenIfFoundMultipleTimes() {
		runner.run(SuiteThatFindsATestClassSeveralTimes.class);
		assertThat(executed.tests, not(hasDuplicates()));
	}
	
	@Test
	public void runsTestsOnlyFromCorrectlyDeclaredFinders() {
		runner.run(SuiteWithGoodAndBadClassFinders.class);
		assertThat(executed.tests, hasItems("runnable3")); 
	}

	@Test
	public void runsTestsFromFinderFieldsDeclaredAsDerivedTypes() {
		runner.run(SuiteWithClassFinderDeclaredAsSubtype.class);
		assertThat(executed.tests, hasItems("runnable1", "runnable2", "runnable3")); 
	}
 
	@Test
	public void appliesAllClassFilterFieldsAnnotatedWithFilter() {
		runner.run(SuiteThatFiltersOutAllClassesButOne.class);
		assertThat(executed.tests, hasItems("runnable2"));
	}

	@Test
	public void appliesOnlyCorrectlyDeclaredClassFilters() {
		runner.run(SuiteWithGoodAndBadClassFilters.class);
		assertThat(executed.tests.size(), is(2));
		assertThat(executed.tests, hasItems("runnable1", "runnable2"));
	}

	@Test
	public void appliesClassFilterFieldsDeclaredAsDerivedTypes() {
		runner.run(SuiteWithClassFilterDeclaredAsSubtype.class);
		assertThat(executed.tests, not(hasItem("runnable2")));
	}

	@Test
	public void runsAllMethodsIfNoFilters() {
		runner.run(SuiteWithNoMethodFilters.class);
		assertThat(executed.tests.size(), is(4));
		assertThat(executed.tests, hasItems("a_test1", "a_test2", "b_test1", "b_test2"));
	}

	@Test
	public void runsOnlyTestMethodsThatSurviveMethodFilters() {
		runner.run(SuiteWithMethodFilters.class);
		assertThat(executed.tests, hasItem("a_test1"));
		assertThat(executed.tests, not(hasItems("a_test2", "b_test1", "b_test2")));
	}

	@Test
	public void ignoresNonTestMethods() {
		Result result = runner.run(SuiteWithAllBadTests.class);
		assertThat(result.getFailureCount(), is(3));
	}

	@Test
	public void runsClassesAnnotatedUsingRunWith() throws Exception {
		runner.run(SuiteWithRunWithClassFromFinder.class);
		assertThat(executed.tests, hasItems("theoryTest"));
	}

	@Test
	public void runsJUnit3TestCases() throws Exception {
		runner.run(SuiteWithJUnit3TestCase.class);
		assertThat(executed.tests, hasItems("testHelloJUnit3"));
	}

	@Test
	public void runsJUnit3StyleSuite() throws Exception {
		runner.run(SuiteWithJUnit3StyleSuite.class);
		assertThat(executed.tests, hasItem("testHelloJUnit3"));
	}

	@Test
	public void appliesFilterToJUnit3StyleSuite() throws Exception {
		runner.run(SuiteWithJUnit3StyleSuiteAndFilter.class);
		assertThat(executed.tests, not(hasItem("testHelloJUnit3")));
	}

	private Matcher<List<String>> hasDuplicates() {
		return IsCollectionThat.<String>hasDuplicates();
	}
}
