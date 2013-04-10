package grid;


public class State implements Comparable<State> {
	private Coordinate coordinate;
	private int distance;
	
	public State (Coordinate coordinate, int distance) {
		this.coordinate = coordinate;
		this.distance = distance;
	}

	/**
	 * @return the coordinate
	 */
	public Coordinate getCoordinate() {
		return coordinate;
	}

	/**
	 * @return the distance
	 */
	public int getDistance() {
		return distance;
	}

	@Override
	public int compareTo(State o) {
		return Integer.compare(getDistance(), o.getDistance());
	}
}
