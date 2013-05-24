package teleportation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import grid.builder.DeterministicGridBuilderDirector;
import grid.builder.TronGridBuilder;
import item.DummyEffectFactory;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import player.TronPlayer;
import player.PlayerDataBase;
import square.AbstractSquare;
import square.Direction;
import square.NormalSquare;
import square.WallPart;

@SuppressWarnings("javadoc")
public class PlayerTeleportTest {
	
	private NormalSquare	start;
	private NormalSquare	destination;
	private TronPlayer	player;
	
	@Before
	public void setUp() throws Exception {
		start = new NormalSquare();
		destination = new NormalSquare();
		PlayerDataBase db = new PlayerDataBase();
		TronGridBuilder builder = new TronGridBuilder(new DummyEffectFactory());
		new DeterministicGridBuilderDirector(builder, false).construct();
		db.createNewDB(builder.getResult().getAllStartingPositions());
		
		player = (TronPlayer) db.getCurrentPlayer();
		// start.addPlayer(player);
		// player.setStartingPosition(start);
		
		// TODO change setup so it uses the teleporters of the deterministic
		// grid
	}
	
	@Test
	public void testTeleportation() {
		// test if the player has shown up for this unit test
		assertEquals(player, start.getPlayer());
		// teleport the player to the destination
		player.teleportTo(destination);
		// test results
		assertFalse(start.hasPlayer());
		assertEquals(player, destination.getPlayer());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testTeleportation_nullArgument() {
		player.teleportTo(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testTeleportation_wallArgument() {
		player.teleportTo(new WallPart(new HashMap<Direction, AbstractSquare>()));
	}
}
