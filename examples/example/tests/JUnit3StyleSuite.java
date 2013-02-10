package example.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

public class JUnit3StyleSuite {

	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTestSuite(JUnit3TestCase.class);
		return suite;
	}

}
