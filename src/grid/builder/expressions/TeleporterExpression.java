package grid.builder.expressions;

import grid.Coordinate;
import grid.builder.GridBuilder;

@SuppressWarnings("javadoc")
public class TeleporterExpression implements Expression {
	
	private Coordinate	destination;

	public TeleporterExpression(String expression) {
		String[] agruments = expression.split(".");
		int x = Character.getNumericValue(agruments[1].charAt(0));
		int y = Character.getNumericValue(agruments[2].charAt(0));
		
		setDestination(new Coordinate(x, y));
	}

	@Override
	public void build(GridBuilder builder, Coordinate coordinate) {
		builder.placeTeleporter(coordinate, destination);
	}

	/**
	 * @return the destination
	 */
	public Coordinate getDestination() {
		return destination;
	}

	/**
	 * @param destination the destination to set
	 */
	private void setDestination(Coordinate destination) {
		this.destination = destination;
	}
	
}
