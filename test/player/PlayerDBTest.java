package player;

import grid.Coordinate;
import grid.Grid;
import grid.GridBuilder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("javadoc")
public class PlayerDBTest {
	
	private PlayerDataBase	playerDB;
	private Coordinate[]	exampleCoords;
	private Grid			grid;
	
	@Before
	public void setUp() {
		playerDB = new PlayerDataBase();
		exampleCoords = new Coordinate[] {
				new Coordinate(GridBuilder.PREDIFINED_GRID_SIZE - 1,
						GridBuilder.PREDIFINED_GRID_SIZE - 1), new Coordinate(0, 0) };
		List<Player> players = playerDB.createNewDB();
		grid = new GridBuilder(players).getPredefinedTestGrid(false);
	}
	
	@Test
	public void testConstructor() {
		playerDB = new PlayerDataBase();
		
		// an empty DB should throw an exception
		boolean exceptionThrown = false;
		try {
			playerDB.getCurrentPlayer();
		}
		catch (IllegalStateException e) {
			exceptionThrown = true;
		}
		Assert.assertEquals(true, exceptionThrown);
	}
	
	@Test
	public void testCreateNewDB() {
		Set<IPlayer> set1 = getAllPlayerFromDB();
		
		;
		Set<IPlayer> set2 = new HashSet<IPlayer>(playerDB.createNewDB());
		
		// the two lists should contain different players
		containDifferentPlayers(set1, set2);
	}
	
	// TODO: Deze test kan verwijderd worden, right?
	// @Test
	// public void testCreateNewDBIllegalInput() {
	// boolean exceptionThrown = false;
	//
	// // null
	// try {
	// playerDB.createNewDB(null, null);
	// }
	// catch (IllegalArgumentException e) {
	// exceptionThrown = true;
	// }
	// Assert.assertEquals(true, exceptionThrown);
	//
	// // empty array
	// Coordinate[] playerStartingCoords = {};
	// try {
	// playerDB.createNewDB(playerStartingCoords, grid);
	// }
	// catch (IllegalArgumentException e) {
	// exceptionThrown = true;
	// }
	// Assert.assertEquals(true, exceptionThrown);
	//
	// // too large array
	// playerStartingCoords =
	// randomCoordArrayOfSize(PlayerDataBase.NUMBER_OF_PLAYERS + 1);
	// try {
	// playerDB.createNewDB(playerStartingCoords, grid);
	// }
	// catch (IllegalArgumentException e) {
	// exceptionThrown = true;
	// }
	// Assert.assertEquals(true, exceptionThrown);
	//
	// // duplicate coords
	// playerStartingCoords =
	// randomCoordArrayOfSize(PlayerDataBase.NUMBER_OF_PLAYERS);
	// playerStartingCoords[playerStartingCoords.length - 1] =
	// playerStartingCoords[0];
	// try {
	// playerDB.createNewDB(playerStartingCoords, grid);
	// }
	// catch (IllegalArgumentException e) {
	// exceptionThrown = true;
	// }
	// Assert.assertEquals(true, exceptionThrown);
	// }
	//
	// /**
	// * Genenerates an array with a specified number of randomly created
	// * coordinates in the grid defined by
	// * {@link GridBuilder#getPredefinedTestGrid()}
	// */
	// private Coordinate[] randomCoordArrayOfSize(int size) {
	// Coordinate[] result = new Coordinate[size];
	// for (int j = 0; j < size; j++) {
	// result[j] = GridBuilder.getRandomCoordOnTestGrid();
	// }
	// return result;
	// }
	
	private void allDifferent(List<IPlayer> list) {
		for (int i = 0; i < list.size() - 1; i++) {
			IPlayer player1 = list.get(i);
			for (int j = i + 1; j < list.size(); j++) {
				IPlayer player2 = list.get(j);
				Assert.assertNotSame(player1, player2);
			}
		}
	}
	
	private void containDifferentPlayers(Set<IPlayer> set1, Set<IPlayer> set2) {
		Assert.assertEquals(set1.size(), set2.size());
		for (IPlayer p1 : set1) {
			for (IPlayer p2 : set2) {
				Assert.assertNotSame(p1, p2);
			}
		}
	}
	
	/**
	 * Returns a set with all the Players in a specified {@link PlayerDataBase}
	 * (by simulating player switchs).
	 */
	private Set<IPlayer> getAllPlayerFromDB() {
		Set<IPlayer> result = new HashSet<IPlayer>();
		
		for (int i = 0; i < PlayerDataBase.NUMBER_OF_PLAYERS; i++) {
			Player curPlayer = (Player) playerDB.getCurrentPlayer();
			result.add(curPlayer);
			
			// let the cur player end his turn
			playerDB.endPlayerTurn(curPlayer);
			// now the curPlayer should have changed
		}
		return result;
	}
}
