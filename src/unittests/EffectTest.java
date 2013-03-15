package unittests;

import static org.junit.Assert.assertEquals;
import grid.Coordinate;
import grid.Direction;
import grid.Grid;
import grid.GridBuilder;
import grid.PowerFailure;
import item.Effect;
import item.Item;
import item.LightGrenade;
import java.util.Random;
import org.junit.Before;
import org.junit.Test;
import ObjectronExceptions.IllegalMoveException;
import player.IPlayer;
import player.Player;

@SuppressWarnings("javadoc")
public class EffectTest {
	
	private IPlayer	player;
	private Effect	effect;
	private Grid	grid;
	
	@Before
	public void setUp() {
		grid = new GridBuilder().getPredefinedTestGrid();
		player = new Player(new Coordinate(0, 9), grid);
		// Set the number of actions left at 2 initially to get a different
		// result from the light grenade and the power failure
		player.skipNumberOfActions(1);
		effect = new Effect(player);
	}
	
	@Test
	public void testLightGrenade() {
		effect.addLightGrenade();
		effect.execute();
		// This will result in a negative number of actions left (i.e. the
		// penalty for his next turn) The player will see he has no actions left
		// and notify the database (to end his turn) and give himself again 3
		// actions for his next
		// turn.
		assertEquals(player.getAllowedNumberOfActions(), -1 + Player.MAX_NUMBER_OF_ACTIONS_PER_TURN);
	}
	
	@Test
	public void testPowerFailure() throws IllegalStateException, IllegalArgumentException, IllegalMoveException {
		grid.addPowerFailureAtCoordinate(new Coordinate(0, 7));
		// a player always has already done a move-action when it hits a
		// power failure. This is necessary to do a successful endTurn().
		try {
			player.moveInDirection(Direction.NORTH);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		assertEquals(player.getAllowedNumberOfActions(), 0 + Player.MAX_NUMBER_OF_ACTIONS_PER_TURN);
	}
	
	@Test
	public void testLightGrenadeAndPowerFailure() {
		grid.addPowerFailureAtCoordinate(new Coordinate(0, 7));
		Item lightGrenade = new LightGrenade();
		lightGrenade.use(grid.getSquareAt(new Coordinate(0, 8)));
		try {
			player.moveInDirection(Direction.NORTH);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		assertEquals(player.getAllowedNumberOfActions(), -2 + Player.MAX_NUMBER_OF_ACTIONS_PER_TURN);
	}
	
}
