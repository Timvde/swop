package grid;

import item.teleporter.Teleporter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import player.Player;
import square.ASquare;
import square.Wall;

/**
 * 
 * @author tom
 *
 */
public class FileGridBuilder extends AGridBuilder  {
	
	/**
	 * 
	 * @param players
	 */
	public FileGridBuilder(List<Player> players) {
		super(players);
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
		// TODO
		
		// place the items on the board
		placeItemsOnBoard(startingCoordinates);
		return new Grid(grid);
	}
	
}
