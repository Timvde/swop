package grid;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;
import grid.Wall.WallPart;
import item.IItem;

public class Grid implements IGrid {
	
	private HashMap<Coordinate, ASquare>	grid;
	private List<Wall>						walls;
	private int								width;
	private int								height;
	
	/**
	 * Create a new grid with a specified builder.
	 * 
	 * @param builder
	 */
	private Grid(Builder builder) {
		this.width = builder.width;
		this.height = builder.height;
		walls = new ArrayList<Wall>();
		grid = new HashMap<Coordinate, ASquare>();
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++)
				grid.put(new Coordinate(i, j), new Square());
		while (builder.maximumNumberOfWalls > ((double) getNumberOfWallParts()) / grid.size())
			placeWall(builder.maximumNumberOfWalls);
	}
	
	/**
	 * place a new wall on the grid. This method will automatically determine
	 * the maximum length of the wall.
	 * 
	 * @param maxPercentage
	 *        The maximum percentage of walls on the grid
	 */
	private void placeWall(double maxPercentage) {
		int wallLength = new Random().nextInt(getMaximumLengthOfWall(maxPercentage));
		// a wall must have a length of two
		if (wallLength < 2)
			return;
		
		Coordinate start, end;
		
		do {
			start = Coordinate.random(width, height);
			end = start.getRandomCoordinateWithDistance(wallLength);
		} while (!canPlaceWall(start, end));
		// place the wall on the grid
		placeWallOnGrid(start, end);
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
			grid.put(coord, wall.getWallPart());
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
	private int getMaximumLengthOfWall(double maxPercentage) {
		int maxLength = 0;
		int walls = getNumberOfWallParts();
		// increase maxLength until a maximum value is reached
		while (maxPercentage > (walls + maxLength++) / size());
		return maxLength;
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
	 */
	private boolean canPlaceWall(Coordinate start, Coordinate end) {
		return false;
	}
	
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
			// the positions are not alligned...
			throw new IllegalArgumentException("The given positions " + start + ", " + end
					+ " are not alligned!");
		return positions;
	}
	
	/**
	 * returns the number of squares in this grid. If the grid contains more
	 * than Integer.MAX_VALUE elements, returns Integer.MAX_VALUE.
	 * 
	 * @return the number of squares
	 */
	public int size() {
		return grid.size();
	}
	
	private int getNumberOfWallParts() {
		int i = 0;
		for (ASquare square : grid.values())
			if (square.getClass() == WallPart.class)
				i++;
		return i;
	}
	
	@Override
	public boolean canMovePlayer(Coordinate coordinate, Direction direction) {
		return false;
	}
	
	@Override
	public void movePlayer(Coordinate coordinate, Direction direction) {
		if (!canMovePlayer(coordinate, direction))
			throw new IllegalArgumentException("Player can not be moved in that direction!");
		
	}
	
	@Override
	public List<IItem> getItemList(Coordinate coordinate) {
		return grid.get(coordinate).getCarryableItems();
	}
	
	@Override
	public ASquare getSquareAt(Coordinate coordinate) {
		return grid.get(coordinate);
	}
	
	@Override
	public Set<Coordinate> getAllGridCoordinates() {
		return this.grid.keySet();
	}
	
	public static class Builder {
		
		private int		minimalLengthOfWall;
		private double	maximalLengthOfWall;
		private double	maximumNumberOfWalls;
		private int		width;
		private int		height;
		
		public Builder() {
			this.minimalLengthOfWall = 2;
			this.maximalLengthOfWall = 0.50;
			this.maximumNumberOfWalls = 0.20;
			this.width = 10;
			this.height = 10;
		}
		
		/**
		 * Set the minimum length of a wall in the grid. The specified length
		 * should be greater then or equal to 2.
		 * 
		 * @param minimalLength
		 *        the minimum length of a wall
		 * @return this
		 */
		// @Requires("minimalLengthOfWall >= 2")
		public Builder setMinimalLengthOfWall(int minimalLength) {
			this.minimalLengthOfWall = minimalLength;
			return this;
		}
		
		/**
		 * set the maximal length of a wall as a percentage of the grid's
		 * length/width.
		 * 
		 * @param maximalLength
		 *        the maximal length of a wall
		 * @return this
		 */
		public Builder setMaximalLengthOfWall(double maximalLength) {
			this.maximalLengthOfWall = maximalLength;
			return this;
		}
		
		/**
		 * set the maximum number of walls in the grid
		 * 
		 * @param maximum
		 *        the maximum number of walls
		 * @return this
		 */
		public Builder setMaximumNumberOfWalls(int maximum) {
			this.maximumNumberOfWalls = maximum;
			return this;
		}
		
		/**
		 * set the width of the grid. This must be a strictly positive integer
		 * 
		 * @param width
		 *        the width of the grid
		 * @return this
		 */
		// @Requires("width > 0")
		public Builder setGridWidth(int width) {
			this.width = width;
			return this;
		}
		
		/**
		 * set the height of the grid. This must be a strictly positive integer
		 * 
		 * @param height
		 *        the height of the grid
		 * @return this
		 */
		// @Requires("height > 0")
		public Builder setGridHeigth(int height) {
			this.height = height;
			return this;
		}
		
		public Grid build() {
			return new Grid(this);
		}
	}
}
