package com.dhemery.runtimesuite.internal;

import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.junit.experimental.categories.Category;

public class CategoryMatcher<T extends AnnotatedElement> {
	private final Collection<Class<?>> matchingCategories;

	public CategoryMatcher(Class<?>...matchingCategories) {
		this.matchingCategories = Arrays.asList(matchingCategories);
	}

	private Collection<Class<?>> categoriesOn(T element) {
		if(!element.isAnnotationPresent(Category.class)) {
			return Collections.emptyList();
		}
		Class<?>[] categories = element.getAnnotation(Category.class).value();
		return Arrays.asList(categories);
	}

	public boolean hasMatchingCategory(T element) {
		return !Collections.disjoint(matchingCategories, categoriesOn(element));
	}
}
