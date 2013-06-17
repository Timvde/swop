package controllers;

import grid.Coordinate;
import grid.Grid;
import grid.GuiGrid;
import grid.GuiSquare;
import item.IItem;
import java.util.List;
import java.util.Set;
import player.Player;
import player.PlayerDataBase;
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
	private GuiGrid	grid;
	
	/**
	 * Create a new gui data controller.
	 * 
	 * @param playerDB
	 *        The player database the controller will use.
	 * @param grid
	 *        The grid the controller will use. This may be <code>null</code>,
	 *        but then one has to call the {@link #setGrid(Grid)} method later.
	 */
	public GUIDataController(PlayerDataBase playerDB, GuiGrid grid) {
		if (playerDB == null)
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
	 * @param grid
	 *        The new grid for this controller
	 */
	public void setGrid(GuiGrid grid) {
		this.grid = grid;
	}
	
	/**
	 * Return the current player in the game.
	 * 
	 * @return returns the current player of the game
	 */
	public Player getCurrentPlayer() {
		return this.playerDB.getCurrentPlayer();
	}
	
	/**
	 * Return a list of items in the inventory of the current player.
	 * 
	 * @return inventory of the player
	 */
	public List<IItem> getCurrentPlayerInventoryItems() {
		return getCurrentPlayer().getInventoryContent();
	}
	
	/**
	 * Return a list of items that are located on the square that the current
	 * player resides on.
	 * 
	 * @return list of items
	 */
	public List<IItem> getItemsOnSquareOfCurrentPlayer() {
		Square currSq = getCurrentPlayer().getCurrentPosition();
		return currSq.getCarryableItems();
	}
	
	/**
	 * Return the items on a square at the specified coordinate of the grid.
	 * 
	 * @param coordinate
	 *        The coordinate of the square we want the items of.
	 * @return items of a specified coordinate
	 */
	public List<IItem> getItemList(Coordinate coordinate) {
		return grid.getSquareAt(coordinate).getItems();
	}
	
	/**
	 * Get the square at a certain coordinate on the grid.
	 * 
	 * @param coordinate
	 *        The coordinate of the square.
	 * @return An abstract square object that lies on the given coordinate on
	 *         the grid.
	 */
	public GuiSquare getSquareAt(Coordinate coordinate) {
		return grid.getSquareAt(coordinate);
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
