package teleportation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import player.Player;
import square.ASquare;
import square.Direction;
import square.Square;
import square.WallPart;

@SuppressWarnings("javadoc")
public class PlayerTeleportTest {

	private Square start;
	private Square destination;
	private Player player;
	
	@Before
	public void setUp() throws Exception {
		Map<Direction, ASquare> neighbours = new HashMap<Direction, ASquare>();
		start = new Square(neighbours);
		destination = new Square(neighbours);
		player = new Player(start);
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
	
	@Test (expected = IllegalArgumentException.class)
	public void testTeleportation_nullArgument() {
		player.teleportTo(null);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testTeleportation_wallArgument() {
		player.teleportTo(new WallPart(new HashMap<Direction, ASquare>()));
	}
}
