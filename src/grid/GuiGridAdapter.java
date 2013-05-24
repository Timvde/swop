package grid;

import java.util.Set;

/**
 * An adapter to make grid more accessible.
 * 
 */
public class GuiGridAdapter implements GuiGrid {
	
	private Grid	grid;
	
	/**
	 * Create a new gui grid adapter
	 * 
	 * @param grid
	 *        the grid to be adapted
	 */
	public GuiGridAdapter(Grid grid) {
		if (!isValidGrid(grid))
			throw new IllegalArgumentException();
		this.grid = grid;
	}
	
	private boolean isValidGrid(Grid grid) {
		if (grid == null)
			return false;
		else 
			return true;
	}
	
	@Override
	public GuiSquare getSquareAt(Coordinate coordinate) throws IllegalArgumentException {
		return new GuiSquareAdapter(grid.getSquareAt(coordinate));
	}
	
	@Override
	public Set<Coordinate> getAllGridCoordinates() throws IllegalArgumentException {
		return grid.getAllGridCoordinates();
	}
	
	public int getWidth() {
		return grid.getWidth();
	}
	
	public int getHeight() {
		return grid.getHeight();
	}
	
}
