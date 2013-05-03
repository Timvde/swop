package controllers;

import grid.Coordinate;
import grid.Grid;
import item.IItem;
import java.util.List;
import java.util.Set;
import player.IPlayer;
import player.PlayerDataBase;
import square.ISquare;
import square.Square;

/**
 * The gui data controller that will fetch data from the game, for creating the
 * GUI visuals.
 * 
 * @author tom
 * 
 */
public class GUIDataController {
	
	private PlayerDataBase	playerDB;
	private Grid			grid;
	
	/**
	 * Create a new gui data controller.
	 * 
	 * @param playerDB
	 *        The player database the controller will use.
	 * @param grid
	 *        The grid the controller will use.
	 */
	public GUIDataController(PlayerDataBase playerDB, Grid grid) {
		if (playerDB == null || grid == null)
			throw new IllegalArgumentException("the specified argument cannot be null");
		this.playerDB = playerDB;
		this.grid = grid;
	}
	
	/**
	 * Check if the gui data controller is configured with a grid.
	 * 
	 * @return True if this data controller is linked to a grid.
	 */
	public boolean hasGrid() {
		if (this.grid != null)
			return true;
		
		return false;
	}
	
	/**
	 * Set the grid of the controller to a new grid.
	 * 
	 * @param g
	 *        The new grid for this controller
	 */
	public void setGrid(Grid g) {
		this.grid = g;
	}
	
	/**
	 * Return the current player in the game.
	 */
	public IPlayer getCurrentPlayer() {
		return this.playerDB.getCurrentPlayer();
	}
	
	/**
	 * Return a list of items in the inventory of the current player.
	 */
	public List<IItem> getCurrentPlayerInventoryItems() {
		return getCurrentPlayer().getInventoryContent();
	}
	
	/**
	 * Return a list of items that are located on the square that the current
	 * player resides on.
	 */
	public List<IItem> getItemsOnSquareOfCurrentPlayer() {
		ISquare currSq = getCurrentPlayer().getCurrentLocation();
		return currSq.getCarryableItems();
	}
	
	/**
	 * Return the items on a square at the given coordinate of the grid.
	 * 
	 * @param c
	 *        The coordinate of the square we want the items of.
	 */
	public List<IItem> getItemList(Coordinate c) {
		return grid.getItemList(c);
	}
	
	/**
	 * Get the square at a certain coordinate on the grid.
	 * 
	 * @param c
	 *        The coordinate of the square.
	 * @return An abstract square object that lies on the given coordinate on
	 *         the grid.
	 */
	public ISquare getSquareAt(Coordinate c) {
		return grid.getSquareAt(c);
	}
	
	/**
	 * Return all the coordinates of the current grid.
	 * 
	 * @return A set with all the coordinates of the grid.
	 */
	public Set<Coordinate> getAllGridCoordinates() {
		return grid.getAllGridCoordinates();
	}
	
	/**
	 * Check if the given square is a starting position of a player.
	 * 
	 * @param s
	 *        The square to test.
	 * @return True if the given square is a player starting position.
	 */
	public boolean isPlayerStartingPosition(ISquare s) {
		for (Square sq : grid.getAllStartingPositions()) {
			if (sq.equals(s))
				return true;
		}
		return false;
	}
	
	/**
	 * Return the width of the grid.
	 */
	@SuppressWarnings("javadoc")
	public int getGridWidth() {
		return this.grid.getWidth();
	}
	
	/**
	 * Return the height of the grid.
	 */
	@SuppressWarnings("javadoc")
	public int getGridHeight() {
		return this.grid.getHeight();
	}
	
}
