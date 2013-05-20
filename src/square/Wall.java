package square;

import grid.Coordinate;

/**
 * This class exists to enforce the wall restrictions as class invariants. - A
 * wall needs to be straight (i.e. either horizontally or vertically aligned) -
 * A wall needs to consist of at least two squares. A wall cannot intersect with
 * another wall.
 */
public class Wall {
	
	private Coordinate	start;
	private Coordinate	end;
	
	/**
	 * Construct a Wall with the given start and end coordinates
	 * 
	 * @param start
	 *        Start coordinate
	 * @param end
	 *        End coordinate
	 * @throws IllegalArgumentException
	 *         The specified Coordinates are not aligned
	 * @throws IllegalArgumentException
	 *         A wall should should consist of at least two squares
	 */
	public Wall(Coordinate start, Coordinate end) throws IllegalArgumentException {
		if (start.getX() != end.getX() && start.getY() != end.getY())
			throw new IllegalArgumentException("Coordinates not aligned");
		
		if (start.getX() == end.getX() && start.getY() == end.getY())
			throw new IllegalArgumentException("A wall should consist of at least two squares");
		
		/*
		 * The one with the smallest x or y value should be first. This is
		 * consistent, as at least one of them has to be the same.
		 */
		if (start.getX() > end.getX() || start.getY() > end.getY()) {
			this.start = end;
			this.end = start;
		}
		else {
			this.start = start;
			this.end = end;
		}
	}
	
	/**
	 * Get the start coordinate of a wall. This will always be the one closest
	 * to the origin.
	 * @return the starting coordinate
	 */
	public Coordinate getStart() {
		return start;
	}
	
	/**
	 * Get the end coordinate of a wall. This will always be the one furthest
	 * from the origin.
	 * 
	 * @return The end coordinate
	 */
	public Coordinate getEnd() {
		return end;
	}
	
	/**
	 * This method returns whether this wall touches another specified wall
	 * 
	 * @param wall
	 *        Another wall to compare with
	 * @return True when they touch, false when they don't
	 */
	public boolean touchesWall(Wall wall) {
		
		/*
		 * To do this easily, we "spoof" one off the walls to be longer and
		 * thicker, and check whether they intersect.
		 */
		
		// If this is true, they can't touch because they don't have any overlap
		// in the X direction
		if (((this.getStart().getX() - 1) > wall.getEnd().getX())
				|| this.getEnd().getX() + 1 < wall.getStart().getX())
			return false;
		
		// If this is true, they can't touch because they don't have any overlap
		// in the Y direction
		if (((this.getStart().getY() - 1) > wall.getEnd().getY())
				|| this.getEnd().getY() + 1 < wall.getStart().getY())
			return false;
		
		// Otherwise, they have overlap in both directions, and they'll touch
		return true;
	}
}
