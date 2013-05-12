package player;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import grid.builder.DeterministicGridBuilderDirector;
import grid.builder.GridBuilderDirector;
import grid.builder.TronGridBuilder;
import java.util.HashSet;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("javadoc")
public class PlayerDBTest {
	
	private PlayerDataBase	playerDB;
	private TronGridBuilder	builder;
	
	@Before
	public void setUp() {
		playerDB = new PlayerDataBase();
		
		builder = new TronGridBuilder();
		new DeterministicGridBuilderDirector(builder, false).construct();
		
		playerDB.createNewDB(builder.getResult().getAllStartingPositions());
	}
	
	@Test(expected = IllegalStateException.class)
	public void testConstructor() {
		playerDB = new PlayerDataBase();
		
		// an empty DB should throw an exception
		playerDB.getCurrentPlayer();
	}
	
	@Test
	public void testCreateNewDB() {
		Set<Player> set1 = getAllPlayerFromDB();
		
		playerDB.createNewDB(builder.getResult().getAllStartingPositions());
		Set<Player> set2 = getAllPlayerFromDB();
		
		// the two lists should contain different players
		containDifferentPlayers(set1, set2);
	}
	
	private void containDifferentPlayers(Set<Player> set1, Set<Player> set2) {
		assertEquals(set1.size(), set2.size());
		for (Player p1 : set1) {
			for (Player p2 : set2) {
				assertNotSame(p1, p2);
			}
		}
	}
	
	/**
	 * Returns a set with all the Players in the {@link PlayerDataBase} (by
	 * simulating player switchs).
	 */
	private Set<Player> getAllPlayerFromDB() {
		Set<Player> result = new HashSet<Player>();
		
		for (int i = 0; i < GridBuilderDirector.NUMBER_OF_PLAYERS; i++) {
			TronPlayer curPlayer = (TronPlayer) playerDB.getCurrentPlayer();
			result.add(curPlayer);
			
			// let the cur player end his turn
			playerDB.endPlayerTurn(curPlayer);
			// now the curPlayer should have changed
			assertNotSame(curPlayer, playerDB.getCurrentPlayer());
		}
		return result;
	}
}
