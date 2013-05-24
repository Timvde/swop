package grid.builder.expressions;

import grid.Coordinate;
import grid.builder.GridBuilder;


public class ForceFieldGeneratorExpression implements Expression {
	
	@Override
	public void build(GridBuilder builder, Coordinate coordinate) {
		builder.placeForceFieldGenerator(coordinate);
	}
	
}
