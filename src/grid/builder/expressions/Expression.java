package grid.builder.expressions;

import grid.Coordinate;
import grid.builder.GridBuilder;

@SuppressWarnings("javadoc")
public interface Expression {
	
	/**
	 * Build the entity this expression represents with a specified builder
	 * 
	 * @param builder
	 *        the builder used to build this expression
	 * @param coordinate
	 *        the coordinate on wicht the square will be build
	 */
	void build(GridBuilder builder, Coordinate coordinate);
}
