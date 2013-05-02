package teleportation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import grid.builder.DeterministicGridBuilderDirector;
import grid.builder.TronGridBuilder;
import java.util.Collections;
import java.util.HashMap;
import org.junit.Before;
import org.junit.Test;
import player.Player;
import player.PlayerDataBase;
import square.ASquare;
import square.Direction;
import square.Square;
import square.WallPart;

@SuppressWarnings("javadoc")
public class PlayerTeleportTest {
	
	private Square	start;
	private Square	destination;
	private Player	player;
	
	@Before
	public void setUp() throws Exception {
		start = new Square(Collections.<Direction, ASquare> emptyMap());
		destination = new Square(Collections.<Direction, ASquare> emptyMap());
		
		PlayerDataBase db = new PlayerDataBase();
		TronGridBuilder builder = new TronGridBuilder();
		new DeterministicGridBuilderDirector(builder, false).construct();
		db.createNewDB(builder.getResult().getAllStartingPositions());
		
		player = (Player) db.getCurrentPlayer();
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
		player.teleportTo(new WallPart(new HashMap<Direction, ASquare>()));
	}
}
