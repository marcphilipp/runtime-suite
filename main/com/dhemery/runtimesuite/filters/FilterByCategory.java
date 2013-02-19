package com.dhemery.runtimesuite.filters;

import static com.dhemery.runtimesuite.filters.FilterByCategory.Match.ANY;
import static org.junit.experimental.categories.Categories.CategoryFilter.categoryFilter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.runner.manipulation.Filter;

import com.dhemery.runtimesuite.MethodFilter;
import com.dhemery.runtimesuite.internal.MethodFilterAdapter;

public class FilterByCategory {

	public enum Match {
		ANY, ALL
	}

	public interface CategoriesExcluder {

		MethodFilter andExcludeNone();

		MethodFilter butExclude(Class<?>... exclusions);

		MethodFilter butExclude(Match exclusionType, Class<?>... exclusions);
		
	}

	public static CategoriesExcluder include(Class<?>... inclusions) {
		return include(ANY, inclusions);
	}

	public static CategoriesExcluder include(Match inclusionType, Class<?>... inclusions) {
		return new MethodCategoriesFilter(inclusionType, inclusions);
	}

	public static MethodFilter exclude(Class<?>... exclusions) {
		return exclude(ANY, exclusions);
	}

	public static MethodFilter exclude(Match exclusionType, Class<?>... exclusions) {
		return new MethodCategoriesFilter().butExclude(exclusionType, exclusions);
	}

	private static class MethodCategoriesFilter extends MethodFilterAdapter implements CategoriesExcluder {

		private final CategorySet inclusions;
		private CategorySet exclusions = CategorySet.none();
		
		private MethodCategoriesFilter() {
			this(CategorySet.none());
		}

		private MethodCategoriesFilter(Match inclusionType, Class<?>[] inclusions) {
			this(new CategorySet(inclusionType, inclusions));
		}

		private MethodCategoriesFilter(CategorySet inclusions) {
			this.inclusions = inclusions;
		}

		@Override
		public MethodFilter andExcludeNone() {
			return butExclude(CategorySet.none());
		}

		@Override
		public MethodFilter butExclude(Class<?>... exclusions) {
			return butExclude(ANY, exclusions);
		}

		@Override
		public MethodFilter butExclude(Match exclusionType, Class<?>... exclusions) {
			return butExclude(new CategorySet(exclusionType, exclusions));
		}

		private MethodFilter butExclude(CategorySet categorySet) {
			this.exclusions = categorySet;
			return this;
		}

		@Override
		protected Filter createFilter() {
			return categoryFilter(inclusions.isAny(), inclusions.toSet(), exclusions.isAny(), exclusions.toSet());
		}
	}
	
	private static class CategorySet {
		
		private static final Class<?>[] NO_CATEGORIES = new Class<?>[0];

		private final Match matchType;
		private final Class<?>[] categories;
		
		static CategorySet none() {
			return new CategorySet(ANY, NO_CATEGORIES);
		}
		
		CategorySet(Match matchType, Class<?>... categories) {
			this.matchType = matchType;
			this.categories = categories;
		}

		boolean isAny() {
			return matchType == ANY;
		}

		Set<Class<?>> toSet() {
			HashSet<Class<?>> result = new HashSet<Class<?>>();
			Collections.addAll(result, categories);
			return result;
		}
	}

}
