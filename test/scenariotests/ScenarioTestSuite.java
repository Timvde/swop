package scenariotests;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@SuppressWarnings("javadoc")
@RunWith(Suite.class)
@SuiteClasses({ CaptureTheFlagTest.class, TeleportationTest.class, IdentityDiskTest.class, EndTurnTest.class, MovePlayerTest.class, PickUpTest.class, StartGameTest.class, UseItemTest.class })
    public class ScenarioTestSuite {
	
	    @BeforeClass 
	    public static void setUpClass() {
	    }
	
	    @AfterClass
	    public static void tearDownClass() { 
	    }

}