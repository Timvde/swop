package grid.builder;

import grid.Coordinate;
import grid.Grid;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import ObjectronExceptions.builderExceptions.GridBuildException;
import ObjectronExceptions.builderExceptions.InvalidGridFileException;

/**
 * Director that will create a grid specified in a file.
 * 
 */
public class FileGridBuilderDirector extends RandomItemGridBuilderDirector {
	
	private String				fileName;
	private List<Coordinate>	startingCoordinates;
	
	/**
	 * Create a new GridBuilderDirector which will use the specified builder to
	 * build the {@link Grid}
	 * 
	 * @param builder
	 *        The builder this director will use to build the grid.
	 * 
	 * @param filepath
	 *        The path to the file specifying the grid
	 */
	public FileGridBuilderDirector(GridBuilder builder, String filepath) {
		super(builder);
		this.startingCoordinates = new ArrayList<Coordinate>();
		this.setFile(filepath);
	}
	
	/**
	 * Set the file name of the file to read.
	 * 
	 * @param file
	 *        the name of the file the grid is located in.
	 */
	private void setFile(String file) {
		if (file == null)
			throw new IllegalArgumentException("Cannot create grid from file if no file specified!");
		this.fileName = file;
	}
	
	/**
	 * This method reset the created grid. I.e. the builder's datastructure and
	 * the grid-specific variables will be cleared.
	 */
	private void resetCreatedGrid() {
		// clear the builder data structure
		builder.createNewEmptyGrid();
		this.startingCoordinates = new ArrayList<Coordinate>();
	}
	
	@Override
	public void construct() throws InvalidGridFileException {
		resetCreatedGrid();
		
		// read the file from the grid and get their starting positions
		GridDimension gridDim = readGridFromFile();
		
		// place the items on the board
		placeItemsOnBoard(startingCoordinates, gridDim.getWidth(), gridDim.getHeight());
		
		checkGridAdheresRules();
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
		try {
			FileInputStream fis = new FileInputStream(fileName);
			DataInputStream dis = new DataInputStream(fis);
			BufferedReader br = new BufferedReader(new InputStreamReader(dis));
			String line;
			
			int numberOfRows = 0;
			int numberOfColls = 0;
			while ((line = br.readLine()) != null) {
				// new line in file
				for (numberOfColls = 0; numberOfColls < line.length(); numberOfColls++) {
					// new character on line
					char c = line.charAt(numberOfColls);
					Coordinate coord = new Coordinate(numberOfColls, numberOfRows);
					if (c == ' ') {
						builder.addSquare(coord);
					}
					else if (c == '*') {
						// do nothing, this square doesn't exist
					}
					else if (c == '#') {
						builder.addWall(coord);
					}
					else if (c == '1') {
						startingCoordinates.add(coord);
					}
					else if (c == '2') {
						startingCoordinates.add(coord);
					}
					else {
						throw new InvalidGridFileException("Invalid grid-file character: " + c);
					}
				}
				numberOfRows++;
			}
			dis.close();
			return new GridDimension(numberOfColls, numberOfRows);
		}
		catch (IOException e) {
			throw new GridBuildException("IO error while processing file");
		}
	}
	
	/**
	 * Checks whether the constructed grid adheres the correct rules.
	 * 
	 * @throws InvalidGridFileException
	 *         If the grid doesn't adhere the rules.
	 */
	private void checkGridAdheresRules() throws InvalidGridFileException {
		if (this.startingCoordinates.size() != 2) {
			resetCreatedGrid();
			throw new InvalidGridFileException("There must be exactly two starting locations.");
		}
		
		if (gridHasUnreachableIslands()) {
			resetCreatedGrid();
			throw new InvalidGridFileException(
					"There can be no unreachable `islands' of squares that are part of the grid");
		}
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
		Set<Coordinate> squaresReachableFromStart = new HashSet<Coordinate>(
				builder.getAllReachableNeighboursOf(this.startingCoordinates.get(0)));
		for (Coordinate coordinate : squaresReachableFromStart) {
			squaresReachableFromStart.addAll(builder.getAllReachableNeighboursOf(coordinate));
		}
		if (squaresReachableFromStart.size() != builder.getNumberOfSquares()) {
			return true;
		}
		return false;
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
