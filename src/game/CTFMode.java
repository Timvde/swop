package game;

import item.Flag;
import item.IItem;
import java.util.HashMap;
import java.util.Map;
import player.Player;
import player.PlayerDataBase;
import player.TurnEvent;
import effects.CTFEffectFactory;
import effects.EffectFactory;

/**
 * In CTFMode the goal for each player is to capture the flag of the other
 * players. A player wins the game when he has captured the flag of each other
 * player at least once, or if all the other players are boxed in, such that
 * they can't move.
 * 
 * In capture the flag mode (CTF), more than 2 players can participate in the
 * game. The maximum number of players is equal to the number of starting
 * locations defined in the grid.
 */
public class CTFMode implements GameMode {
	
	/**
	 * The minimum number of players at the start of CTFMode
	 */
	public static final int			MINIMUM_NUMBER_OF_PLAYERS	= 2;
	
	private int						numberOfPlayers;
	private Map<Player, Integer>	capturedFlagCount;
	
	/**
	 * Creates a new CTFMode with a specified number of players.
	 * 
	 * @param numberOfPlayers
	 *        the number of players participating in this CTFMode.
	 * @throws IllegalArgumentException
	 *         The specified number of players must be
	 *         {@link #isValidNumberOfPlayers(int) valid}.
	 */
	public CTFMode(int numberOfPlayers) {
		if (!isValidNumberOfPlayers(numberOfPlayers))
			throw new IllegalArgumentException("the number of players must be valid");
		
		this.numberOfPlayers = numberOfPlayers;
		this.capturedFlagCount = new HashMap<Player, Integer>();
	}
	
	/**
	 * Returns whether or not a specified number of players is valid. The number
	 * of players must be greater then {@value #MINIMUM_NUMBER_OF_PLAYERS}.
	 * 
	 * @param numberOfPlayers
	 *        the number of players to check.
	 * @return whether the specified number is valid.
	 */
	public boolean isValidNumberOfPlayers(int numberOfPlayers) {
		return numberOfPlayers >= MINIMUM_NUMBER_OF_PLAYERS;
	}
	
	@Override
	public boolean checkCurrentPlayerHasWon(PlayerDataBase playerDB, TurnEvent turnEvent) {
		Player curPlayer = playerDB.getCurrentPlayer();
		
		if (curPlayer.getCurrentPosition().equals(curPlayer.getStartingPosition())) {
			/*
			 * the player moved to his starting location; check if he has any
			 * flags
			 */
			for (IItem item : curPlayer.getInventoryContent())
				if (item instanceof Flag) {
					// teleport back
					item.use(curPlayer.getCurrentPosition(), item.getUseArguments());
					capturedFlagCount.put(curPlayer, capturedFlagCount.get(curPlayer) + 1);
				}
		}
		
		if (capturedFlagCount.get(curPlayer) == playerDB.getNumberOfPlayers()) {
			// the player has captured all flags of all players still alive
			playerDB.clearDataBase();
			return true;
		}
		return false;
	}
	
	@Override
	public boolean checkCurrentPlayerHasLost(PlayerDataBase playerDB, TurnEvent turnEvent) {
		Player curPlayer = playerDB.getCurrentPlayer();
		
		if (turnEvent == TurnEvent.END_TURN && !curPlayer.hasMovedYet()) {
			/*
			 * If a player is boxed in, such that he cannot make a move, the
			 * player dies and is removed from the game and disappears. If he
			 * was carrying a flag, the flag is dropped on the square where the
			 * player was last alive.
			 */
			for (IItem item : curPlayer.getInventoryContent()) {
				if (item instanceof Flag)
					curPlayer.getCurrentPosition().addItem(item);
			}
			playerDB.removeCurrentPlayer();
			return true;
		}
		return false;
	}
	
	@Override
	public int getNumberOfPlayers() {
		return numberOfPlayers;
	}
	
	@Override
	public EffectFactory getEffectFactory() {
		return new CTFEffectFactory();
	}
	
}
