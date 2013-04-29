package player;

import grid.RandomGridBuilder;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("javadoc")
public class PlayerDBTest {
	
	private PlayerDataBase	playerDB;
	
	@Before
	public void setUp() {
		playerDB = new PlayerDataBase();
		List<Player> players = playerDB.createNewDB();
		
		// Needed to initialize the players properly
		new RandomGridBuilder(players).getPredefinedTestGrid(false);
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
		
		Set<IPlayer> set2 = new HashSet<IPlayer>(playerDB.createNewDB());
		
		// the two lists should contain different players
		containDifferentPlayers(set1, set2);
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
