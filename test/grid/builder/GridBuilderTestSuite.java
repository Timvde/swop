package grid.builder;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@SuppressWarnings("javadoc")
@RunWith(Suite.class)
@SuiteClasses({ DeterministicDirectorTest.class, FileDirectorTest.class,
		RandomDirectorTest.class, TronGridBuilderTest.class})
public class GridBuilderTestSuite {
	
}
