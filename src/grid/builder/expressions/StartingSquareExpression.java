package grid.builder.expressions;

import grid.Coordinate;
import grid.builder.GridBuilder;


public class StartingSquareExpression implements Expression {
	
	@Override
	public void build(GridBuilder builder, Coordinate coordinate) {
		builder.addPlayerStartingPosition(coordinate);
	}
	
}
