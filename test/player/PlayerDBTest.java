package player;

import grid.GridBuilder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import square.ASquare;
import square.Direction;
import square.Square;

@SuppressWarnings("javadoc")
public class PlayerDBTest {
	
	private PlayerDataBase	playerDB;
	private Square[]		playerPositions;
	
	@Before
	public void setUp() {
		playerDB = new PlayerDataBase();
		playerPositions = new GridBuilder().getPlayerStartingPositionsOnTestGrid();
		playerDB.createNewDB(playerPositions);
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
		List<IPlayer> list = getAllPlayerFromDB();
		// all players should be different
		allDifferent(list);
		
		// use the same starting positions
		playerDB.createNewDB(playerPositions);
		List<IPlayer> list2 = getAllPlayerFromDB();
		// all players should be different
		allDifferent(list2);
		
		// the two lists should contain different players
		containDifferentPlayers(list, list2);
	}
	
	@Test
	public void testCreateNewDBDiffStartingPos() {
		List<IPlayer> list = getAllPlayerFromDB();
		// all players should be different
		allDifferent(list);
		
		// use different starting positions
		playerDB.createNewDB(this.getSquareArrayOfSize(PlayerDataBase.NUMBER_OF_PLAYERS));
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
			playerDB.createNewDB(null);
		}
		catch (IllegalArgumentException e) {
			exceptionThrown = true;
		}
		Assert.assertEquals(true, exceptionThrown);
		
		// empty array
		Square[] playerStartingPositions = {};
		try {
			playerDB.createNewDB(playerStartingPositions);
		}
		catch (IllegalArgumentException e) {
			exceptionThrown = true;
		}
		Assert.assertEquals(true, exceptionThrown);
		
		// too large array
		playerStartingPositions = getSquareArrayOfSize(PlayerDataBase.NUMBER_OF_PLAYERS + 1);
		try {
			playerDB.createNewDB(playerStartingPositions);
		}
		catch (IllegalArgumentException e) {
			exceptionThrown = true;
		}
		Assert.assertEquals(true, exceptionThrown);
		
		// duplicate coords
		playerStartingPositions = getSquareArrayOfSize(PlayerDataBase.NUMBER_OF_PLAYERS);
		playerStartingPositions[playerStartingPositions.length - 1] = playerStartingPositions[0];
		try {
			playerDB.createNewDB(playerStartingPositions);
		}
		catch (IllegalArgumentException e) {
			exceptionThrown = true;
		}
		Assert.assertEquals(true, exceptionThrown);
	}
	
	/**
	 * Genenerates an array with a specified number of newly created
	 * squares.
	 */
	private Square[] getSquareArrayOfSize(int size) {
		Square[] result = new Square[size];
		for (int j = 0; j < size; j++) {
			result[j] = new Square(Collections.<Direction, ASquare> emptyMap());
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
			
			//let the cur player end his turn
			playerDB.endPlayerTurn((Player) curPlayer);
			// now the curPlayer should have changed
		}
		return result;
	}
	
}
