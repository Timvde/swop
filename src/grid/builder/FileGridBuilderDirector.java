package grid.builder;

import grid.Coordinate;
import grid.Grid;
import grid.builder.expressions.Expression;
import grid.builder.expressions.StartingSquareExpression;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import ObjectronExceptions.builderExceptions.InvalidGridFileException;

/**
 * Director that will create a grid specified in a file. For the placement of
 * items it will call the
 * {@link RandomItemGridBuilderDirector#placeItemsOnBoard(Map, int, int)
 * supertype method}.
 */
@SuppressWarnings("javadoc")
public class FileGridBuilderDirector extends RandomItemGridBuilderDirector {
	
	private Map<Coordinate, Expression>	grid;
	private Map<Integer, Coordinate>	startingCoordinates;
	private TronFileParser				parser;
	private File						file;
	
	/**
	 * Create a new GridBuilderDirector which will use the specified builder to
	 * build the {@link Grid}
	 * 
	 * @param builder
	 *        The builder this director will use to build the grid.
	 * 
	 * @param filepath
	 *        The path to the file specifying the grid
	 * @throws FileNotFoundException
	 */
	public FileGridBuilderDirector(GridBuilder builder, String filepath)
			throws FileNotFoundException {
		super(builder);
		startingCoordinates = new HashMap<Integer, Coordinate>();
		grid = new HashMap<Coordinate, Expression>();
		File file = new File(filepath);
		parser = new TronFileParser(file);
		this.file = file;
	}
	
	/**
	 * Construct the grid specified in the file given in the constructor. This
	 * method will also {@link RandomItemGridBuilderDirector} <i>randomly</i>
	 * (i.e. as specified by the Tron game constraints) place items on the grid.
	 * 
	 * @throws InvalidGridFileException
	 *         The grid file must adhere the correct rules and it cannot contain
	 *         invalid characters.
	 */
	@Override
	public void construct() throws InvalidGridFileException {
		try {
			reset();
		}
		catch (FileNotFoundException e1) {
			throw new InvalidGridFileException("file not found");
		}
		
		GridDimension gridDim = readGridFromFile();
		placeItemsOnBoard(startingCoordinates, gridDim.getWidth(), gridDim.getHeight());
		
		if (!isValidGrid()) {
			try {
				reset();
			}
			catch (FileNotFoundException e) {
				// Do nothing because we're already throwing an exception
			}
			throw new InvalidGridFileException("The specified grid was not valid");
		}
	}
	
	private void reset() throws FileNotFoundException {
		builder.createNewEmptyGrid();
		grid = new HashMap<Coordinate, Expression>();
		parser = new TronFileParser(file);
		startingCoordinates = new HashMap<Integer, Coordinate>();
	}
	
	private boolean isValidGrid() {
		if (gridHasUnreachableIslands())
			return false;
		else if (hasDoubleStartingPositions())
			return false;
		else if (hasSingleOrNoStartingPosition())
			return false;
		else
			return true;
	}
	
	private boolean hasSingleOrNoStartingPosition() {
		return (startingCoordinates.size() <= 1);
	}
	
	private boolean hasDoubleStartingPositions() {
		Set<Integer> values = new HashSet<Integer>();
		for (Coordinate startingCoordinate : startingCoordinates.values()) {
			if (values.contains(((StartingSquareExpression) grid.get(startingCoordinate)).getId()))
				return true;
			values.add(((StartingSquareExpression) grid.get(startingCoordinate)).getId());
		}
		return false;
		
	}
	
	/**
	 * This method will construct the grid as specified in the gridFile.
	 * 
	 * @return The dimensions of the read grid.
	 * 
	 * @throws InvalidGridFileException
	 *         When the gridfile contains an invalid character.
	 */
	private GridDimension readGridFromFile() throws InvalidGridFileException {
		int i = 0, j = 0;
		while (parser.hasNextValidStatement()) {
			j = 0;
			while (!parser.isAtEndOfLine()) {
				if (!parser.hasNextValidStatement()) {
					throw new InvalidGridFileException("invalid grid symbol");
				}
				Expression expression = parser.nextExpression();
				if (expression != null) {
					expression.build(builder, new Coordinate(j, i));
					grid.put(new Coordinate(j, i), expression);
				}
				if (expression instanceof StartingSquareExpression) {
					int id = ((StartingSquareExpression) expression).getId();
					startingCoordinates.put(id, new Coordinate(j, i));
				}
				j++;
				
			}
			parser.readEndOfLine();
			i++;
		}
		return new GridDimension(i, j);
	}
	
	/**
	 * There must be a path from each free square that is part of the grid to
	 * each other free square that is part of the grid. That is, there can be no
	 * unreachable `islands' of squares that are part of the grid.
	 * 
	 * The grid having no unreachable islands also means there is a path between
	 * the two starting locations.
	 * 
	 * @return Whether or not the creates grid has unreachable islands.
	 */
	private boolean gridHasUnreachableIslands() {
		Set<Coordinate> squaresReachableFromStart = new HashSet<Coordinate>();
		List<Coordinate> coordinatesToVisit = builder
				.getAllReachableNeighboursOf(this.startingCoordinates.get(1));
		
		while (!coordinatesToVisit.isEmpty()) {
			Coordinate curCoordinate = coordinatesToVisit.remove(0);
			List<Coordinate> coordsToAdd = builder.getAllReachableNeighboursOf(curCoordinate);
			for (Coordinate coordinate : coordsToAdd)
				if (squaresReachableFromStart.add(coordinate)) {
					coordinatesToVisit.add(coordinate);
				}
		}
		
		return (squaresReachableFromStart.size() != builder.getNumberOfSquares());
	}
	
	/**
	 * A class to represent the dimensions of a 2 dimensional grid.
	 */
	private class GridDimension {
		
		private int	width;
		private int	height;
		
		public GridDimension(int width, int height) {
			this.width = width;
			this.height = height;
		}
		
		public int getWidth() {
			return width;
		}
		
		public int getHeight() {
			return height;
		}
	}
}
