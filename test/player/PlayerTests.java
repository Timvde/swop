package player;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


@RunWith(Suite.class)
@SuiteClasses({ InventoryTest.class, LightTrailTest.class, PlayerDBTest.class, PlayerTest.class })
public class PlayerTests {
	
}
