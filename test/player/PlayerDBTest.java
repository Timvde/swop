package player;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import grid.builder.DeterministicGridBuilderDirector;
import grid.builder.TronGridBuilder;
import item.DummyEffectFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import square.Direction;
import square.NormalSquare;
import square.SquareContainer;
import square.StartingPositionProperty;

@SuppressWarnings("javadoc")
public class PlayerDBTest {
	
	private PlayerDataBase	playerDB;
	private TronGridBuilder	builder;
	
	@Before
	public void setUp() {
		playerDB = new PlayerDataBase();
		
		// build a grid to fill the db
		builder = new TronGridBuilder(new DummyEffectFactory());
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
		List<Player> playerList = playerDB.getAllPlayers();
		assertIsCorrectPlayerList(playerList);
		
		playerDB.createNewDB(builder.getResult().getAllStartingPositions());
		List<Player> playerList2 = playerDB.getAllPlayers();
		assertIsCorrectPlayerList(playerList2);
		
		// the two lists should contain different players
		containDifferentPlayers(playerList, playerList2);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCreateNewDBNullInput() {
		playerDB.createNewDB(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCreateNewDBEmptyListInput() {
		playerDB.createNewDB(new ArrayList<SquareContainer>());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCreateNewDBDuplicateListInput() {
		List<SquareContainer> list = new ArrayList<SquareContainer>();
		SquareContainer square1 = new SquareContainer(
				Collections.<Direction, SquareContainer> emptyMap(), new NormalSquare());
		square1.addProperty(new StartingPositionProperty());
		list.add(square1);
		list.add(square1);
		
		playerDB.createNewDB(list);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCreateNewDBNoStartingPos() {
		List<SquareContainer> list = new ArrayList<SquareContainer>();
		SquareContainer square1 = new SquareContainer(
				Collections.<Direction, SquareContainer> emptyMap(), new NormalSquare());
		SquareContainer square2 = new SquareContainer(
				Collections.<Direction, SquareContainer> emptyMap(), new NormalSquare());
		list.add(square1);
		list.add(square2);
		
		playerDB.createNewDB(list);
	}
	
	@Test
	public void testClearDB() {
		List<Player> playerList = playerDB.getAllPlayers();
		playerDB.clearDataBase();
		assertSame(0, playerDB.getNumberOfPlayers());
		
		for (Player player : playerList) {
			assertFalse(player.canPerformAction());
		}
	}
	
	private void assertIsCorrectPlayerList(List<Player> playerList) {
		// all players should have WAITING state
		for (Player player : playerList)
			assertSame(PlayerState.WAITING, ((TronPlayer) player).getPlayerState());
		
		assertIsSet(playerList);
		assertCorrectOrder(playerList);
	}
	
	private void assertIsSet(List<Player> playerList) {
		Set<Player> set = new HashSet<Player>(playerList);
		assertSame(playerList.size(), set.size());
	}
	
	private void assertCorrectOrder(List<Player> playerList) {
		List<Player> orderedList = this.getAllPlayerFromDB();
		
		assertSame(orderedList.size(), playerList.size());
		for (int i = 0; i < orderedList.size(); i++)
			assertSame(playerList.get(i), orderedList.get(i));
	}
	
	private void containDifferentPlayers(List<Player> playerList, List<Player> playerList2) {
		assertEquals(playerList.size(), playerList2.size());
		for (Player p1 : playerList)
			for (Player p2 : playerList2)
				assertNotSame(p1, p2);
	}
	
	/**
	 * Returns an ordered list with all the Players in the
	 * {@link PlayerDataBase} (by simulating player switches).
	 */
	private List<Player> getAllPlayerFromDB() {
		List<Player> result = new ArrayList<Player>();
		
		for (int i = 0; i < playerDB.getNumberOfPlayers(); i++) {
			TronPlayer curPlayer = (TronPlayer) playerDB.getCurrentPlayer();
			result.add(curPlayer);
			
			// let the cur player end his turn
			playerDB.getCurrentPlayer().endTurn();
			// now the curPlayer should have changed
			assertNotSame(curPlayer, playerDB.getCurrentPlayer());
			assertFalse(curPlayer.canPerformAction());
		}
		return result;
	}
}
