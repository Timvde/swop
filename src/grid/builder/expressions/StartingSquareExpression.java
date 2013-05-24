package grid.builder.expressions;

import grid.Coordinate;
import grid.builder.GridBuilder;

/**
 * starting square expression
 * 
 */
public class StartingSquareExpression implements Expression {
	
	private int	id;
	
	/**
	 * create a new starting square expression
	 * 
	 * @param id
	 *        the id of the starting position
	 */
	public StartingSquareExpression(int id) {
		this.id = id;
	}
	
	@Override
	public void build(GridBuilder builder, Coordinate coordinate) {
		builder.addPlayerStartingPosition(coordinate);
	}
	
	/**
	 * returns the id of the starting square
	 * 
	 * @return id
	 */
	public int getId() {
		return id;
	}
	
}
