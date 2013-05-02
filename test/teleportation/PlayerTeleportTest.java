package teleportation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import player.Player;
import player.PlayerDataBase;
import square.AbstractSquare;
import square.Direction;
import square.NormalSquare;
import square.WallPart;

@SuppressWarnings("javadoc")
public class PlayerTeleportTest {
	
	private NormalSquare	start;
	private NormalSquare	destination;
	private Player	player;
	
	@Before
	public void setUp() throws Exception {
		Map<Direction, AbstractSquare> neighbours = new HashMap<Direction, ASquare>();
		start = new NormalSquare(neighbours);
		destination = new NormalSquare(neighbours);
		PlayerDataBase db = new PlayerDataBase();
		db.createNewDB();
		player = (Player) db.getCurrentPlayer();
		start.addPlayer(player);
		player.setStartingPosition(start);
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
