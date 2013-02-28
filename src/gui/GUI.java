package gui;

import game.Game;
import grid.ASquare;
import grid.Coordinate;
import grid.Grid;
import grid.Wall;
import item.Item;
import item.LightGrenade;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import player.IPlayer;
import player.PlayerManager;
import controllers.EndTurnController;
import controllers.MoveController;
import controllers.PickUpItemController;
import controllers.UseItemController;

/**
 * This class is the GUI for interacting with the game.
 * 
 * For correct threading, the GUI must be started like this:
 * java.awt.EventQueue.invokeLater(new GUI(Game game, PlayerManager
 * playerManager, MoveController moveCont, PickUpItemController pickupCont,
 * EndTurnController endturnCont, UseItemController useitemCont));
 * 
 * @author Tom
 */
public class GUI implements Runnable {
	
	private AGUI					gui;
	private Grid					grid;
	private Game					game;
	// TODO playermanager niet gebruiken maar pickupitem controller
	private PlayerManager			playerManager;
	
	/**
	 * The following values are not final and will be updated with each redraw,
	 * depending on the Grid object dimension. These are just start values.
	 */
	private int						numRows				= 8;
	private int						numCols				= 8;
	
	/**
	 * This variable is the size of a square on the GUI. If modified, the grid
	 * and all images will automaticly be resized.
	 */
	private final static int		SQUARE_SIZE			= 40;
	
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
	 * This is the list of items that the current player can interact with.
	 */
	private gui.List				itemList;
	private Object					itemListSelected;
	private gui.List				inventoryList;
	private Object					inventoryListSelected;
	
	/**
	 * Offsets that determine where the top left position of the grid will be.
	 */
	private int						topLeftGridOffsetX	= 150;
	private int						topLeftGridOffsetY	= 42;
	
	/**
	 * Construct a new GUI and initialize the controllers.
	 * 
	 * @param moveCont
	 *        The move controller.
	 * @param pickupCont
	 *        The pickup item controller.
	 * @param endturnCont
	 *        The end of turn controller.
	 * @param useitemCont
	 *        The use item controller.
	 */
	public GUI(Game game, PlayerManager playerManager, MoveController moveCont,
			PickUpItemController pickupCont, EndTurnController endturnCont,
			UseItemController useitemCont) {
		this.game = game;
		this.playerManager = playerManager;
		this.moveController = moveCont;
		this.pickUpController = pickupCont;
		this.endTurnController = endturnCont;
		this.useItemController = useitemCont;
		
	}
	
