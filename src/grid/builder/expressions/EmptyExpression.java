package grid.builder.expressions;

import grid.Coordinate;
import grid.builder.GridBuilder;

@SuppressWarnings("javadoc")
public class EmptyExpression implements Expression {
	
	@Override
	public void build(GridBuilder builder, Coordinate coordinate) {
		/* an empty square does nothing */
	}
	
}
