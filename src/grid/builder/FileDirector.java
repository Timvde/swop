package grid.builder;

import grid.Coordinate;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * A grid builder that will create a grid that is read from a file.
 * 
 * @author Tom
 * 
 */
public class FileDirector extends RandomItemGridBuilderDirector {
	
	private String		fileName;
	
	private Coordinate	player1StartingPosition;
	private Coordinate	player2StartingPosition;
	
	/**
	 * TODO
	 * 
	 * @param builder
	 *        ...
	 */
	public FileDirector(GridBuilder builder) {
		super(builder);
	}
	
	/**
	 * Set the file name of the file to read.
	 * 
	 * @param file
	 *        the name of the file the grid is located in.
	 * @return this
	 */
	public FileDirector setFile(String file) {
		if (file == null)
			throw new IllegalArgumentException("Cannot create grid from file if no file specified!");
		this.fileName = file;
		return this;
	}
	
	/**
	 * Build a new grid object that is read from a file. The grid will be build
	 * with the parameters set in this builder. If these parameters were not
	 * set, the default values will be used.
	 * 
	 */
	public void construct() {
		
		// read the file from the grid and get their starting positions
		readGridFromFile(this.fileName);
		
		// place players on the board and set their starting positions
		List<Coordinate> startingCoordinates;
		
		if (this.player1StartingPosition == null || this.player2StartingPosition == null) {
			System.out.println("Player starting positions were not included in file.");
			// FIXME the following can be done when we read the file!
			startingCoordinates = new ArrayList<Coordinate>();
			startingCoordinates.add(this.player1StartingPosition);
			startingCoordinates.add(this.player2StartingPosition);
		}
		else {
			// FIXME the following can be done when we read the file!
			startingCoordinates = new ArrayList<Coordinate>();
			startingCoordinates.add(this.player1StartingPosition);
			startingCoordinates.add(this.player2StartingPosition);
		}
		// place the items on the board
		placeItemsOnBoard(startingCoordinates);
	}
	
	/**
	 * TODO
	 * 
	 * @param filename
	 */
	private void readGridFromFile(String filename) {
		
		try {
			FileInputStream fis = new FileInputStream(filename);
			DataInputStream dis = new DataInputStream(fis);
			BufferedReader br = new BufferedReader(new InputStreamReader(dis));
			String line;
			
			int row = 0;
			while ((line = br.readLine()) != null) {
				// new line in file
				for (int i = 0; i < line.length(); i++) {
					// new character on line
					char c = line.charAt(i);
					Coordinate coord = new Coordinate(i, row);
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
						this.player1StartingPosition = coord;
						builder.addSquare(coord);
					}
					else if (c == '2') {
						this.player2StartingPosition = coord;
						builder.addSquare(coord);
					}
				}
				row++;
			}
			
			dis.close();
		}
		catch (Exception e) {
			System.err.println("File Read Error: " + e.getMessage());
		}
	}
}
