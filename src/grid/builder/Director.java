package grid.builder;

import grid.Grid;

/**
 * A director manages the creation of the {@link Grid grid}. It uses 
 * a {@link GridBuilder builder} for the actual creating of objects.  
 *
 */
public abstract class Director {
	
	protected GridBuilder builder;
	
	public Director(GridBuilder builder) {
		this.builder = builder;
	}
	
	/**
	 * Construct the grid
	 */
	public abstract void construct();
}
