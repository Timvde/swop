package grid.builder.expressions;

import grid.Coordinate;
import grid.builder.GridBuilder;

@SuppressWarnings("javadoc")
public class CombinedExpression implements Expression {

	private Expression	expression;
	private Expression	itemExpression;

	public CombinedExpression(Expression expression, Expression itemExpression) {
		this.expression = expression;
		this.itemExpression = itemExpression;
		
	}

	@Override
	public void build(GridBuilder builder, Coordinate coordinate) {
		expression.build(builder, coordinate);
		itemExpression.build(builder, coordinate);
	}
	
	
	
}
