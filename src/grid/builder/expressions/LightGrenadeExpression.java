package grid.builder.expressions;

import grid.Coordinate;
import grid.builder.GridBuilder;

@SuppressWarnings("javadoc")
public class LightGrenadeExpression implements Expression {
	
	@Override
	public void build(GridBuilder builder, Coordinate coordinate) {
		builder.placeLightGrenade(coordinate);
	}
	
}
