package com.dhemery.runtimesuite.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.dhemery.runtimesuite.MethodFilter;
import com.dhemery.runtimesuite.filters.FilterByCategory;
import com.dhemery.runtimesuite.filters.FilterByCategory.Match;

import example.categories.CategoryA;
import example.categories.CategoryB;
import example.tests.ClassInCategoriesAandB;
import example.tests.ClassInCategoryA;
import example.tests.ClassInCategoryB;
import example.tests.ClassWithCategorizedMethods;
import example.tests.ClassWithNoCategories;

public class AFilterByCategory {

	@Test
	public void forASingleCategory_passesEachMethodInThatCategory() throws Exception {
		MethodFilter filter = FilterByCategory.include((CategoryA.class)).andExcludeNone();
		assertTrue(filter.passes(ClassWithCategorizedMethods.class.getMethod("methodInCategoryA")));
		assertTrue(filter.passes(ClassWithCategorizedMethods.class.getMethod("methodInCategoriesAandB")));
	}

	@Test
	public void forASingleCategory_rejectsEachMethodNotInThatCategory() throws Exception {
		MethodFilter filter = FilterByCategory.include(CategoryA.class).andExcludeNone();
		assertFalse(filter.passes(ClassWithCategorizedMethods.class.getMethod("methodWithNoCategories")));
		assertFalse(filter.passes(ClassWithCategorizedMethods.class.getMethod("methodInCategoryB")));
		assertFalse(filter.passes(ClassWithCategorizedMethods.class.getMethod("methodInCategoriesCandD")));
	}
	
	@Test
	public void forMultipleCategories_passesEachMethodInAnySpecifiedCategory() throws Exception {
		MethodFilter filter = FilterByCategory.include(CategoryA.class, CategoryB.class).andExcludeNone();
		assertTrue(filter.passes(ClassWithCategorizedMethods.class.getMethod("methodInCategoryA")));
		assertTrue(filter.passes(ClassWithCategorizedMethods.class.getMethod("methodInCategoryB")));
		assertTrue(filter.passes(ClassWithCategorizedMethods.class.getMethod("methodInCategoriesAandB")));
	}
	
	@Test
	public void forMultipleCategories_rejectsEachMethodThatLacksEverySpecifiedCategory() throws Exception {
		MethodFilter filter = FilterByCategory.include(CategoryA.class, CategoryB.class).andExcludeNone();
		assertFalse(filter.passes(ClassWithNoCategories.class.getMethod("method")));
		assertFalse(filter.passes(ClassWithCategorizedMethods.class.getMethod("methodWithNoCategories")));
		assertFalse(filter.passes(ClassWithCategorizedMethods.class.getMethod("methodInCategoriesCandD")));
	}

	@Test
	public void forASingleCategory_rejectsEachMethodInThatCategory() throws Exception {
		MethodFilter filter = FilterByCategory.exclude(CategoryA.class);
		assertFalse(filter.passes(ClassWithCategorizedMethods.class.getMethod("methodInCategoryA")));
		assertFalse(filter.passes(ClassWithCategorizedMethods.class.getMethod("methodInCategoriesAandB")));
	}

	@Test
	public void forASingleCategory_passesEachMethodNotInThatCategory() throws Exception {
		MethodFilter filter = FilterByCategory.exclude(CategoryA.class);
		assertTrue(filter.passes(ClassWithNoCategories.class.getMethod("method")));
		assertTrue(filter.passes(ClassWithCategorizedMethods.class.getMethod("methodWithNoCategories")));
		assertTrue(filter.passes(ClassWithCategorizedMethods.class.getMethod("methodInCategoryB")));
		assertTrue(filter.passes(ClassWithCategorizedMethods.class.getMethod("methodInCategoriesCandD")));
	}
	
	@Test
	public void forMultipleCategories_rejectsEachMethodInAnySpecifiedCategory() throws Exception {
		MethodFilter filter = FilterByCategory.exclude(CategoryA.class, CategoryB.class);
		assertFalse(filter.passes(ClassWithCategorizedMethods.class.getMethod("methodInCategoryA")));
		assertFalse(filter.passes(ClassWithCategorizedMethods.class.getMethod("methodInCategoryB")));
		assertFalse(filter.passes(ClassWithCategorizedMethods.class.getMethod("methodInCategoriesAandB")));
	}

	@Test
	public void forMultipleCategories_passesEachMethodThatLacksEverySpecifiedCategory() throws Exception {
		MethodFilter filter = FilterByCategory.exclude(CategoryA.class, CategoryB.class);
		assertTrue(filter.passes(ClassWithCategorizedMethods.class.getMethod("methodWithNoCategories")));
		assertTrue(filter.passes(ClassWithCategorizedMethods.class.getMethod("methodInCategoriesCandD")));
	}

	@Test
	public void forCategoriesDeclaredOnClassLevel_passesMethodsThatLacksAllSpecifiedCategories() throws Exception {
		MethodFilter filter = FilterByCategory.exclude(Match.ALL, CategoryA.class, CategoryB.class);
		assertTrue(filter.passes(ClassInCategoryA.class.getMethod("method")));
		assertTrue(filter.passes(ClassInCategoryB.class.getMethod("method")));
		assertFalse(filter.passes(ClassInCategoriesAandB.class.getMethod("method")));
	}
	
	@Test
	public void forMultipleCategories_passesEachMethodInAnySpecifiedCategoryButExcludesThoseInExcludedCategories() throws Exception {
		MethodFilter filter = FilterByCategory.include(CategoryA.class).butExclude(CategoryB.class);
		assertTrue(filter.passes(ClassWithCategorizedMethods.class.getMethod("methodInCategoryA")));
		assertFalse(filter.passes(ClassWithCategorizedMethods.class.getMethod("methodInCategoryB")));
		assertFalse(filter.passes(ClassWithCategorizedMethods.class.getMethod("methodInCategoriesAandB")));
	}
	
	@Test
	public void forCategoriesDeclaredOnClassLevel_passesEachMethodInAnySpecifiedCategoryButExcludesThoseInExcludedCategories() throws Exception {
		MethodFilter filter = FilterByCategory.include(CategoryA.class).butExclude(CategoryB.class);
		assertTrue(filter.passes(ClassInCategoryA.class.getMethod("method")));
		assertFalse(filter.passes(ClassInCategoryB.class.getMethod("method")));
		assertFalse(filter.passes(ClassInCategoriesAandB.class.getMethod("method")));
	}
	
	@Test
	public void forCategoriesDeclaredOnClassLevel_passesEachMethodInAnySpecifiedCategory() throws Exception {
		MethodFilter filter = FilterByCategory.include(CategoryA.class, CategoryB.class).andExcludeNone();
		assertTrue(filter.passes(ClassInCategoryA.class.getMethod("method")));
		assertTrue(filter.passes(ClassInCategoryB.class.getMethod("method")));
		assertTrue(filter.passes(ClassInCategoriesAandB.class.getMethod("method")));
	}
	
	@Test
	public void forCategoriesDeclaredOnClassLevel_passesEachMethodInAllOfTheSpecifiedCategories() throws Exception {
		MethodFilter filter = FilterByCategory.include(Match.ALL, CategoryA.class, CategoryB.class).andExcludeNone();
		assertFalse(filter.passes(ClassInCategoryA.class.getMethod("method")));
		assertFalse(filter.passes(ClassInCategoryB.class.getMethod("method")));
		assertTrue(filter.passes(ClassInCategoriesAandB.class.getMethod("method")));
	}
}
