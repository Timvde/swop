package player.actions;

import player.Player;
import player.TronPlayer;
import square.Square;
import square.SquareContainer;

/**
 * Action is an interaction between a {@link Player} and a {@link Square}.
 * actions can be executed by players with the
 * {@link #execute(TronPlayer, SquareContainer)} method.
 */
public interface Action {
	
	/**
	 * Let a specified player execute this action on a specified square.
	 * 
	 * @param player
	 *        The player who will perform the action
	 * @param square
	 *        The square on which the action is performed
	 */
	void execute(TronPlayer player, SquareContainer square);
}
