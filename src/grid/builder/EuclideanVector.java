package grid.builder;

import grid.Coordinate;

/**
 * The State class is used in the algorithm to find the possible locations for a
 * charged identity disk. It couples a coordinate to its distance from some kind
 * of origin.
 */
public class EuclideanVector implements Comparable<EuclideanVector> {
	
	private Coordinate	coordinate;
	private int			distance;
	
	/**
	 * Create a State with a specified coordinate and accompanying distance.
	 * 
	 * @param coordinate
	 *        The coordinate of this state
	 * @param distance
	 *        The distance to the origin
	 */
	public EuclideanVector(Coordinate coordinate, int distance) {
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
	public int compareTo(EuclideanVector o) {
		return Integer.valueOf(getDistance()).compareTo(Integer.valueOf(o.getDistance()));
	}
}
