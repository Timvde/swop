package unittests;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@SuppressWarnings("javadoc")
@RunWith(Suite.class)
@SuiteClasses({ CoordinateTest.class, GridTest.class, WallTest.class, SquareTest.class,
		InventoryTest.class, LightTrailTest.class })
public class TestSuite {
	
	@BeforeClass
	public static void setUpClass() {}
	
	@AfterClass
	public static void tearDownClass() {}
	
}
