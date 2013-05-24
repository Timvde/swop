package grid.builder.expressions;

import grid.builder.GridBuilder;
import grid.Coordinate;


public class SquareExpression implements Expression {
	
	@Override
	public void build(GridBuilder builder, Coordinate coordinate) {
		builder.addSquare(coordinate);		
	}
}
