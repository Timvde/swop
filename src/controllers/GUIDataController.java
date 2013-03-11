package controllers;

import item.IItem;
import java.util.List;
import java.util.Set;
import grid.ASquare;
import grid.Coordinate;
import grid.Grid;
import player.IPlayer;
import player.PlayerDatabase;

public class GUIDataController {
	
	private PlayerDatabase	playerDB;
	private Grid			grid;
	
	public GUIDataController(PlayerDatabase playerDB, Grid grid) {
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
	public IPlayer getCurrentPlayer() {
		return this.playerDB.getCurrentPlayer();
	}
	
	/**
	 * Return a list of items in the inventory of the current player.
	 */
	public List<IItem> getCurrentPlayerInventoryItems() {
		return getCurrentPlayer().getInventory().getItems();
	}
	
	/**
	 * Return a list of items that are located on the square that the current player resides on.
	 */
	public List<IItem> getItemsOnSquareOfCurrentPlayer() {
		ASquare currSq = this.grid.getSquareAt(this.grid.getPlayerCoordinate(getCurrentPlayer()));
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
	public int getGridWidth() {
		return this.grid.getWidth();
	}
	
	/**
	 * Return the height of the grid.
	 */
	public int getGridHeight() {
		return this.grid.getHeight();
	}
	
}
