package gui;

import item.IItem;
import item.LightGrenade;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.List;
import java.util.Set;
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
 * @author tom
 */
public class GUI {
	// TODO class constants zoals 40
	// TODO als huidige player op square, get item list in een lijst.
	// TODO get dim van grid en pas GUI aan?
	
	public static void main(String[] args) {
		// All code that accesses the simple GUI must run in the AWT event
		// handling thread.
		// A simple way to achieve this is to run the entire application in the
		// AWT event handling thread.
		// This is done by simply wrapping the body of the main method in a call
		// of EventQueue.invokeLater.
		java.awt.EventQueue.invokeLater(new Runnable() {
			
			AGUI	gui;
			Grid	grid;
			String	status				= "teststatus";
			int		numRows				= 4;
			int		numCols				= 4;
			
			Image	playerRedImage;
			Image	playerBlueImage;
			Image wallImage;
			Image lightGrenadeImage;
			Image lightTrailImage;
			
			// these values decide where the top left position of the grid will
			// be:
			int		topLeftGridOffsetX	= 150;
			int		topLeftGridOffsetY	= 42;
			
			// Non-changing labels
			String	statusLBL			= "Status:";
			
			public void run() {
				
				this.gui = new AGUI("GUI", 200 + (40 * numCols), 200 + (40 * numRows)) {
					
					@Override
					public void paint(Graphics2D graphics) {
						// Deze blok wordt bij elke repaint uitgevoerd:
						
						
						// Draw status string
						graphics.drawString(status, 60, 20);
						graphics.drawString(statusLBL, 10, 20);
						
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
								if (player.getID() == 0) {
									graphics.drawImage(playerBlueImage, guiCoord.getX(),
											guiCoord.getY(), 40, 40, null);
								}
								if (player.getID() == 1) {
									graphics.drawImage(playerRedImage, guiCoord.getX(),
											guiCoord.getY(), 40, 40, null);
								}
							}
							
							// Draw wall if necessary
							if (square instanceof Wall.WallPart) {
								graphics.drawImage(wallImage, guiCoord.getX(),
										guiCoord.getY(), 40, 40, null);
							}
						
							// Draw lighttrail if necessary
							if (square.hasLightTrail()) {
								graphics.drawImage(lightTrailImage, guiCoord.getX(),
										guiCoord.getY(), 40, 40, null);
							}
							
							// Draw items if necessary
							List<IItem> itemList = square.getItemList();
							for (IItem i : itemList) {
								if (i instanceof LightGrenade) {
									graphics.drawImage(lightGrenadeImage, guiCoord.getX(),
											guiCoord.getY(), 40, 40, null);
								}
							}
							
						}
						
						//TODO draw cell finishes
					}
					
				};
				
				// Initialize images
				playerRedImage = gui.loadImage("player_red.png", 40, 40);
				playerBlueImage = gui.loadImage("player_red.png", 40, 40);
				wallImage = gui.loadImage("wall.png", 40, 40);
				lightGrenadeImage = gui.loadImage("lightgrenade.png", 40, 40);
				lightTrailImage = gui.loadImage("lighttrail.png", 40, 40);
				
				
				/* ---- ---- ---- ---- MOVE ARROWS ---- ---- ---- ---- */
				
				// Use the two offsets below to move all arrows at once
				int moveArrowsOffsetX = 10;
				int moveArrowsOffsetY = 40;
				
				Button upButton = gui.createButton(moveArrowsOffsetX + 40, moveArrowsOffsetY + 0,
						40, 40, new Runnable() {
							
							public void run() {
								//TODO up button pressed
								gui.repaint();
							}
						});
				upButton.setImage(gui.loadImage("arrow_N.png", 40, 40));
				// --
				Button leftButton = gui.createButton(moveArrowsOffsetX + 0, moveArrowsOffsetY + 40,
						40, 40, new Runnable() {
							
							public void run() {
								//TODO left button pressed
								gui.repaint();
							}
						});
				leftButton.setImage(gui.loadImage("arrow_W.png", 40, 40));
				// --
				Button rightButton = gui.createButton(moveArrowsOffsetX + 80,
						moveArrowsOffsetY + 40, 40, 40, new Runnable() {
							
							public void run() {
								//TODO right button pressed
								gui.repaint();
							}
						});
				rightButton.setImage(gui.loadImage("arrow_E.png", 40, 40));
				// --
				Button downButton = gui.createButton(moveArrowsOffsetX + 40,
						moveArrowsOffsetY + 80, 40, 40, new Runnable() {
							
							public void run() {
								//TODO down button pressed
								gui.repaint();
							}
						});
				downButton.setImage(gui.loadImage("arrow_S.png", 40, 40));
				
				/* ---- ---- ---- ---- END OF MOVE ARROWS ---- ---- ---- ---- */
				
			}
			
			/**
			 * This method will convert the game Grid coordinate to x and y
			 * coordinates on the GUI frame.
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
			 * Draw a Grid object on the GUI.
			 * 
			 * @param g
			 *        The Grid to draw.
			 */
			public void draw(Grid g) {
				this.grid = g;
				gui.repaint();
			}
		});
		
	}
}
