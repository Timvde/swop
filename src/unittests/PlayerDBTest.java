package unittests;

import grid.Coordinate;
import java.util.ArrayList;
import java.util.Arrays;
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
	
	@Before
	public void setUp() {
		playerDB = new PlayerDataBase();
		exampleCoords = randomCoordArrayOfSize(PlayerDataBase.NUMBER_OF_PLAYERS);
		playerDB.createNewDB(exampleCoords);
	}
	
	@Test
	public void testContstructor() {
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
		List<IPlayer> list = getAllPlayerFromDB();
		// all players should be different
		allDifferent(list);
		
		List<IPlayer> list2 = playerDB.createNewDB(exampleCoords);
		List<IPlayer> list3 = getAllPlayerFromDB();
		Assert.assertEquals(true, Arrays.deepEquals(list2.toArray(), list3.toArray()));
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
			playerDB.createNewDB(null);
		}
		catch (IllegalArgumentException e) {
			exceptionThrown = true;
		}
		Assert.assertEquals(true, exceptionThrown);
		
		// empty array
		Coordinate[] playerStartingCoords = {};
		try {
			playerDB.createNewDB(playerStartingCoords);
		}
		catch (IllegalArgumentException e) {
			exceptionThrown = true;
		}
		Assert.assertEquals(true, exceptionThrown);
		
		// too large array
		playerStartingCoords = randomCoordArrayOfSize(PlayerDataBase.NUMBER_OF_PLAYERS + 1);
		try {
			playerDB.createNewDB(playerStartingCoords);
		}
		catch (IllegalArgumentException e) {
			exceptionThrown = true;
		}
		Assert.assertEquals(true, exceptionThrown);
		
		//duplicate coords
		playerStartingCoords = randomCoordArrayOfSize(PlayerDataBase.NUMBER_OF_PLAYERS);
		playerStartingCoords[playerStartingCoords.length-1] = playerStartingCoords[0];
		try {
			playerDB.createNewDB(playerStartingCoords);
		}
		catch (IllegalArgumentException e) {
			exceptionThrown = true;
		}
		Assert.assertEquals(true, exceptionThrown);
	}
	
	private Coordinate[] randomCoordArrayOfSize(int size) {
		int random = (int) Math.random();
		Coordinate[] result = new Coordinate[size];
		for (int j = 0; j < size; j++) {
			result[j] = new Coordinate(random + j, random);
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
