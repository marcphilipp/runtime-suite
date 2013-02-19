package example.tests;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import example.categories.CategoryA;

@Category(CategoryA.class)
public class ClassInCategoryA {
	@Test public void method() {}
}