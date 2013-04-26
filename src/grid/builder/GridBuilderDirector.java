package grid.builder;

import grid.Grid;

/**
 * A director manages the creation of a {@link Grid grid}. It uses a
 * {@link GridBuilder builder} for the actual creating of objects.
 * 
 */
public abstract class GridBuilderDirector {
	
	protected GridBuilder	builder;
	
	/**
	 * Create a new GridBuilderDirector which will use the specified builder to
	 * build the {@link Grid}
	 * 
	 * @param builder
	 *        The builder this director will use to build the grid.
	 */
	public GridBuilderDirector(GridBuilder builder) {
		this.builder = builder;
	}
	
	/**
	 * Construct the grid. After calling this method the {@link GridBuilder
	 * builder} specified in the constructor will return the corresponding grid.
	 */
	public abstract void construct();
}
