package tests;

import static org.junit.Assert.*;
import grid.Coordinate;
import java.util.HashMap;
import java.util.Map;
import org.junit.BeforeClass;
import org.junit.Test;
import ObjectronExceptions.IllegalNumberOfPlayersException;
import player.Player;
import player.PlayerManager;

public class PlayerManagerTest {
	
	private static PlayerManager	playerManager;
	private static Player player1;
	private static Player player2;
	
	@BeforeClass
	public static void setUpBeforeClass() {
		Map<Player, Coordinate> coordinates = new HashMap<Player, Coordinate>();
		Coordinate coord1 = new Coordinate(9, 0);
		Coordinate coord2 = new Coordinate(0, 9);
		player1 = new Player(coord1);
		player2 = new Player(coord2);
		coordinates.put(player1, coord2);
		coordinates.put(player2, coord1);
		try {
			playerManager = new PlayerManager(coordinates);
		}
		catch (IllegalNumberOfPlayersException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testCurrentPlayer() {
		assertTrue(true);
	}
	
}
