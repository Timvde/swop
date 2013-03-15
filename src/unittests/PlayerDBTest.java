package unittests;

import grid.Coordinate;
import grid.Grid;
import grid.GridBuilder;
import java.util.ArrayList;
import java.util.List;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import player.IPlayer;
import player.Player;
import player.PlayerDataBase;

@SuppressWarnings("javadoc")
public class PlayerDBTest {
	
	private PlayerDataBase	playerDB;
	private Coordinate[]	exampleCoords;
	private Grid			grid;
	
	@Before
	public void setUp() {
		grid = new GridBuilder().getPredefinedTestGrid();
		playerDB = new PlayerDataBase(grid);
		exampleCoords = randomCoordArrayOfSize(PlayerDataBase.NUMBER_OF_PLAYERS);
		playerDB.createNewDB(exampleCoords, grid);
	}
	
	@Test
	public void testConstructor() {
		playerDB = new PlayerDataBase(grid);
		
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
		List<IPlayer> list = getAllPlayerFromDB();
		// all players should be different
		allDifferent(list);
		
		playerDB.createNewDB(exampleCoords, grid);
		List<IPlayer> list2 = getAllPlayerFromDB();
		// all players should be different
		allDifferent(list2);
		
		// the two lists should contain different players
		containDifferentPlayers(list, list2);
	}
	
	@Test
	public void testCreateNewDBIllegalInput() {
		boolean exceptionThrown = false;
		
		// null
		try {
			playerDB.createNewDB(null, null);
		}
		catch (IllegalArgumentException e) {
			exceptionThrown = true;
		}
		Assert.assertEquals(true, exceptionThrown);
		
		// empty array
		Coordinate[] playerStartingCoords = {};
		try {
			playerDB.createNewDB(playerStartingCoords, grid);
		}
		catch (IllegalArgumentException e) {
			exceptionThrown = true;
		}
		Assert.assertEquals(true, exceptionThrown);
		
		// too large array
		playerStartingCoords = randomCoordArrayOfSize(PlayerDataBase.NUMBER_OF_PLAYERS + 1);
		try {
			playerDB.createNewDB(playerStartingCoords, grid);
		}
		catch (IllegalArgumentException e) {
			exceptionThrown = true;
		}
		Assert.assertEquals(true, exceptionThrown);
		
		// duplicate coords
		playerStartingCoords = randomCoordArrayOfSize(PlayerDataBase.NUMBER_OF_PLAYERS);
		playerStartingCoords[playerStartingCoords.length - 1] = playerStartingCoords[0];
		try {
			playerDB.createNewDB(playerStartingCoords, grid);
		}
		catch (IllegalArgumentException e) {
			exceptionThrown = true;
		}
		Assert.assertEquals(true, exceptionThrown);
	}
	
	/**
	 * Genenerates an array with a specified number of randomly created
	 * coordinates in the grid defined by
	 * {@link GridBuilder#getPredefinedTestGrid()}
	 */
	private Coordinate[] randomCoordArrayOfSize(int size) {
		Coordinate[] result = new Coordinate[size];
		for (int j = 0; j < size; j++) {
			result[j] = GridBuilder.getRandomCoordOnTestGrid();
		}
		return result;
	}
	
	private void allDifferent(List<IPlayer> list) {
		for (int i = 0; i < list.size() - 1; i++) {
			IPlayer player1 = list.get(i);
			for (int j = i + 1; j < list.size(); j++) {
				IPlayer player2 = list.get(j);
				Assert.assertNotSame(player1, player2);
			}
		}
	}
	
	private void containDifferentPlayers(List<IPlayer> list1, List<IPlayer> list2) {
		Assert.assertEquals(list1.size(), list2.size());
		for (int i = 0; i < list1.size(); i++) {
			Assert.assertNotSame(list1.get(i), list2.get(i));
		}
	}
	
	private List<IPlayer> getAllPlayerFromDB() {
		List<IPlayer> result = new ArrayList<IPlayer>();
		
		for (int i = 0; i < PlayerDataBase.NUMBER_OF_PLAYERS; i++) {
			IPlayer curPlayer = (Player) playerDB.getCurrentPlayer();
			result.add(curPlayer);
			
			// simulate a notifyObserves from curPlayer to indicate he wants to
			// end his turn
			playerDB.update((Player) curPlayer, null);
			// now the curPlayer should have changed
		}
		return result;
	}
	
}
