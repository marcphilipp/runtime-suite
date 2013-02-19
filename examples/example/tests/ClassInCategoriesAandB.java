package example.tests;

import org.junit.experimental.categories.Category;

import example.categories.CategoryA;
import example.categories.CategoryB;

@Category({CategoryA.class, CategoryB.class})
public class ClassInCategoriesAandB {}