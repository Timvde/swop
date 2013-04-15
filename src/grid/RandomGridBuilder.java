package grid;

import item.teleporter.Teleporter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import player.Player;
import square.ASquare;
import square.ISquare;
import square.Wall;
import square.WallPart;

/**
 * 
 * @author tom
 *
 */
public class RandomGridBuilder extends AGridBuilder {
	
	private static final int	MINIMUM_WALL_SIZE			= 2;
	
	private double				maximalLengthOfWall;
	private double				maximumNumberOfWalls;
	
	/**
	 * 
	 * @param players
	 */
	public RandomGridBuilder(List<Player> players) {
		super(players);
		
		this.maximalLengthOfWall = 0.50;
		this.maximumNumberOfWalls = 0.20;
	}
	
	/**
	 * set the maximal length of a wall as a percentage of the grid's
	 * length/width.
	 * 
	 * @param maximalLength
	 *        the maximal length of a wall
	 * @return this
	 */
	public AGridBuilder setMaximalLengthOfWall(double maximalLength) {
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
	public AGridBuilder setMaximumNumberOfWalls(int maximum) {
		this.maximumNumberOfWalls = maximum;
		return this;
	}
	
	/**
	 * returns the number of walls in the grid
	 * 
	 * @return number of walls
	 */
	private int getNumberOfWallParts() {
		int i = 0;
		for (ISquare square : grid.values())
			if (square.getClass() == WallPart.class)
				i++;
		return i;
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
		while (maxPercentage >= (walls + maxLength++) / ((double) grid.size()));
		
		int maxLength2 = (int) (maximalLengthOfWall * Math.max(height, width));
		
		return Math.min(maxLength, maxLength2);
	}
	
	/**
	 * Build a new grid object. The grid will be build with the parameters set
	 * in this builder. If these parameters were not set, the default values
	 * will be used.
	 * 
	 * @return a new grid object
	 */
	public Grid build() {
		walls = new ArrayList<Wall>();
		grid = new HashMap<Coordinate, ASquare>();
		teleporterCoords = new HashMap<Teleporter, Coordinate>();
		
		// Populate the grid with squares
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++) {
				grid.put(new Coordinate(i, j), getSquare(new Coordinate(i, j)));
			}
		
		// place players on the board and set their starting positions
		List<Coordinate> startingCoordinates = calculateStartingPositionsOfPlayers();
		
		for (int i = 0; i < players.size(); ++i) {
			players.get(i).setStartingPosition(getSquare(startingCoordinates.get(i)));
			getSquare(startingCoordinates.get(i)).addPlayer(players.get(i));
		}
		
		// place walls on the grid
		int max = MINIMUM_WALL_SIZE
				+ (int) (maximumNumberOfWalls * grid.size() - MINIMUM_WALL_SIZE - maximalLengthOfWall
						* Math.max(width, height));
		int maximumNumberOfWalls = new Random().nextInt(max);
		while (maximumNumberOfWalls >= getNumberOfWallParts())
			placeWall(maximumNumberOfWalls, maximalLengthOfWall);
		
		// place the items on the board
		placeItemsOnBoard(startingCoordinates);
		return new Grid(grid);
	}
	
}
