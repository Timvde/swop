package scenariotests;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import controllers.EndTurnController;
import controllers.MoveController;
import game.Game;
import grid.Coordinate;
import grid.Grid;
import grid.GridBuilder;
import player.PlayerDataBase;
import square.ASquare;
import square.Direction;
import square.PowerFailure;

@SuppressWarnings("javadoc")
public class PowerFailureScenarioTest {
	
	private Grid				grid;
	private EndTurnController	endTurnCont;
	private MoveController	moveCont;
	
	@Before
	public void setUp() throws Exception {
		Game game = new Game();
		PlayerDataBase playerDB = new PlayerDataBase();
		grid = new GridBuilder(playerDB.createNewDB()).getPredefinedTestGrid(false);
		
		game.setGrid(grid);
		game.start();
		
		endTurnCont = new EndTurnController(playerDB);
		moveCont = new MoveController(playerDB);
	}
	
	@Test
	public final void testPrimarySecondaryTertiaryPowerFailure() {
		// add a primary power failure
		ASquare sqPrimary = grid.getSquareAt(new Coordinate(1, 6));
		PowerFailure pf = new PowerFailure(sqPrimary);
		// now check the primary
		assertEquals(pf, sqPrimary.getPowerFailure());
		// When the primary power failure starts, one randomly chosen
		// surrounding square has a secondary power failure.
		boolean powerFailureFound = false;
		ASquare sqSecondary = null;
		for (Direction dir : Direction.values()) {
			ASquare neighbor = sqPrimary.getNeighbour(dir);
			if (neighbor != null && neighbor.getPowerFailure() != null) {
				assertFalse(powerFailureFound);
				assertEquals(pf, neighbor.getPowerFailure());
				powerFailureFound = true;
				sqSecondary = neighbor;
			}
		}
		assertTrue(powerFailureFound);
		
		/*
		 * A tertiary power failure occurs on a single square randomly chosen
		 * from the following three squares (T1, T2, T3) adjacent to the
		 * secondary power failure (F2).
		 * 
		 * T1 is on a line with the primary (F1) and secondary (F2) power
		 * failures.
		 */
		ASquare T1 = getSquareOnLineWith(sqPrimary, sqSecondary);
		powerFailureFound = T1.getPowerFailure().equals(pf);
		// T2 and T3 are adjacent to both F2 and T1, and are each either
		// north, east, south, or west of T1.
		for (Direction dir : Direction.NORTH.getPrimaryDirections()) {
			ASquare sq = T1.getNeighbour(dir);
			if (sq != null && sq.isAdjacentTo(sqSecondary) && sq.isAdjacentTo(sqPrimary)) {
				if (sq.getPowerFailure().equals(pf)) {
					assertFalse(powerFailureFound);
					powerFailureFound = true;
				}
			}
		}
		assertTrue(powerFailureFound);
	}
	
	@Test
	public final void testPrimaryPowerFailure_ActionDuration() {
		// add a primary power failure
		ASquare sqPrimary = grid.getSquareAt(new Coordinate(1, 6));
		new PowerFailure(sqPrimary);
		
		/*
		 * A primary power failure lasts for 3 turns.
		 */
		assertTrue(sqPrimary.hasPowerFailure());
		moveCont.move(Direction.NORTH);
		endTurnCont.endTurn();
		
		assertTrue(sqPrimary.hasPowerFailure());
		moveCont.move(Direction.SOUTH);
		endTurnCont.endTurn();
		
		assertTrue(sqPrimary.hasPowerFailure());
		moveCont.move(Direction.NORTH);
		endTurnCont.endTurn();

		assertFalse(sqPrimary.hasPowerFailure());
	}
	
	@Test
	public final void testSecondaryPowerFailure_ActionDuration() {
		// add a primary power failure
		ASquare sqPrimary = grid.getSquareAt(new Coordinate(1, 6));
		PowerFailure pf = new PowerFailure(sqPrimary);
		
		// find the secondary powerfailure
		boolean powerFailureFound = false;
		ASquare sqSecondary = null;
		for (Direction dir : Direction.values()) {
			ASquare neighbor = sqPrimary.getNeighbour(dir);
			if (neighbor != null && neighbor.getPowerFailure() != null) {
				assertFalse(powerFailureFound);
				assertEquals(pf, neighbor.getPowerFailure());
				powerFailureFound = true;
				sqSecondary = neighbor;
			}
		}
		assertTrue(powerFailureFound);
		
		// A secondary power failure lasts for 1 action.
		assertTrue(sqSecondary.hasPowerFailure());
		moveCont.move(Direction.NORTH);
		assertFalse(sqSecondary.hasPowerFailure());
	}
	
	@Test
	public final void testTertiaryPowerFailure_ActionDuration() {
		// add a primary power failure
		ASquare sqPrimary = grid.getSquareAt(new Coordinate(1, 6));
		PowerFailure pf = new PowerFailure(sqPrimary);
		
		// find the secondary powerfailure
		boolean powerFailureFound = false;
		ASquare sqSecondary = null;
		for (Direction dir : Direction.values()) {
			ASquare neighbor = sqPrimary.getNeighbour(dir);
			if (neighbor != null && neighbor.getPowerFailure() != null) {
				assertFalse(powerFailureFound);
				assertEquals(pf, neighbor.getPowerFailure());
				powerFailureFound = true;
				sqSecondary = neighbor;
			}
		}
		assertTrue(powerFailureFound);
		
		// finally find the tertiary power failure
		ASquare sqTertiary = null;
		ASquare T1 = getSquareOnLineWith(sqPrimary, sqSecondary);
		if (T1.getPowerFailure().equals(pf)) {
			sqTertiary = T1;
			powerFailureFound = true;
		}
		// T2 and T3 are adjacent to both F2 and T1, and are each either
		// north, east, south, or west of T1.
		for (Direction dir : Direction.NORTH.getPrimaryDirections()) {
			ASquare sq = T1.getNeighbour(dir);
			if (sq != null && sq.isAdjacentTo(sqSecondary) && sq.isAdjacentTo(sqPrimary)) {
				if (sq.getPowerFailure().equals(pf)) {
					assertFalse(powerFailureFound);
					sqTertiary = sq;
					powerFailureFound = true;
				}
			}
		}
		assertTrue(powerFailureFound);
		
		// A tertiary power failure lasts for 1 action.
		assertTrue(sqTertiary.hasPowerFailure());
		moveCont.move(Direction.NORTH);
		assertFalse(sqTertiary.hasPowerFailure());
	}
	
	private ASquare getSquareOnLineWith(ASquare sqPrimary, ASquare sqSecondary) {
		for (Direction dir : Direction.values()) {
			if (sqPrimary.getNeighbour(dir).equals(sqSecondary)) {
				return sqSecondary.getNeighbour(dir);
			}
		}
		fail("sqPrimary and sqSecondary are no neighbors!");
		return null;
	}
}
