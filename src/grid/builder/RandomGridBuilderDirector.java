package grid.builder;

import grid.Coordinate;
import grid.Grid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import square.Wall;

/**
 * This GridBuilder will construct a <i>random</i> grid as specified by the Tron
 * game constraints. For the placement of items it will call the
 * {@link RandomItemGridBuilderDirector#placeItemsOnBoard(Map, int, int)
 * supertype method}.
 */
@SuppressWarnings("javadoc")
public class RandomGridBuilderDirector extends RandomItemGridBuilderDirector {
	
	/**
	 * The number of candidate player starting positions on a automatically
	 * generated grid.
	 */
	public static final int		NUMBER_OF_PLAYER_STARTS				= 4;
	/** The minimum (default) width of the grid. */
	public static final int		MINIMUM_GRID_WIDTH				= 10;
	/** The minimum (default) height of the grid. */
	public static final int		MINIMUM_GRID_HEIGHT				= 10;
	/** the minimal number of squares in a wall */
	static final int			MINIMUM_WALL_LENGHT				= 2;
	/** the maximal length of a wall as a percentage of the grid's length/width. */
	static final double			MAXIMUM_WALL_LENGHT_PERCENTAGE	= 0.50;
	/** the maximal number of walls as a percentage of the nb of sq on the grid */
	static final double			MAXIMUM_WALL_NUMBER_PERCENTAGE	= 0.20;
	
	private int					height;
	private int					width;
	
	private Collection<Wall>	walls;
	private int					numberOfWallPartsToPlace;
	
	/**
	 * Create a new GridBuilderDirector which will use the specified builder to
	 * build the {@link Grid}. The default dimensions of the grid to construct
	 * are {@value #MINIMUM_GRID_WIDTH} x {@value #MINIMUM_GRID_HEIGHT}.
	 * 
	 * @param builder
	 *        The builder this director will use to build the grid.
	 */
	public RandomGridBuilderDirector(GridBuilder builder) {
		super(builder);
		
		this.height = MINIMUM_GRID_HEIGHT;
		this.width = MINIMUM_GRID_WIDTH;
		this.resetCreatedGrid();
	}
	
	/**
	 * Set the width of the grid.
	 * 
	 * @param width
	 *        ...
	 */
	public void setWidth(int width) {
		if (width < MINIMUM_GRID_WIDTH) {
			throw new IllegalArgumentException("width is smaller then minimum width");
		}
		this.width = width;
	}
	
	/**
	 * Set the height of the grid.
	 * 
	 * @param height
	 *        ...
	 */
	public void setHeight(int height) {
		if (height < MINIMUM_GRID_HEIGHT) {
			throw new IllegalArgumentException("height is smaller then minimum height");
		}
		this.height = height;
	}
	
	/**
	 * This method reset the created grid. I.e. the builder's datastructure and
	 * the grid-specific variables will be cleared.
	 */
	private void resetCreatedGrid() {
		builder.createNewEmptyGrid();
		this.walls = new ArrayList<Wall>();
		this.numberOfWallPartsToPlace = 0;
	}
	
	@Override
	public void construct() {
		resetCreatedGrid();
		
		// Populate the grid with squares
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++)
				builder.addSquare(new Coordinate(i, j));
		
		// place walls on the grid
		int maxNumberOfWallParts = (int) Math.ceil(width * height * MAXIMUM_WALL_NUMBER_PERCENTAGE);
		this.numberOfWallPartsToPlace = new Random().nextInt(maxNumberOfWallParts
				- MINIMUM_WALL_LENGHT + 1)
				+ MINIMUM_WALL_LENGHT;
		
		while ((numberOfWallPartsToPlace - getNumberOfWallParts()) >= MINIMUM_WALL_LENGHT)
			placeNewWall();
		
		// place the items on the board
		placeItemsOnBoard(getStartingPositions(), width, height);
	}
	
	/**
	 * returns the number of walls in the grid
	 * 
	 * @return number of walls
	 */
	private int getNumberOfWallParts() {
		int result = 0;
		for (Wall wall : walls) {
			result += getWallPositions(wall.getStart(), wall.getEnd()).size();
		}
		return result;
	}
	
	/**
	 * Place a new wall on the grid. This method will automatically determine
	 * the maximum length of the wall.
	 */
	private void placeNewWall() {
		// generate random number between a minimum and a maximum
		int max = getMaximumLengthOfWall();
		int wallLength = new Random().nextInt(max - MINIMUM_WALL_LENGHT + 1) + MINIMUM_WALL_LENGHT;
		
		Coordinate start, end;
		do {
			start = Coordinate.random(width, height);
			end = start.getRandomCoordinateWithDistance(wallLength - 1);
			// We should do -1 here, because an end square placed (n)
			// squares from the start square produces a wall of length
			// (n+1)
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
		return getStartingPositions().values().contains(coordinate);
	}
	
	/**
	 * Returns a list of the player starting positions.
	 * 
	 * @return the player starting positions for this grid.
	 */
	private Map<Integer, Coordinate> getStartingPositions() {
		/*
		 * The starting positions are hardcoded at this moment, we can change
		 * this here if needed at some point
		 * 
		 * Player 1 starts on the bottom left corner of the grid, player 2
		 * starts on the top right corner, player 3 starts in the top left
		 * corner, and player 4 starts in the bottom right corner.
		 */
		Map<Integer, Coordinate> result = new HashMap<Integer, Coordinate>();
		
		result.put(1, new Coordinate(0, height - 1));
		result.put(2, new Coordinate(width - 1, 0));
		result.put(3, new Coordinate(0, 0));
		result.put(4, new Coordinate(width - 1, height - 1));
		
		return result;
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
	 * Returns the maximum length for new wall that is to be placed on the
	 * board. This method takes into account the maximum percentage of walls on
	 * the board. This number will be rounded up.
	 */
	private int getMaximumLengthOfWall() {
		int maxLength = 0;
		int walls = getNumberOfWallParts();
		// increase maxLength until a maximum value is reached
		while ((walls + maxLength++) / ((double) width * height) <= MAXIMUM_WALL_NUMBER_PERCENTAGE);
		
		int maxLength2 = (int) (MAXIMUM_WALL_LENGHT_PERCENTAGE * Math.max(height, width));
		
		int maxLenght3 = this.numberOfWallPartsToPlace - getNumberOfWallParts();
		
		return Math.min(Math.min(maxLength, maxLength2), maxLenght3);
	}
}
