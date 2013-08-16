package grid.builder.expressions;

import grid.Coordinate;
import grid.builder.GridBuilder;

@SuppressWarnings("javadoc")
public class IdentityDiskExpression implements Expression {
	
	private boolean	charged;

	public IdentityDiskExpression(boolean charged) {
		this.charged = charged;
		
	}

	@Override
	public void build(GridBuilder builder, Coordinate coordinate) {
		if (charged)
			builder.placeChargedIdentityDisc(coordinate);
		else 
			builder.placeUnchargedIdentityDisc(coordinate);
	}
	
}
