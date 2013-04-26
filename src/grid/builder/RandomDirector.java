package grid.builder;

import grid.Coordinate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import square.Wall;

/**
 * A grid builder for generating random grids.
 * 
 * @author Tom
 * 
 */
public class RandomDirector extends RandomItemGridBuilderDirector {
	
	private static final int	MINIMUM_WALL_SIZE	= 2;
	
	private double				maximalLengthOfWall;
	private double				maximumNumberOfWalls;
	private int					height;
	private int					width;
	
	private Collection<Wall>	walls;
	
	/**
	 * @param builder
	 * 
	 * 
	 */
	public RandomDirector(GridBuilder builder) {
		super(builder);
		
		this.maximalLengthOfWall = 0.50;
		this.maximumNumberOfWalls = 0.20;
		this.height = 12;
		this.width = 12;
	}
	
	/**
	 * set the maximal length of a wall as a percentage of the grid's
	 * length/width.
	 * 
	 * @param maximalLength
	 *        the maximal length of a wall
	 */
	public void setMaximalLengthOfWall(double maximalLength) {
		this.maximalLengthOfWall = maximalLength;
	}
	
	/**
	 * set the maximum number of walls in the grid
	 * 
	 * @param maximum
	 *        the maximum number of walls
	 */
	public void setMaximumNumberOfWalls(int maximum) {
		this.maximumNumberOfWalls = maximum;
	}
	
	/**
	 * set the width of the grid
	 * 
	 * @param width
	 *        ...
	 */
	public void setWidth(int width) {
		this.width = width;
	}
	
	/**
	 * set the height of the grid
	 * 
	 * @param height
	 *        ...
	 */
	public void setHeight(int height) {
		this.height = height;
	}
	
	/**
	 * returns the number of walls in the grid
	 * 
	 * @return number of walls
	 */
	private int getNumberOfWallParts() {
		int i = 0;
		for (Wall wall : walls) {
			i += getWallPositions(wall.getStart(), wall.getEnd()).size();
		}
		return i;
	}
	
	/**
	 * This method will place a wall on the grid with a specified start and end
	 * position. If the wall cannot be placed on the board this method will
	 * throw an {@link IllegalArgumentException}
	 * 
	 * @param start
	 *        the start position of the grid
	 * @param end
	 *        the end position of the grid
	 * @throws IllegalArgumentException
	 *         if the wall cannot be placed on the board
	 */
	private void placeWallOnGrid(Coordinate start, Coordinate end) throws IllegalArgumentException {
		if (!canPlaceWall(start, end))
			throw new IllegalArgumentException("the wall cannot be placed on the board");
		Wall wall = new Wall(start, end);
		for (Coordinate coord : getWallPositions(start, end))
			builder.addWall(coord);
		walls.add(wall);
	}
	
	/**
	 * returns whether a wall, specified by its start and end position, can be
	 * placed on the board.
	 * 
	 * @param start
	 *        the start position of the wall
	 * @param end
	 *        the end position of the wall
	 * @return true if a wall can be placed, else false
	 * 
	 */
	private boolean canPlaceWall(Coordinate start, Coordinate end) {
		// walls must be placed on the board
		if (!isCoordinateInGrid(start) || !isCoordinateInGrid(end))
			return false;
		// walls cannot be placed on start positions
		if (isStartingPosition(start) || isStartingPosition(end))
			return false;
		// walls cannot touch other walls on the board
		for (Wall w : walls)
			if (w.touchesWall(new Wall(start, end)))
				return false;
		return true;
	}
	
	/**
	 * Returns whether the specified coordinate is part of the grid
	 * 
	 * @param coordinate
	 *        the coordinate to test
	 * @return true if the coordinate is part of the grid
	 */
	private boolean isCoordinateInGrid(Coordinate coordinate) {
		if (coordinate.getX() >= width || coordinate.getX() < 0)
			return false;
		else if (coordinate.getY() >= height || coordinate.getY() < 0)
			return false;
		else
			return true;
	}
	
	/**
	 * Returns whether the specified coordinate is a starting position
	 * 
	 * @param coordinate
	 *        the coordinate to test
	 * @return true if the specified coordinate is a starting position
	 */
	private boolean isStartingPosition(Coordinate coordinate) {
		return getStartingPositions().contains(coordinate);
	}
	
