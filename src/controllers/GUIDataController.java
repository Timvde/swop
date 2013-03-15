package controllers;

import item.IItem;
import java.util.List;
import java.util.Set;
import grid.ASquare;
import grid.Coordinate;
import grid.Grid;
import player.IPlayer;
import player.PlayerDataBase;

/**
 * The gui data controller that will fetch data from the game, for creating the GUI visuals.
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
	 * 			The player database the controller will use.
	 * @param grid
	 * 			The grid the controller will use.
	 */
	public GUIDataController(PlayerDataBase playerDB, Grid grid) {
		this.playerDB = playerDB;
		this.grid = grid;
	}
	
	/**
	 * Set the grid of the controller to a new grid.
	 * 
	 * @param g
	 * 			The new grid for this controller
	 */
	public void setGrid(Grid g) {
		this.grid = g;
	}
	
	/**
	 * Return the current player in the game.
	 */
	@SuppressWarnings("javadoc")
	public IPlayer getCurrentPlayer() {
		return this.playerDB.getCurrentPlayer();
	}
	
	/**
	 * Return a list of items in the inventory of the current player.
	 */
	@SuppressWarnings("javadoc")
	public List<IItem> getCurrentPlayerInventoryItems() {
		return getCurrentPlayer().getInventoryContent();
	}
	
	/**
	 * Return a list of items that are located on the square that the current player resides on.
	 */
	@SuppressWarnings("javadoc")
	public List<IItem> getItemsOnSquareOfCurrentPlayer() {
		ASquare currSq = grid.getSquareAt(getCurrentPlayer().getCurrentLocation());
		return currSq.getCarryableItems();
	}
	
	/**
	 * Return the items on a square at the given coordinate of the grid.
	 * 
	 * @param c
	 *        The coordinate of the square we want the items of.
	 */
	@SuppressWarnings("javadoc")
	public List<IItem> getItemList(Coordinate c) {
		return grid.getItemList(c);
	}
	
	/**
	 * Get the square at a certain coordinate on the grid.
	 * 
	 * @param c
	 *        The coordinate of the square.
	 * @return An abstract square object that lies on the given coordinate on
	 *         the grid. TODO wat als coordinate niet in grid??
	 */
	public ASquare getSquareAt(Coordinate c) {
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
