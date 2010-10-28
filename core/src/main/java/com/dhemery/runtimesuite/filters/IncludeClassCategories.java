package com.dhemery.runtimesuite.filters;

import com.dhemery.runtimesuite.internal.ClassCategoryFilter;

/**
 * A filter that accepts each class if it is in an allowed category.
 * See {@link Category} for details of how to place classes in categories.
 * @author Dale H. Emery
 */
public class IncludeClassCategories extends ClassCategoryFilter {
	/**
	 * @param allowedCategories the list of categories allowed by this filter.
	 */
	public IncludeClassCategories(Class<?>...allowedCategories) {
		super(allowedCategories);
	}

	/**
	 * Accepts the class if it is in an allowed category.
	 * @param candidateClass the class to evaluate.
	 * @return {@code true} if {@code candidateClass} is in an allowed category,
	 * otherwise {@code false}. 
	 */
	public boolean passes(Class<?> candidateClass) {
		return hasMatchingCategory(candidateClass);
	}
}
