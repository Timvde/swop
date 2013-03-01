package gui;

import grid.ASquare;
import grid.Coordinate;
import grid.Grid;
import grid.Wall;
import item.IItem;
import item.Item;
import item.LightGrenade;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import player.IPlayer;
import controllers.GetInventoryListController;
import controllers.GetItemListController;
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
	
	private AGUI						gui;
	private Grid						grid;
	
	/**
	 * The following values are not final and will be updated with each redraw,
	 * depending on the Grid object dimension. These are just temp start values.
	 */
	private int							gridHeight			= 4;
	private int							gridWidth			= 4;
	
	/**
	 * This variable is the size of a square on the GUI. If modified, the grid
	 * and all images will automaticly be resized.
	 */
	private final static int			SQUARE_SIZE			= 40;
	
	/**
	 * Controllers for interacting with the game engine.
	 */
	private MoveController				moveController;
	private PickUpItemController		pickUpController;
	private UseItemController			useItemController;
	private GetItemListController		getItemListController;
	private GetInventoryListController	getInventoryListController;
	
	/**
	 * Image objects for displaying on the GUI.
	 */
	private Image						playerRedImage;
	private Image						playerBlueImage;
	private Image						wallImage;
	private Image						lightGrenadeImage;
	private Image						lightTrailImage;
	private Image						finishBlue;
	private Image						finishRed;
	
	/**
	 * This is the list of items that the current player can interact with.
	 */
	private gui.List					itemList;
	private Object						itemListSelected;
	private gui.List					inventoryList;
	private Object						inventoryListSelected;
	
	/**
	 * Offsets that determine where the top left position of the grid will be.
	 */
	private int							topLeftGridOffsetX	= 150;
	private int							topLeftGridOffsetY	= 42;
	
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
	public GUI(MoveController moveCont, PickUpItemController pickupCont,
			UseItemController useitemCont, GetInventoryListController getInvListCont,
			GetItemListController getItemListCont) {
		this.moveController = moveCont;
		this.pickUpController = pickupCont;
		this.useItemController = useitemCont;
		this.getInventoryListController = getInvListCont;
		this.getItemListController = getItemListCont;
	}
	
	/**
	 * Start the GUI thread by creating the GUI elements and displaying them.
	 */
	public void run() {
		this.gui = new AGUI("GUI", 200 + (SQUARE_SIZE * gridWidth),
				200 + (SQUARE_SIZE * gridHeight)) {
			
			@Override
			public void paint(Graphics2D graphics) {
				// This block is executed with each repaint():
				
				graphics.drawString("items on square:", 10, 275);
				graphics.drawString("items in inventory:", 10, 405);
				
				// Adjust the grid dimensions and GUI size:
				gridHeight = getGridHeigth(grid);
				gridWidth = getGridWidth(grid);
				
				gui.getPanel().setSize(200 + (SQUARE_SIZE * gridWidth),
						200 + (SQUARE_SIZE * gridHeight));
				gui.getFrame().setSize(200 + (SQUARE_SIZE * gridWidth),
						200 + (SQUARE_SIZE * gridHeight));
				// Draw grid lines
				for (int r = 0; r < gridHeight; r++) {
					for (int c = 0; c < gridWidth; c++) {
						graphics.drawRect(topLeftGridOffsetX + ((SQUARE_SIZE) * c),
								topLeftGridOffsetY + ((SQUARE_SIZE) * r), (SQUARE_SIZE),
								(SQUARE_SIZE));
					}
				}
				
				// Draw the two finish squares:
				Coordinate guiCoordFinishRed = toGUICoord(new Coordinate(0, gridHeight - 1));
				Coordinate guiCoordFinishBlue = toGUICoord(new Coordinate(gridWidth - 1, 0));
				graphics.drawImage(finishBlue, guiCoordFinishBlue.getX(),
						guiCoordFinishBlue.getY(), SQUARE_SIZE, SQUARE_SIZE, null);
				graphics.drawImage(finishRed, guiCoordFinishRed.getX(), guiCoordFinishRed.getY(),
						SQUARE_SIZE, SQUARE_SIZE, null);
				
				// Populate the grid squares with the correct images:
				Set<Coordinate> gridCoords = grid.getAllGridCoordinates();
				
				
				for (Coordinate c : gridCoords) {
					// TODO draw players anders? coords opvragen..
					ASquare square = grid.getSquareAt(c);
					IPlayer player = square.getPlayer();
					Coordinate guiCoord = toGUICoord(c);
					
					// Draw players if necessary
					if (player != null) {
						switch (player.getID()) {
							case 1:
								graphics.drawImage(playerBlueImage, guiCoord.getX(),
										guiCoord.getY(), SQUARE_SIZE, SQUARE_SIZE, null);
								break;
							case 2:
								graphics.drawImage(playerRedImage, guiCoord.getX(),
										guiCoord.getY(), SQUARE_SIZE, SQUARE_SIZE, null);
								break;
							default:
								break;
						}
					}
					
					// Draw wall if necessary
					if (square.getClass() == Wall.WallPart.class) {
						graphics.drawImage(wallImage, guiCoord.getX(), guiCoord.getY(),
								SQUARE_SIZE, SQUARE_SIZE, null);
					}
					
					// Draw lighttrail if necessary
					if (square.hasLightTrail()) {
						graphics.drawImage(lightTrailImage, guiCoord.getX(), guiCoord.getY(),
								SQUARE_SIZE, SQUARE_SIZE, null);
					}
					
					// Draw items if necessary
					List<IItem> itemList = square.getCarryableItems();
					for (IItem i : itemList) {
						if (i instanceof LightGrenade) {
							graphics.drawImage(lightGrenadeImage, guiCoord.getX(), guiCoord.getY(),
									SQUARE_SIZE, SQUARE_SIZE, null);
						}
					}
					
				}
				
				
				
				// Draw the list of items that are on the current square.
				
				// TODO get items of inventory and items on the square.
				
				Vector<Item> itemsSquare = new Vector<Item>();
				Vector<Item> itemsInventory = new Vector<Item>();
				
				itemList.setListData(itemsSquare);
				inventoryList.setListData(itemsInventory);
				
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
						// moveController.movePlayer(Direction.NORTH);
						
						gui.repaint();
					}
				});
		upButton.setImage(gui.loadImage("arrow_N.png", SQUARE_SIZE, SQUARE_SIZE));
		// --
		Button leftButton = gui.createButton(moveArrowsOffsetX + 0, moveArrowsOffsetY + 40, 40, 40,
				new Runnable() {
					
					public void run() {
						// moveController.movePlayer(Direction.WEST);
						
						gui.repaint();
					}
				});
		leftButton.setImage(gui.loadImage("arrow_W.png", SQUARE_SIZE, SQUARE_SIZE));
		// ---
		Button rightButton = gui.createButton(moveArrowsOffsetX + 80, moveArrowsOffsetY + 40, 40,
				40, new Runnable() {
					
					public void run() {
						// moveController.movePlayer(Direction.EAST);
						
						gui.repaint();
					}
				});
		rightButton.setImage(gui.loadImage("arrow_E.png", SQUARE_SIZE, SQUARE_SIZE));
		// --
		Button downButton = gui.createButton(moveArrowsOffsetX + 40, moveArrowsOffsetY + 80, 40,
				40, new Runnable() {
					
					public void run() {
						// moveController.movePlayer(Direction.SOUTH);
						
						gui.repaint();
					}
				});
		downButton.setImage(gui.loadImage("arrow_S.png", SQUARE_SIZE, SQUARE_SIZE));
		// --
		Button NEButton = gui.createButton(moveArrowsOffsetX + 80, moveArrowsOffsetY, 40, 40,
				new Runnable() {
					
					public void run() {
						// moveController.movePlayer(Direction.NORTHEAST);
						
						gui.repaint();
					}
				});
		NEButton.setImage(gui.loadImage("arrow_NE.png", SQUARE_SIZE, SQUARE_SIZE));
		// --
		Button SEButton = gui.createButton(moveArrowsOffsetX + 80, moveArrowsOffsetY + 80, 40, 40,
				new Runnable() {
					
					public void run() {
						// moveController.movePlayer(Direction.SOUTHEAST);
						
						gui.repaint();
					}
				});
		SEButton.setImage(gui.loadImage("arrow_SE.png", SQUARE_SIZE, SQUARE_SIZE));
		// --
		Button SWButton = gui.createButton(moveArrowsOffsetX, moveArrowsOffsetY + 80, 40, 40,
				new Runnable() {
					
					public void run() {
						// moveController.movePlayer(Direction.SOUTHWEST);
						
						gui.repaint();
					}
				});
		SWButton.setImage(gui.loadImage("arrow_SW.png", SQUARE_SIZE, SQUARE_SIZE));
		// --
		Button NWButton = gui.createButton(moveArrowsOffsetX, moveArrowsOffsetY, 40, 40,
				new Runnable() {
					
					public void run() {
						// moveController.movePlayer(Direction.NORTHWEST);
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
						
						// Use the itemListSelected to access the Item that is
						// selected in the items on square list!
						System.out.println("item list selected:" + itemListSelected);
						gui.repaint();
					}
				});
		pickItemUpButton.setText("Pick up item");
		// ----
		Button dropMineButton = gui.createButton(actionButtonsOffsetX, actionButtonsOffsetY + 40,
				120, 30, new Runnable() {
					
					public void run() {
						// TODO button drop mine pressed
						
						// Use the inventoryListSelected to access the Item that
						// is
						// selected in the inventory!
						System.out.println("inventory list selected:" + inventoryListSelected);
						gui.repaint();
					}
				});
		dropMineButton.setText("Drop mine");
		/* ---- ---- ---- ----END OF ACTION BUTTONS---- ---- ---- ---- */
		
		/* ---- ---- ---- ---- LISTS ---- ---- ---- ---- */
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
				Object selectedValue = inventoryList.getSelectedValue();
				
				// only update if it's a select operation, and not a deselect
				if (selectedValue != null) {
					inventoryListSelected = inventoryList.getSelectedValue();
				}
			}
		});
		/* ---- ---- ---- ---- END OF LISTS ---- ---- ---- ---- */
	}
	
	/**
	 * Draw a whole Grid object on the GUI.
	 * 
	 * @param g
	 *        The Grid to draw.
	 */
	public void draw(Grid g) {
		this.grid = g;
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
	private int getGridHeigth(Grid g) {
		Set<Coordinate> gridCoords = grid.getAllGridCoordinates();
		
		int maxRowNum = 0;
		for (Coordinate c : gridCoords) {
			if (c.getY() > maxRowNum)
				maxRowNum = c.getY();
		}
		
		return maxRowNum + 1;
	}
	
	/**
	 * This method will return the number of columns in a grid.
	 * 
	 * @param g
	 *        The grid.
	 * @return The number of columns in the grid.
	 */
	private int getGridWidth(Grid g) {
		Set<Coordinate> gridCoords = grid.getAllGridCoordinates();
		
		int maxColNum = 0;
		for (Coordinate c : gridCoords) {
			if (c.getX() > maxColNum)
				maxColNum = c.getY();
		}
		
		return maxColNum + 1;
	}
}