	/**
	 * Returns a list of the starting positions 
	 * @return the starting positions for this grid
	 */
	private List<Coordinate> getStartingPositions() {
		List<Coordinate> positions = new ArrayList<Coordinate>();
		positions.add(new Coordinate(0, height));
		positions.add(new Coordinate(width, 0));
		
		return positions;
	}
	
	/**
	 * Return all the coordinates of a wall that starts and ends at two certain
	 * points.
	 * 
	 * @param start
	 *        The start position of the wall.
	 * @param end
	 *        The end position of the wall.
	 * @return A collection of coordinates of this wall.
	 * @throws IllegalArgumentException
	 *         If the given positions are not aligned.
	 */
	private Collection<Coordinate> getWallPositions(Coordinate start, Coordinate end) {
		Collection<Coordinate> positions = new ArrayList<Coordinate>();
		
		// start adding the coordinates
		if (start.getX() == end.getX() && start.getY() < end.getY())
			for (int i = start.getY(); i <= end.getY(); i++)
				positions.add(new Coordinate(start.getX(), i));
		else if (start.getX() == end.getX() && start.getY() > end.getY())
			for (int i = start.getY(); i >= end.getY(); i--)
				positions.add(new Coordinate(start.getX(), i));
		else if (start.getY() == end.getY() && start.getX() < end.getX())
			for (int i = start.getX(); i <= end.getX(); i++)
				positions.add(new Coordinate(i, start.getY()));
		else if (start.getY() == end.getY() && start.getX() > end.getX())
			for (int i = start.getX(); i >= end.getX(); i--)
				positions.add(new Coordinate(i, start.getY()));
		else
			// the positions are not aligned...
			throw new IllegalArgumentException("The given positions " + start + ", " + end
					+ " are not aligned!");
		return positions;
	}
	
	/**
	 * place a new wall on the grid. This method will automatically determine
	 * the maximum length of the wall.
	 * 
	 * @param maxPercentage
	 *        The maximum percentage of walls on the grid
	 */
	private void placeWall(double maxPercentage, double maximalLengthOfWall) {
		// generate random number between a minimum and a maximum
		int max = getMaximumLengthOfWall(maxPercentage, maximalLengthOfWall);
		int wallLength = new Random().nextInt(max - MINIMUM_WALL_SIZE + 1) + MINIMUM_WALL_SIZE;
		
		Coordinate start, end;
		do {
			start = Coordinate.random(width, height);
			end = start.getRandomCoordinateWithDistance(wallLength);
			// Logically, we should do -1 here, but wallLength is a random
			// excluding the wallLength itself, so it implicitly already
			// happened.
		} while (!canPlaceWall(start, end));
		// place the wall on the grid
		placeWallOnGrid(start, end);
	}
	
	/**
	 * Returns the maximum length for new wall that is to be placed on the
	 * board. this method takes into account the maximum percentage of walls on
	 * the board. This number will be rounded up.
	 * 
	 * @param maxPercentage
	 *        the maximum percentage of walls on the board
	 * @return the maximum length of a new wall
	 */
	private int getMaximumLengthOfWall(double maxPercentage, double maximalLengthOfWall) {
		int maxLength = 0;
		int walls = getNumberOfWallParts();
		// increase maxLength until a maximum value is reached
		while (maxPercentage >= (walls + maxLength++) / ((double) width * height));
		
		int maxLength2 = (int) (maximalLengthOfWall * Math.max(height, width));
		
		return Math.min(maxLength, maxLength2);
	}
	
	public void construct() {
		walls = new ArrayList<Wall>();
		
		// Populate the grid with squares
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++)
				builder.addSquare(new Coordinate(i, j));
		
		// place walls on the grid
		int max = MINIMUM_WALL_SIZE
				+ (int) (maximumNumberOfWalls * width * height - MINIMUM_WALL_SIZE - maximalLengthOfWall
						* Math.max(width, height));
		int maximumNumberOfWalls = new Random().nextInt(max);
		while (maximumNumberOfWalls >= getNumberOfWallParts())
			placeWall(maximumNumberOfWalls, maximalLengthOfWall);
		
		// place the items on the board
		placeItemsOnBoard(getStartingPositions());
	}
	

	
}
