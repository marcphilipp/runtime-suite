package example.tests;

import org.junit.experimental.categories.Category;

import example.categories.CategoryC;
import example.categories.CategoryD;

@Category({CategoryC.class, CategoryD.class})
public class ClassInCategoriesCandD {}