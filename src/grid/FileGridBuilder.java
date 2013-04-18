package grid;

import item.teleporter.Teleporter;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import player.Player;
import square.ASquare;
import square.Wall;

/**
 * A grid builder that will create a grid that is read from a file.
 * 
 * @author Tom
 * 
 */
public class FileGridBuilder extends AGridBuilder {
	
	private String fileName;
	
	private Coordinate	player1StartingPosition;
	private Coordinate	player2StartingPosition;
	
	/**
	 * TODO
	 * 
	 * @param players
	 */
	public FileGridBuilder(List<Player> players) {
		super(players);
	}
	
	/**
	 * Set the file name of the file to read.
	 * 
	 * @param file
	 *        the name of the file the grid is located in.
	 * @return this
	 */
	public FileGridBuilder setFile(String file) {
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
	 * @return a new grid object
	 */
	public Grid build() {
		walls = new ArrayList<Wall>();
		grid = new HashMap<Coordinate, ASquare>();
		teleporterCoords = new HashMap<Teleporter, Coordinate>();
		
		// read the file from the grid and get their starting positions
		readGridFromFile(this.fileName, grid);
		
		// place players on the board and set their starting positions
		List<Coordinate> startingCoordinates;
		
		if (this.player1StartingPosition == null || this.player2StartingPosition == null) {
			System.out.println("Player starting positions were not included in file.");
			startingCoordinates = calculateStartingPositionsOfPlayers();
		}
		else {
			startingCoordinates = new ArrayList<Coordinate>();
			startingCoordinates.add(this.player1StartingPosition);
			startingCoordinates.add(this.player2StartingPosition);
		}
		
		for (int i = 0; i < players.size(); ++i) {
			players.get(i).setStartingPosition(getSquare(startingCoordinates.get(i)));
			getSquare(startingCoordinates.get(i)).addPlayer(players.get(i));
		}
		
		// place the items on the board
		placeItemsOnBoard(startingCoordinates);
		return new Grid(grid);
	}
	
	/**
	 * TODO
	 * 
	 * @param filename
	 */
	private void readGridFromFile(String filename, HashMap<Coordinate, ASquare> grid) {
		
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
						ASquare square = getSquare(coord);
						grid.put(coord, square);
					}
					else if (c == '*') {
						// do nothing, this square doesn't exist
					}
					else if (c == '#') {
						ASquare square = getWallPart(coord);
						grid.put(coord, square);
					}
					else if (c == '1') {
						this.player1StartingPosition = coord;
						ASquare square = getSquare(coord);
						grid.put(coord, square);
					}
					else if (c == '2') {
						this.player2StartingPosition = coord;
						ASquare square = getSquare(coord);
						grid.put(coord, square);
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
