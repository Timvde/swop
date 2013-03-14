package unittests;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import player.IPlayer;
import player.Player;
import player.PlayerDataBase;

public class PlayerDBTest {

	private PlayerDataBase playerDB;

	@Before
	public void setUp() {
		playerDB = new PlayerDataBase();
	}

	@Test
	public void testCreateNewDB() {
		List<IPlayer> list = getAllPlayerFromDB();
		// all players should be different
		allDifferent(list);

		playerDB.createNewDB();
		List<IPlayer> list2 = getAllPlayerFromDB();
		// all players shold be different
		allDifferent(list2);

		// the two lists should contain different players
		containDifferentPlayers(list, list2);
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

	private void containDifferentPlayers(List<IPlayer> list1,
			List<IPlayer> list2) {
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
