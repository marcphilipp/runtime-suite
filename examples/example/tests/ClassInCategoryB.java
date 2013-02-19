package example.tests;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import example.categories.CategoryB;

@Category(CategoryB.class)
public class ClassInCategoryB {
	@Test public void method() {}
}