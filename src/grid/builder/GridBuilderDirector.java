package grid.builder;

import ObjectronExceptions.builderExceptions.GridBuildException;
import grid.Grid;

/**
 * A director manages the creation of a {@link Grid grid}. It uses the
 * {@link GridBuilder builderinterface} for the actual creating of the domain
 * concepts.
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
		if (builder == null) {
			throw new IllegalArgumentException("The specified builder cannot be null");
		}
		this.builder = builder;
	}
	
	/**
	 * Construct the grid. After calling this method the {@link GridBuilder
	 * builder} specified in the constructor will have build the corresponding
	 * grid.
	 * 
	 * @throws GridBuildException
	 *         When the grid cannot be constructed.
	 */
	public abstract void construct() throws GridBuildException;
}
