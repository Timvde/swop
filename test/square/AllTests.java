package square;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


@SuppressWarnings("javadoc")
@RunWith(Suite.class)
@SuiteClasses({ ASquareTest.class, PowerFailureTest.class, SquareTest.class, WallPartTest.class,
		WallTest.class })
public class AllTests {
	
}