	/**
	 * Start the GUI thread by creating the GUI elements and displaying them.
	 */
	public void run() {
		
		this.gui = new AGUI("GUI", 200 + (SQUARE_SIZE * numCols), 200 + (SQUARE_SIZE * numRows)) {
			
			@Override
			public void paint(Graphics2D graphics) {
				// This block is executed with each repaint():
				
				
				
				// Adjust the grid dimensions and GUI size:
				// numRows = getGridNumRows(grid);
				// numCols = getGridNumCols(grid);
				// gui.getPanel()
				// .setSize(200 + (SQUARE_SIZE * numCols), 200 + (SQUARE_SIZE *
				// numRows));
				
				// Draw grid lines
				for (int r = 0; r < numRows; r++) {
					for (int c = 0; c < numCols; c++) {
						graphics.drawRect(topLeftGridOffsetX + ((SQUARE_SIZE + 2) * c),
								topLeftGridOffsetY + ((SQUARE_SIZE + 2) * r), (SQUARE_SIZE + 2),
								(SQUARE_SIZE + 2));
					}
				}
				
				// // Populate the grid squares with the correct images:
				// Set<Coordinate> gridCoords = grid.getAllGridCoordinates();
				//
				// for (Coordinate c : gridCoords) {
				// ASquare square = grid.getSquareAt(c);
				// IPlayer player = square.getPlayer();
				// Coordinate guiCoord = toGUICoord(c);
				//
				// // Draw players if necessary
				// if (player != null) {
				// switch (player.getID()) {
				// case 0:
				// graphics.drawImage(playerBlueImage, guiCoord.getX(),
				// guiCoord.getY(), SQUARE_SIZE, SQUARE_SIZE, null);
				// break;
				// case 1:
				// graphics.drawImage(playerRedImage, guiCoord.getX(),
				// guiCoord.getY(), SQUARE_SIZE, SQUARE_SIZE, null);
				// break;
				// default:
				// break;
				// }
				// }
				//
				// // Draw wall if necessary
				// if (square instanceof Wall.WallPart) {
				// graphics.drawImage(wallImage, guiCoord.getX(),
				// guiCoord.getY(),
				// SQUARE_SIZE, SQUARE_SIZE, null);
				// }
				//
				// // Draw lighttrail if necessary
				// if (square.hasLightTrail()) {
				// graphics.drawImage(lightTrailImage, guiCoord.getX(),
				// guiCoord.getY(),
				// SQUARE_SIZE, SQUARE_SIZE, null);
				// }
				//
				// // Draw items if necessary
				// List<IItem> itemList = square.getCarryableItems();
				// for (Item i : itemList) {
				// if (i instanceof LightGrenade) {
				// graphics.drawImage(lightGrenadeImage, guiCoord.getX(),
				// guiCoord.getY(),
				// SQUARE_SIZE, SQUARE_SIZE, null);
				// }
				// }
				//
				// }
				//
				// // Draw the two finish squares:
				// Coordinate guiCoordFinishRed = toGUICoord(new Coordinate(0,
				// numRows));
				// Coordinate guiCoordFinishBlue = toGUICoord(new
				// Coordinate(numCols, 0));
				// graphics.drawImage(finishBlue, guiCoordFinishBlue.getX(),
				// guiCoordFinishBlue.getY(), SQUARE_SIZE, SQUARE_SIZE, null);
				// graphics.drawImage(finishRed, guiCoordFinishRed.getX(),
				// guiCoordFinishRed.getY(),
				// SQUARE_SIZE, SQUARE_SIZE, null);
				
				// Draw the list of items that are on the current square.
				// Coordinate currentPlayerPosition =
				// playerManager.getCurrentPlayerCoordinate();
				// ASquare playerSquare =
				// grid.getSquareAt(currentPlayerPosition);
				//
				// List<Item> items = playerSquare.getCarryableItems();
				
				Vector<Item> items = new Vector<Item>();
				
				// for (Item i : items) {
				// items.add(i);
				// }
				Item i = new LightGrenade();
				items.add(i);
				
				itemList.setListData(items);
				
				
			}
			
		};
		
		// Initialize images
		this.playerRedImage = gui.loadImage("player_red.png", SQUARE_SIZE, SQUARE_SIZE);
		this.playerBlueImage = gui.loadImage("player_blue.png", SQUARE_SIZE, SQUARE_SIZE);
		this.wallImage = gui.loadImage("wall.png", SQUARE_SIZE, SQUARE_SIZE);
		this.lightGrenadeImage = gui.loadImage("lightgrenade.png", SQUARE_SIZE, SQUARE_SIZE);
		this.lightTrailImage = gui.loadImage("cell_lighttrail.png", SQUARE_SIZE, SQUARE_SIZE);
		this.finishBlue = gui.loadImage("cell_finish_blue.png", SQUARE_SIZE, SQUARE_SIZE);
		this.finishRed = gui.loadImage("cell_finish_red.png", SQUARE_SIZE, SQUARE_SIZE);
		
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
		upButton.setImage(gui.loadImage("arrow_N.png", SQUARE_SIZE, SQUARE_SIZE));
		// --
		Button leftButton = gui.createButton(moveArrowsOffsetX + 0, moveArrowsOffsetY + 40, 40, 40,
				new Runnable() {
					
					public void run() {
						// TODO left button pressed
						gui.repaint();
					}
				});
		leftButton.setImage(gui.loadImage("arrow_W.png", SQUARE_SIZE, SQUARE_SIZE));
		// ---
		Button rightButton = gui.createButton(moveArrowsOffsetX + 80, moveArrowsOffsetY + 40, 40,
				40, new Runnable() {
					
					public void run() {
						// TODO right button pressed
						gui.repaint();
					}
				});
		rightButton.setImage(gui.loadImage("arrow_E.png", SQUARE_SIZE, SQUARE_SIZE));
		// --
		Button downButton = gui.createButton(moveArrowsOffsetX + 40, moveArrowsOffsetY + 80, 40,
				40, new Runnable() {
					
					public void run() {
						// TODO down button pressed
						gui.repaint();
					}
				});
		downButton.setImage(gui.loadImage("arrow_S.png", SQUARE_SIZE, SQUARE_SIZE));
		// --
		Button NEButton = gui.createButton(moveArrowsOffsetX + 80, moveArrowsOffsetY, 40, 40,
				new Runnable() {
					
					public void run() {
						// TODO NE button pressed
						gui.repaint();
					}
				});
		NEButton.setImage(gui.loadImage("arrow_NE.png", SQUARE_SIZE, SQUARE_SIZE));
		// --
		Button SEButton = gui.createButton(moveArrowsOffsetX + 80, moveArrowsOffsetY + 80, 40, 40,
				new Runnable() {
					
					public void run() {
						// TODO SE button pressed
						gui.repaint();
					}
				});
		SEButton.setImage(gui.loadImage("arrow_SE.png", SQUARE_SIZE, SQUARE_SIZE));
		// --
		Button SWButton = gui.createButton(moveArrowsOffsetX, moveArrowsOffsetY + 80, 40, 40,
				new Runnable() {
					
					public void run() {
						// TODO SW button pressed
						gui.repaint();
					}
				});
		SWButton.setImage(gui.loadImage("arrow_SW.png", SQUARE_SIZE, SQUARE_SIZE));
		// --
		Button NWButton = gui.createButton(moveArrowsOffsetX, moveArrowsOffsetY, 40, 40,
				new Runnable() {
					
					public void run() {
						// TODO NW button pressed
						gui.repaint();
					}
				});
		NWButton.setImage(gui.loadImage("arrow_NW.png", SQUARE_SIZE, SQUARE_SIZE));
		
		/* ---- ---- ---- ---- END OF MOVE ARROWS ---- ---- ---- ---- */
		
		/* ---- ---- ---- ---- ACTION BUTTONS ---- ---- ---- ---- */
		// Use these offsets to move all the action buttons at once:
		int actionButtonsOffsetX = 10;
		int actionButtonsOffsetY = 180;
		
		Button pickItemUpButton = gui.createButton(actionButtonsOffsetX, actionButtonsOffsetY, 120,
				30, new Runnable() {
					
					public void run() {
						// TODO button pick up pressed
						// Gebruik instance var itemListSelected. Dat is het
						// item object dat selected is
						System.out.println(itemListSelected);
						gui.repaint();
					}
				});
		pickItemUpButton.setText("Pick up item");
		// ----
		Button dropMineButton = gui.createButton(actionButtonsOffsetX, actionButtonsOffsetY + 40,
				120, 30, new Runnable() {
					
					public void run() {
						// TODO drop mine button pressed
						gui.repaint();
					}
				});
		dropMineButton.setText("Drop mine");
		/* ---- ---- ---- ----END OF ACTION BUTTONS---- ---- ---- ---- */
		
		/* ---- ---- ---- ----        LISTS        ---- ---- ---- ---- */
		itemList = gui.createList(10, 280, 120, 100, new Runnable() {
			
			public void run() {
				// each time the selection in the list is changed, the
				// itemListSelected instance variable will update.
				Object selectedValue = itemList.getSelectedValue();

				// only update if it's a select operation, and not a deselect
				if (selectedValue != null) {
					itemListSelected = itemList.getSelectedValue();
				}
			}
		});
		// ---
		inventoryList = gui.createList(10, 410, 120, 100, new Runnable() {
			
			public void run() {
				// each time the selection in the list is changed, the
				// inventoryListSelected instance variable will update.
				
			}
		});
		/* ---- ---- ---- ----    END OF LISTS     ---- ---- ---- ---- */
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
