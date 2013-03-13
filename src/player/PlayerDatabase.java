package player;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * PlayerDatabase keeps track of the players turn in the Game. If a player
 * reaches a the end of its turn the database will be notified and will
 * automatically switch players
 * 
 * @author Bavo Mees
 */
public class PlayerDatabase implements IPlayerDatabase, Observer {
	
	private List<Player>	players;
	private int				currentPlayerIndex;
	
	/**
	 * create a new playerDB with a specified list of players.
	 */
	public PlayerDatabase() {
		this.players = new ArrayList<Player>();
	}
	
	/**
	 * Set the players of this database. Each of these players will be reset to
	 * play new game.
	 * 
	 * @param players
	 *        the new players in this database
	 */
	public void setPlayers(List<Player> players) {
		this.players = new ArrayList<Player>(players);
		for (Player player : players)
			player.reset();
	}
	
	@Override
	public Player getCurrentPlayer() {
		return players.get(currentPlayerIndex);
	}
	
	@Override
	public void createNewDB() {
		// set the current player to the first player
		currentPlayerIndex = 0;
		
		// reset all the players
		for (Player player : players)
			player.reset();
	}
	
	@Override
	public void update(Observable o, Object arg) {
		currentPlayerIndex = ++currentPlayerIndex % players.size();
	}
	
}
