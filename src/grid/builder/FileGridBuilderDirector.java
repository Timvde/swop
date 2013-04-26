package grid.builder;

import grid.Coordinate;
import grid.Grid;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * A grid builder that will create a grid specified in a file.
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
	 * @return this
	 */
	private FileGridBuilderDirector setFile(String file) {
		if (file == null)
			throw new IllegalArgumentException("Cannot create grid from file if no file specified!");
		this.fileName = file;
		return this;
	}
	
	@Override
	public void construct() {
		builder.createNewGrid();
		
		// read the file from the grid and get their starting positions
		GridDimension gridDim = readGridFromFile(this.fileName);
		
		// place the items on the board
		placeItemsOnBoard(startingCoordinates, gridDim.getWidth(), gridDim.getHeight());
	}
	
	private GridDimension readGridFromFile(String filename) {
		try {
			FileInputStream fis = new FileInputStream(filename);
			DataInputStream dis = new DataInputStream(fis);
			BufferedReader br = new BufferedReader(new InputStreamReader(dis));
			String line;
			
			int height = 0;
			int width = 0;
			while ((line = br.readLine()) != null) {
				// new line in file
				for (width = 0; width < line.length(); width++) {
					// new character on line
					char c = line.charAt(width);
					Coordinate coord = new Coordinate(width, height);
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
						builder.addSquare(coord);
						builder.placePlayer(coord);
					}
					else if (c == '2') {
						startingCoordinates.add(coord);
						builder.addSquare(coord);
						builder.placePlayer(coord);
					}
				}
				height++;
			}
			
			dis.close();
			
			return new GridDimension(width, height);
		}
		catch (Exception e) {
			System.err.println("File Read Error: " + e.getMessage());
			return null;
		}
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
