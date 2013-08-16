package unittests;

import grid.CoordinateTest;
import grid.GridTest;
import grid.builder.DeterministicDirectorTest;
import grid.builder.FileDirectorTest;
import grid.builder.RandomDirectorTest;
import grid.builder.TronGridBuilderTest;
import item.EffectTest;
import item.forcefieldgenerator.ForceFieldGeneratorTest;
import item.identitydisk.IdentityDiskTest;
import item.lightgrenade.LightGrenadeTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import player.InventoryTest;
import player.LightTrailTest;
import player.PlayerDBTest;
import player.PlayerTest;
import powerfailure.PowerFailureTest;
import powerfailure.PrimaryPowerFailureTest;
import powerfailure.SecondaryPowerFailureTest;
import scenariotests.CaptureTheFlagTest;
import scenariotests.EndTurnTest;
import scenariotests.MovePlayerTest;
import scenariotests.PickUpTest;
import scenariotests.StartGameTest;
import scenariotests.TeleportationTest;
import scenariotests.UseItemTest;
import square.ASquareTest;
import square.DirectionTest;
import square.SquareTest;
import square.WallPartTest;
import square.WallTest;

@SuppressWarnings("javadoc")
@RunWith(Suite.class)
		
@SuiteClasses({ CoordinateTest.class, EffectTest.class, GridTest.class, InventoryTest.class,
		LightTrailTest.class, PlayerDBTest.class, PlayerTest.class, SquareTest.class,
		WallTest.class, EndTurnTest.class, MovePlayerTest.class, PickUpTest.class,
		StartGameTest.class, UseItemTest.class, LightGrenadeTest.class, DeterministicDirectorTest.class, 
		FileDirectorTest.class, RandomDirectorTest.class,
		TronGridBuilderTest.class, IdentityDiskTest.class,
		PowerFailureTest.class, PrimaryPowerFailureTest.class, SecondaryPowerFailureTest.class,
		CaptureTheFlagTest.class, scenariotests.IdentityDiskTest.class, TeleportationTest.class,
		ASquareTest.class, DirectionTest.class, square.PowerFailureTest.class, SquareTest.class,
		WallPartTest.class,ForceFieldGeneratorTest.class, WallTest.class
		})
public class TestSuite {
	
	@BeforeClass
	public static void setUpClass() {}
	
	@AfterClass
	public static void tearDownClass() {}
	
}
