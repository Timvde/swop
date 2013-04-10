package player;

import grid.GridBuilder;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import square.ASquare;
import square.Direction;
import square.Square;
import square.WallPart;

/**
 * Tests the creation of a {@link PlayerDataBase}. Player switching behavior is
 * tested in {@link PlayerTest}.
 * 
 */
@SuppressWarnings("javadoc")
public class PlayerDBTest {
	
	private PlayerDataBase	playerDB;
	private Set<ASquare>	playerPositions;
	
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
		// all players are different (set would throw an excption otherwise)
		Set<IPlayer> set1 = getAllPlayerFromDB();
		
		// use the same starting positions
		playerDB.createNewDB(playerPositions);
		Set<IPlayer> set2 = getAllPlayerFromDB();
		
		// the two lists should contain different players
		containDifferentPlayers(set1, set2);
	}
	
	@Test
	public void testCreateNewDBDiffStartingPos() {
		// all players are different (set would throw an excption otherwise)
		Set<IPlayer> set1 = getAllPlayerFromDB();
		
		// use different starting positions
		playerDB.createNewDB(this.getPlayerStartingPosSetOfSize(PlayerDataBase.NUMBER_OF_PLAYERS));
		Set<IPlayer> set2 = getAllPlayerFromDB();
		
		// the two lists should contain different players
		containDifferentPlayers(set1, set2);
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
		
		// empty set
		try {
			playerDB.createNewDB(new HashSet<ASquare>());
		}
		catch (IllegalArgumentException e) {
			exceptionThrown = true;
		}
		Assert.assertEquals(true, exceptionThrown);
		
		// too large array
		try {
			playerDB.createNewDB(getPlayerStartingPosSetOfSize(PlayerDataBase.NUMBER_OF_PLAYERS + 1));
		}
		catch (IllegalArgumentException e) {
			exceptionThrown = true;
		}
		Assert.assertEquals(true, exceptionThrown);
		
		// walls
		try {
			playerDB.createNewDB(getWallSetOfSize(PlayerDataBase.NUMBER_OF_PLAYERS + 1));
		}
		catch (IllegalArgumentException e) {
			exceptionThrown = true;
		}
		Assert.assertEquals(true, exceptionThrown);
	}
	
	/**
	 * Genenerates a set with a specified number of newly created squares.
	 */
	private Set<ASquare> getPlayerStartingPosSetOfSize(int size) {
		Set<ASquare> result = new HashSet<ASquare>();
		for (int j = 0; j < size; j++) {
			result.add(new Square(Collections.<Direction, ASquare> emptyMap()));
		}
		return result;
	}
	
	private Set<ASquare> getWallSetOfSize(int size) {
		Set<ASquare> result = new HashSet<ASquare>();
		for (int j = 0; j < size; j++) {
			result.add(new WallPart(Collections.<Direction, ASquare> emptyMap()));
		}
		return result;
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
			playerDB.endPlayerTurn((Player) curPlayer);
			// now the curPlayer should have changed
		}
		return result;
	}
}
