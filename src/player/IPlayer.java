package player;

import grid.Coordinate;
import grid.Direction;
import item.IItem;

/**
 * A Player is the main actor of the game. It can move round, pick up objects
 * and tries to win the game
 */
public interface IPlayer {
	
	/**
	 * TODO deze hier in interface of enkel in Player zelf? De vraag is,
	 * werkt een player database met IPlayer of gewoon Player? Als het
	 * gewoon Player heeft mag deze uit de interface..
	 */
	public void IncreaseActionCounter();
	
	/**
	 * TODO
	 * @param i
	 */
	public void useItem(IItem i);
	
	/**
	 * TODO
	 * @param i
	 */
	public void pickUpItem(IItem i);
	
	/**
	 * TODO
	 * @param d
	 */
	public void move(Direction d);
	
	/**
	 * TODO
	 */
	public void decreaseNumberOfActions();
	
	/**
	 * TODO
	 * @return
	 */
	public int getNumberOfActionsLeft();
	
	/**
	 * TODO
	 * @param n
	 */
	public void skipNumberOfActions(int n);
	
	/**
	 *TODO
	 */
	public void endTurn();
	
	/**
	 * Returns the unique ID-number associated with this player.
	 * 
	 * @return the unique ID-number associated with this player.
	 */
	public int getID();
	
	/**
	 * Returns the coordinate this player has to reach to win the game.
	 * 
	 * @return the coordinate this player has to reach to win the game.
	 */
	public Coordinate getTargetPosition();
	
	/**
	 * Returns the Inventory associated with this player.
	 * 
	 * @return the Inventory associated with this player.
	 */
	public Inventory getInventory();
	
}
