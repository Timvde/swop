package player;

import grid.Coordinate;
import grid.Square;
import item.LightGrenade;

import java.util.ArrayList;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import com.sun.istack.internal.NotNull;

/**
 * A class to store PlayerDataBase.NUMBER_OF_PLAYERS {@link Player}s and to
 * appoint the current player allowed to play. The {@link PlayerDataBase} will
 * observe his players. A Player notifies the database to indicate he wants to
 * end his turn.
 * 
 */
public class PlayerDataBase implements Observer, IPlayerDataBase {

	public static final int NUMBER_OF_PLAYERS = 2;

	@NotNull
	private ArrayList<Player> playerList = new ArrayList<Player>();

	private int currentPlayerIndex; // index in playerList

	/**
	 * Creates a new PlayerManager-object and calls the
	 * <code>createNewDB()</code> method to fill it initially with new
	 * {@link Player}s.
	 */
	public PlayerDataBase(Map<Player, Coordinate> playerCoordinateMap) {
		this.createNewDB();
	}

	/**
	 * This method first clears the current database and then re-fills the
	 * database with PlayerDataBase.NUMBER_OF_PLAYERS newly created
	 * {@link Player} which it will observe.
	 * 
	 * The player allowed to play, is the player first created .
	 */
	public void createNewDB() {
		this.playerList.clear();
		for (int i = 0; i < NUMBER_OF_PLAYERS; i++) {
			Player newPlayer = new Player();
			this.playerList.add(newPlayer);
			newPlayer.addObserver(this);
		}
		this.currentPlayerIndex = 0;
	}

	/**
	 * Returns the player who is currently allowed to play.
	 * 
	 * @return the player who is currently allowed to play.
	 */
	public Player getCurrentPlayer() {
		return this.playerList.get(this.currentPlayerIndex);
	}

	/**
	 * This method is called whenever an observed object (i.e. a {@link Player})
	 * notifies the database. A Player notifies the database to indicate he
	 * wants to end his turn. This happens if <li>he has no actions left</li>
	 * <li>he enters a {@link Square} with no power left and no active
	 * {@link LightGrenade}</li> <li>if the user expl * A {@link PlayerDataBase}
	 * stores PlayerDataBase.NUMBER_OF_PLAYERS {@link Player}s and appoints the
	 * current player allowed to play. The {@link PlayerDataBase} will observe
	 * his players. A Player notifies the database to indicate he wants to end
	 * his turn. icitly asks to end the player's turn.</li> <br>
	 * 
	 * The database will only switch players if the player asking to end his
	 * turn is the current player ( <code>this.getCurrentPlayer</code>).
	 * 
	 */
	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof Player) {
			Player player = (Player) o;

			/*
			 * If a player for example enters a square with no power left and no
			 * active lightgrenade, he will ask the database to switch players.
			 * The player then decreases the number of actions done, this may
			 * become zero and thus the player will ask the database again to
			 * switch players. The database will not perform the second switch
			 * because it will see the player asking to end his turn is not the
			 * current player (which is switched in the first demand).
			 */
			if (player.equals(this.getCurrentPlayer())) {
				this.endCurrentPlayerTurn();
				player.increaseAllowedNumberOfActions();
			}
		}
	}

	/**
	 * Ends the turn of the current player (this.getCurrentPlayer()) and
	 * appoints a next current player (circular shift).
	 */
	private void endCurrentPlayerTurn() {
		if (this.currentPlayerIndex < this.playerList.size())
			this.currentPlayerIndex++;
		else
			this.currentPlayerIndex = 0;
	}

}