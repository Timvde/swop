package scenariotests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import game.Game;
import grid.Grid;
import grid.GridBuilder;
import item.IItem;
import item.forcefield.ForceField;
import item.forcefield.ForceFieldGenerator;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import player.PlayerDataBase;
import square.ASquare;
import square.Direction;
import ObjectronExceptions.IllegalMoveException;
import controllers.EndTurnController;
import controllers.MoveController;
import controllers.PickUpItemController;
import controllers.UseItemController;

@SuppressWarnings("javadoc")
public class ForceFieldTest {
	
	private MoveController			moveCont;
	private EndTurnController		endTurnCont;
	private PlayerDataBase			playerDB;
	private PickUpItemController	pickUpCont;
	private UseItemController		useItemCont;
	private ForceField				forcefield;
	
	@Before
	public void setUp() throws Exception {
		Game game = new Game();
		playerDB = new PlayerDataBase();
		Grid grid = new GridBuilder(playerDB.createNewDB()).getPredefinedTestGrid(false);
		// the predefined test grid has a forcefieldgen NORTHEAST of player 1
		// and SOUTH of player 2
		
		game.setGrid(grid);
		game.start();
		
		moveCont = new MoveController(playerDB);
		endTurnCont = new EndTurnController(playerDB);
		pickUpCont = new PickUpItemController(playerDB);
		useItemCont = new UseItemController(playerDB);
	}
	
	@Test
	public void testGenerateForceField() {
		// move player 1 to the force field generator and pick it up
		moveCont.move(Direction.NORTHEAST);
		IItem forceFieldGen = playerDB.getCurrentPlayer().getCurrentLocation().getCarryableItems()
				.get(0);
		pickUpCont.pickUpItem(forceFieldGen);
		/*
		 * Force field generators in the inventory of a player are always
		 * inactive
		 */
		ForceFieldGenerator ffg1 = (ForceFieldGenerator) forceFieldGen;
		assertFalse(ffg1.hasActiveForceField());
		// now move east and drop the forcefieldgenerator
		moveCont.move(Direction.EAST);
		useItemCont.useItem(forceFieldGen);
		
		// move player 2 to the force field generator and pick it up
		moveCont.move(Direction.SOUTH);
		forceFieldGen = playerDB.getCurrentPlayer().getCurrentLocation().getCarryableItems().get(0);
		pickUpCont.pickUpItem(forceFieldGen);
		/*
		 * Force field generators in the inventory of a player are always
		 * inactive and do not influence other force field generators.
		 */
		ForceFieldGenerator ffg2 = (ForceFieldGenerator) forceFieldGen;
		assertFalse(ffg2.hasActiveForceField());
		assertFalse(ffg1.hasActiveForceField());
		// use the teleporter
		moveCont.move(Direction.SOUTH);
		// now drop the forcefieldgenerator. It should be activated now.
		useItemCont.useItem(forceFieldGen);
		assertTrue(ffg2.hasActiveForceField());
		assertTrue(ffg1.hasActiveForceField());
		
		// get the generated forcefield and check it
		forcefield = getForceFieldBetween(ffg1, ffg2);
		assertTrue(forcefield.isActive());
		/*
		 * A force field forms a straight line between its two generators, and
		 * includes the squares of these generators.
		 */
		ArrayList<ASquare> list = new ArrayList<ASquare>();
		list.add(playerDB.getCurrentPlayer().getCurrentLocation());
		list.add(playerDB.getCurrentPlayer().getCurrentLocation().getNeighbour(Direction.WEST));
		list.add(playerDB.getCurrentPlayer().getCurrentLocation().getNeighbour(Direction.WEST)
				.getNeighbour(Direction.WEST));
		assertEquals(list, forcefield.getSquares());
	}
	
	@Test
	public void testForceField_Move() {
		testGenerateForceField();
		assertTrue(forcefield.isActive());
		
		/*
		 * A force field switches on and off after every 2 actions that have
		 * been performed.
		 * 
		 * When a force field is activated and a player is present on one of its
		 * squares, that player cannot move until the force field deactivates.
		 */
		assertTrue(forcefield.isActive());
		assertMoveException(Direction.EAST);
		endTurnCont.endTurn();
		
		// player 2 is also trapped in the forcefield
		assertTrue(forcefield.isActive());
		assertMoveException(Direction.SOUTH);
		endTurnCont.endTurn();
		
		// now the forcefield should be deactivated (2 actions has been
		// performed) --> player 1 can move again
		assertFalse(forcefield.isActive());
		moveCont.move(Direction.EAST);
		assertFalse(forcefield.isActive());
		/*
		 * A player cannot pas through a force field, or enter one of the
		 * squares of the force field.
		 */
		assertMoveException(Direction.WEST);
		assertFalse(forcefield.isActive());
		endTurnCont.endTurn();
		
		assertTrue(forcefield.isActive());
	}
	
	private void assertMoveException(Direction dir) {
		boolean exceptionthrown = false;
		try {
			moveCont.move(dir);
		}
		catch (IllegalMoveException e) {
			exceptionthrown = true;
		}
		assertTrue(exceptionthrown);
	}
	
	private ForceField getForceFieldBetween(ForceFieldGenerator ffg1, ForceFieldGenerator ffg2) {
		// TODO implement...
		return null;
	}
}
