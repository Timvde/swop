package gui;

import item.IItem;
import item.LightGrenade;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.List;
import java.util.Set;
import controllers.EndTurnController;
import controllers.MoveController;
import controllers.PickUpItemController;
import controllers.UseItemController;
import player.IPlayer;
import grid.ASquare;
import grid.Coordinate;
import grid.Grid;
import grid.Wall;
import gui.Button;
import gui.AGUI;

/**
 * This class is the GUI for interacting with the game.
 * 
 * For correct threading, the GUI must be started like this:
 * java.awt.EventQueue.invokeLater(new GUI(moveCont, pickupCont, endTurnCont,
 * useItemCont));
 * 
 * @author Tom
 */
public class GUI implements Runnable {
	
	// TODO class constants zoals 40
	// TODO als huidige player op square, get item list in een lijst.
	
	private AGUI					gui;
	private Grid					grid;
	private int						numRows				= 4;
	private int						numCols				= 4;
	
	/**
	 * Controllers for interacting with the game engine.
	 */
	private MoveController			moveController;
	private PickUpItemController	pickUpController;
	private EndTurnController		endTurnController;
	private UseItemController		useItemController;
	
	/**
	 * Image objects for displaying on the GUI.
	 */
	private Image					playerRedImage;
	private Image					playerBlueImage;
	private Image					wallImage;
	private Image					lightGrenadeImage;
	private Image					lightTrailImage;
	private Image					finishBlue;
	private Image					finishRed;
	
	/**
	 * Offsets that determine where the top left position of the grid will be.
	 */
	private int						topLeftGridOffsetX	= 150;
	private int						topLeftGridOffsetY	= 42;
	
	/**
	 * Construct a new GUI and initialize the controllers.
	 * 
	 * @param moveCont
	 * 				The move controller.
	 * @param pickupCont
	 * 				The pickup item controller.
	 * @param endturnCont
	 * 				The end of turn controller.
	 * @param useitemCont
	 * 				The use item controller.
	 */
	public GUI(MoveController moveCont, PickUpItemController pickupCont,
			EndTurnController endturnCont, UseItemController useitemCont) {
		
		this.moveController = moveCont;
		this.pickUpController = pickupCont;
		this.endTurnController = endturnCont;
		this.useItemController = useitemCont;
		
	}
	
