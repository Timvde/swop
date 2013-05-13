package scenariotests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import item.IItem;
import item.lightgrenade.LightGrenade;
import java.util.List;
import game.Game;
import grid.builder.GridBuilderDirector;
import org.junit.Before;
import org.junit.Test;
import player.IPlayer;
import player.Player;
import player.PlayerState;
import square.Direction;
import controllers.NewGameController;

/**
 * Test the capture the flag mode
 * 
 * 
 * At the start of the game, there is a flag on the starting position of each
 * player. Each player's goal is to capture the flag of the other players, i.e.
 * pick up the flag of another player and return it to your own starting
 * location. A player wins the game when he has captured the flag of each other
 * player at least once, or if all the other players are boxed in, such that
 * they can't move. A player can only carry a single flag at once. If a player
 * is carrying a flag and is hit by a light grenade, an identity disc, a charged
 * identity disc or if he uses a teleporter, the flag is dropped onto a randomly
 * selected square neighbouring the player's current square. A flag can be on a
 * square that already contains other items. When a player steps onto his own
 * starting location carrying another player's flag, that flag is captured. When
 * a player captures a flag, that flag is returned to its starting location.
 * When a player tries to pick up his own flag, his flag is returned to its
 * starting location. A player's own flag does not need to be on its starting
 * location for that player to be able to capture a flag. If a player is boxed
 * in, such that he cannot make a move, the player dies and is removed from the
 * game and disappears. If he was carrying a flag, the flag is dropped on the
 * square where the player was last alive. The other items he possessed
 * disappear.
 */
@SuppressWarnings("javadoc")
public class CaptureTheFlagTest extends SetupTestGrid {
	
	private NewGameController	newGameCont;
	
	@Before
	public void setUp() {
		Game game = new Game();
		newGameCont = new NewGameController(game);
		
		// SetupTestGrid will create the test grid with two player in the
		// @Before
		newGameCont.newCTFGame(GridBuilderDirector.NUMBER_OF_PLAYERS);
	}
	
	/**
	 * Tests: At the start of the game, there is a flag on the starting position
	 * of each player. Each player's goal is to capture the flag of the other
	 * players, i.e. pick up the flag of another player and return it to your
	 * own starting location. A player wins the game when he has captured the
	 * flag of each other player at least once
	 */
	@Test
	public void testPlayerWinsWhenCapturedAllTheFlags() {
		// Player 1
		IPlayer player1 = playerDB.getCurrentPlayer();
		moveCont.move(Direction.SOUTH);
		moveCont.move(Direction.SOUTH);
		// now player 1 is teleported
		moveCont.move(Direction.SOUTH);
		endTurnCont.endTurn();
		
		// player 2 (end turn asap)
		assertNotSame(player1, playerDB.getCurrentPlayer());
		moveCont.move(Direction.EAST);
		endTurnCont.endTurn();
		
		// player 1
		assertEquals(player1, playerDB.getCurrentPlayer());
		moveCont.move(Direction.SOUTH);
		// now player 1 is on the startsquare of player 2
		assertNotSame(PlayerState.FINISHED, ((Player) player1).getPlayerState());
		// pickup the flag:
		List<IItem> itemsList = playerDB.getCurrentPlayer().getCurrentLocation()
				.getCarryableItems();
		assertEquals(1, itemsList.size());
		assertTrue(itemsList.get(0) instanceof Flag);
		Flag flag = (Flag) itemsList.get(0);
		pickUpCont.pickUpItem(Flag);
		// (now player 1 has to return the flag to his start)
		moveCont.move(Direction.NORTH);
		moveCont.move(Direction.NORTH);
		// now player 1 is teleported and his turn is ended (because of 4
		// actions)
		
		// player 2 (end turn asap)
		assertNotSame(player1, playerDB.getCurrentPlayer());
		moveCont.move(Direction.EAST);
		endTurnCont.endTurn();
		
		// player 1
		assertEquals(player1, playerDB.getCurrentPlayer());
		moveCont.move(Direction.NORTH);
		moveCont.move(Direction.NORTH);
		// now player 1 is back on his start with the flag of player 2 --> he
		// wins
		assertEquals(PlayerState.FINISHED, ((Player) player1).getPlayerState());
	}
	
}
