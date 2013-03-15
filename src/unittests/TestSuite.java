package unittests;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import scenariotests.EndTurnTest;
import scenariotests.MovePlayerTest;
import scenariotests.PickUpTest;
import scenariotests.StartGameTest;
import scenariotests.UseItemTest;

@SuppressWarnings("javadoc")
@RunWith(Suite.class)
@SuiteClasses({ CoordinateTest.class, EffectTest.class, GridTest.class, InventoryTest.class,
		LightTrailTest.class, PlayerDBTest.class, PlayerTest.class, SquareTest.class,
		WallTest.class, EndTurnTest.class, MovePlayerTest.class, PickUpTest.class,
		StartGameTest.class, UseItemTest.class })
public class TestSuite {
	
	@BeforeClass
	public static void setUpClass() {}
	
	@AfterClass
	public static void tearDownClass() {}
	
}