	/**
	 * Start the GUI thread by creating the GUI elements and displaying them.
	 */
	public void run() {
		
		this.gui = new AGUI("GUI", 200 + (40 * numCols), 200 + (40 * numRows)) {
			
			@Override
			public void paint(Graphics2D graphics) {
				// This block is executed with each repaint():
				
				// Adjust the grid dimensions and GUI size:
				numRows = getGridNumRows(grid);
				numCols = getGridNumCols(grid);
				gui.getPanel().setSize(200 + (40 * numCols), 200 + (40 * numRows));
				
				// Draw grid lines
				for (int r = 0; r < numRows; r++) {
					for (int c = 0; c < numCols; c++) {
						graphics.drawRect(topLeftGridOffsetX + (42 * c), topLeftGridOffsetY
								+ (42 * r), 42, 42);
					}
				}
				
				// Populate the grid squares with the correct images:
				Set<Coordinate> gridCoords = grid.getAllGridCoordinates();
				
				for (Coordinate c : gridCoords) {
					ASquare square = grid.getSquareAt(c);
					IPlayer player = square.getPlayer();
					Coordinate guiCoord = toGUICoord(c);
					
					// Draw players if necessary
					if (player != null) {
						switch (player.getID()) {
							case 0:
								graphics.drawImage(playerBlueImage, guiCoord.getX(),
										guiCoord.getY(), 40, 40, null);
								break;
							case 1:
								graphics.drawImage(playerRedImage, guiCoord.getX(),
										guiCoord.getY(), 40, 40, null);
								break;
							default:
								break;
						}
					}
					
					// Draw wall if necessary
					if (square instanceof Wall.WallPart) {
						graphics.drawImage(wallImage, guiCoord.getX(), guiCoord.getY(), 40, 40,
								null);
					}
					
					// Draw lighttrail if necessary
					if (square.hasLightTrail()) {
						graphics.drawImage(lightTrailImage, guiCoord.getX(), guiCoord.getY(), 40,
								40, null);
					}
					
					// Draw items if necessary
					List<IItem> itemList = square.getItemList();
					for (IItem i : itemList) {
						if (i instanceof LightGrenade) {
							graphics.drawImage(lightGrenadeImage, guiCoord.getX(), guiCoord.getY(),
									40, 40, null);
						}
					}
					
				}
				
				// Draw the two finish squares:
				Coordinate guiCoordFinishRed = toGUICoord(new Coordinate(0, numRows));
				Coordinate guiCoordFinishBlue = toGUICoord(new Coordinate(numCols, 0));
				graphics.drawImage(finishBlue, guiCoordFinishBlue.getX(),
						guiCoordFinishBlue.getY(), 40, 40, null);
				graphics.drawImage(finishRed, guiCoordFinishRed.getX(), guiCoordFinishRed.getY(),
						40, 40, null);
				
			}
			
		};
		
		// Initialize images
		this.playerRedImage = gui.loadImage("player_red.png", 40, 40);
		this.playerBlueImage = gui.loadImage("player_blue.png", 40, 40);
		this.wallImage = gui.loadImage("wall.png", 40, 40);
		this.lightGrenadeImage = gui.loadImage("lightgrenade.png", 40, 40);
		this.lightTrailImage = gui.loadImage("cell_lighttrail.png", 40, 40);
		this.finishBlue = gui.loadImage("cell_finish_blue.png", 40, 40);
		this.finishRed = gui.loadImage("cell_finish_red.png", 40, 40);
		
		/* ---- ---- ---- ---- MOVE ARROWS ---- ---- ---- ---- */
		
		// Use the two offsets below to move all arrows at once
		int moveArrowsOffsetX = 10;
		int moveArrowsOffsetY = 40;
		
		Button upButton = gui.createButton(moveArrowsOffsetX + 40, moveArrowsOffsetY + 0, 40, 40,
				new Runnable() {
					
					public void run() {
						// TODO up button pressed
						gui.repaint();
					}
				});
		upButton.setImage(gui.loadImage("arrow_N.png", 40, 40));
		// --
		Button leftButton = gui.createButton(moveArrowsOffsetX + 0, moveArrowsOffsetY + 40, 40, 40,
				new Runnable() {
					
					public void run() {
						// TODO left button pressed
						gui.repaint();
					}
				});
		leftButton.setImage(gui.loadImage("arrow_W.png", 40, 40));
		// --
		Button rightButton = gui.createButton(moveArrowsOffsetX + 80, moveArrowsOffsetY + 40, 40,
				40, new Runnable() {
					
					public void run() {
						// TODO right button pressed
						gui.repaint();
					}
				});
		rightButton.setImage(gui.loadImage("arrow_E.png", 40, 40));
		// --
		Button downButton = gui.createButton(moveArrowsOffsetX + 40, moveArrowsOffsetY + 80, 40,
				40, new Runnable() {
					
					public void run() {
						// TODO down button pressed
						gui.repaint();
					}
				});
		downButton.setImage(gui.loadImage("arrow_S.png", 40, 40));
		
		/* ---- ---- ---- ---- END OF MOVE ARROWS ---- ---- ---- ---- */
		
	}
	
	/**
	 * Draw a whole Grid object on the GUI.
	 * 
	 * @param g
	 *        The Grid to draw.
	 */
	public void draw(Grid g) {
		this.grid = g;
		gui.repaint();
	}
	
	/**
	 * This method will convert the game Grid coordinate to x and y coordinates
	 * on the GUI frame.
	 * 
	 * @param c
	 *        The grid coordinate
	 * @return A new Coordinate object that has the GUI coordinates.
	 */
	private Coordinate toGUICoord(Coordinate c) {
		int x = (c.getX() * 40) + topLeftGridOffsetX;
		int y = (c.getY() * 40) + topLeftGridOffsetY;
		
		return new Coordinate(x, y);
	}
	
	/**
	 * This method will return the number of rows in a grid.
	 * 
	 * @param g
	 *        The grid.
	 * @return The number of rows in the grid.
	 */
	private int getGridNumRows(Grid g) {
		Set<Coordinate> gridCoords = grid.getAllGridCoordinates();
		
		int maxRowNum = 0;
		for (Coordinate c : gridCoords) {
			if (c.getY() > maxRowNum)
				maxRowNum = c.getY();
		}
		
		return maxRowNum;
	}
	
	/**
	 * This method will return the number of columns in a grid.
	 * 
	 * @param g
	 *        The grid.
	 * @return The number of columns in the grid.
	 */
	private int getGridNumCols(Grid g) {
		Set<Coordinate> gridCoords = grid.getAllGridCoordinates();
		
		int maxColNum = 0;
		for (Coordinate c : gridCoords) {
			if (c.getX() > maxColNum)
				maxColNum = c.getY();
		}
		
		return maxColNum;
	}
}
