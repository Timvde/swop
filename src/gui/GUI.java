package gui;

import grid.Coordinate;
import grid.Grid;
import item.IItem;
import item.Item;
import item.identitydisk.ChargedIdentityDisk;
import item.identitydisk.IdentityDisk;
import item.lightgrenade.LightGrenade;
import item.lightgrenade.LightGrenade.LightGrenadeState;
import item.teleporter.Teleporter;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import javax.swing.JOptionPane;
import player.IPlayer;
import square.Direction;
import square.Square;
import square.AbstractSquare;
import ObjectronExceptions.IllegalMoveException;
import ObjectronExceptions.IllegalUseException;
import ObjectronExceptions.builderExceptions.GridBuildException;
import controllers.EndTurnController;
import controllers.GUIDataController;
import controllers.MoveController;
import controllers.NewGameController;
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
	
	/**
	 * The following values are not final and will be updated with each redraw,
	 * depending on the Grid object dimension. These are just temp start values.
	 */
	private int						gridHeight			= 10;
	private int						gridWidth			= 10;
	
	/**
	 * This variable is the size of a square on the GUI. If modified, the grid
	 * and all images will automaticly be resized.
	 */
	private final static int		SQUARE_SIZE			= 40;
	
	/**
	 * Controllers for interacting with the game engine.
	 */
	private NewGameController		newGameController;
	private EndTurnController		endTurnController;
	private MoveController			moveController;
	private PickUpItemController	pickUpController;
	private UseItemController		useItemController;
	private GUIDataController		guiDataController;
	
	/**
	 * Image objects for displaying on the GUI.
	 */
	private Image					playerRedImage;
	private Image					playerBlueImage;
	private Image					wallImage;
	private Image					lightGrenadeImage;
	private Image					lightGrenadeExplodedImage;
	private Image					teleporterImage;
	private Image					identityDiskImage;
	private Image					chargedIdentityDiskImage;
	private Image					lightTrailImage;
	private Image					finish;
	private Image					powerfailure;
	private Image					greenBackground;
	private Image					squareBackground;
	
	/**
	 * This is the list of items that the current player can interact with.
	 */
	private gui.List				itemList;
	private Object					itemListSelected;
	private gui.List				inventoryList;
	private Object					inventoryListSelected;
	
	/**
	 * The grid dimension textfields that need to be a static instance field for
	 * referral reasons.
	 */
	private static TextField		gridWidthTextField;
	private static TextField		gridHeightTextField;
	
	/**
	 * The grid file textfield that needs to be a static instance field for
	 * refferal reasons.
	 */
	private static TextField		gridFileTextField;
	
	/**
	 * Offsets that determine where the top left position of the grid will be.
	 */
	private int						topLeftGridOffsetX	= 150;
	private int						topLeftGridOffsetY	= 45;
	
	/**
	 * Construct a new GUI and initialize the controllers.
	 * 
	 * @param moveCont
	 *        The move controller.
	 * @param pickupCont
	 *        The pickup item controller.
	 * @param endTurnCont
	 *        The end of turn controller.
	 * @param useitemCont
	 *        The use item controller.
	 * @param newGameCont
	 *        The new game controller.
	 * @param guiDataCont
	 *        The GUI data controller.
	 */
	public GUI(MoveController moveCont, PickUpItemController pickupCont,
			UseItemController useitemCont, NewGameController newGameCont,
			EndTurnController endTurnCont, GUIDataController guiDataCont) {
		this.moveController = moveCont;
		this.pickUpController = pickupCont;
		this.useItemController = useitemCont;
		this.newGameController = newGameCont;
		this.endTurnController = endTurnCont;
		this.guiDataController = guiDataCont;
	}
	
	/**
	 * Start the GUI thread by creating the GUI elements and displaying them.
	 */
	public void run() {
		// These initial width and height only show the New Game button.
		this.gui = new AGUI("Objectron  |  Groep 9", 140, 160) {
			
			@Override
			public void paint(Graphics2D graphics) {
				// Only paint if a new game started, meaning there is a grid.
				if (guiDataController.hasGrid()) {
					// This block is executed with each repaint():
					
					// Draw some labels:
					graphics.drawString("x", 64, 35);
					graphics.drawString("items on square:", 10, 316);
					graphics.drawString("items in inventory:", 10, 444);
					graphics.drawString("CURRENT PLAYER:", 550, 19);
					graphics.drawString(guiDataController.getCurrentPlayer().getID() + "", 666, 19);
					graphics.drawString("ACTIONS LEFT:", 550, 32);
					graphics.drawString(guiDataController.getCurrentPlayer()
							.getAllowedNumberOfActions() + "", 646, 32);
					
					// Adjust the grid dimensions and GUI size:
					gridHeight = guiDataController.getGridHeight();
					gridWidth = guiDataController.getGridWidth();
					
					int GUIheight = 100 + (SQUARE_SIZE * gridHeight);
					int GUIwidth = 180 + (SQUARE_SIZE * gridWidth);
					if (GUIheight < 610)
						GUIheight = 610;
					if (GUIwidth < 720)
						GUIwidth = 720;
					
					gui.getPanel().setSize(GUIwidth, GUIheight);
					gui.getFrame().setSize(GUIwidth, GUIheight);
					
					// Draw grid lines
					for (int r = 0; r < gridHeight; r++) {
						for (int c = 0; c < gridWidth; c++) {
							graphics.drawRect(topLeftGridOffsetX + ((SQUARE_SIZE) * c),
									topLeftGridOffsetY + ((SQUARE_SIZE) * r), (SQUARE_SIZE),
									(SQUARE_SIZE));
						}
					}
					
					// Populate the grid squares with the correct images:
					Set<Coordinate> gridCoords = guiDataController.getAllGridCoordinates();
					
					// draw the square backgrounds. This is done in a seperate
					// loop for layering reasons.
					for (Coordinate c : gridCoords) {
						Coordinate guiCoord = toGUIGridCoord(c);
						
						graphics.drawImage(squareBackground, guiCoord.getX() + 1,
								guiCoord.getY() + 1, SQUARE_SIZE - 1, SQUARE_SIZE - 1, null);
					}
					
					for (Coordinate c : gridCoords) {
						Square square = guiDataController.getSquareAt(c);
						IPlayer player = square.getPlayer();
						Coordinate guiCoord = toGUIGridCoord(c);
						
						// Draw finish lines
						if (guiDataController.isPlayerStartingPosition(square)) {
							graphics.drawImage(finish, guiCoord.getX(),
									guiCoord.getY(), SQUARE_SIZE, SQUARE_SIZE, null);
						}
						
						// Draw powerfailures if necessary
						if (square.hasPowerFailure()) {
							graphics.drawImage(powerfailure, guiCoord.getX(), guiCoord.getY(),
									SQUARE_SIZE, SQUARE_SIZE, null);
						}
						
						// Draw wall if necessary
						if (((AbstractSquare) square).isWall()) {
							graphics.drawImage(wallImage, guiCoord.getX(), guiCoord.getY(),
									SQUARE_SIZE, SQUARE_SIZE, null);
						}
						
						// Draw lighttrail if necessary
						if (square.hasLightTrail()) {
							graphics.drawImage(lightTrailImage, guiCoord.getX(), guiCoord.getY(),
									SQUARE_SIZE, SQUARE_SIZE, null);
						}
						
						// Draw items if necessary
						List<IItem> itemList = guiDataController.getItemList(c);
						for (IItem i : itemList) {
							if (i.getClass() == LightGrenade.class) {
								LightGrenade l = (LightGrenade) i;
								if (l.getState() == LightGrenadeState.INACTIVE) {
									graphics.drawImage(lightGrenadeImage, guiCoord.getX(),
											guiCoord.getY(), SQUARE_SIZE, SQUARE_SIZE, null);
								}
								if (l.getState() == LightGrenadeState.EXPLODED) {
									graphics.drawImage(lightGrenadeExplodedImage, guiCoord.getX(),
											guiCoord.getY(), SQUARE_SIZE, SQUARE_SIZE, null);
								}
							}
							if (i.getClass() == Teleporter.class) {
								graphics.drawImage(teleporterImage, guiCoord.getX(),
										guiCoord.getY(), SQUARE_SIZE, SQUARE_SIZE, null);
							}
							if (i instanceof ChargedIdentityDisk)
								graphics.drawImage(chargedIdentityDiskImage, guiCoord.getX(),
										guiCoord.getY(), SQUARE_SIZE, SQUARE_SIZE, null);
							else if (i instanceof IdentityDisk) {
								graphics.drawImage(identityDiskImage, guiCoord.getX(),
										guiCoord.getY(), SQUARE_SIZE, SQUARE_SIZE, null);
							}
						}
						
						// Draw players if necessary, this should happen last.
						if (player != null) {
							switch (player.getID()) {
								case 1:
									if (guiDataController.getCurrentPlayer().getID() == 1) {
										graphics.drawImage(greenBackground, guiCoord.getX(),
												guiCoord.getY(), SQUARE_SIZE, SQUARE_SIZE, null);
									}
									graphics.drawImage(playerBlueImage, guiCoord.getX(),
											guiCoord.getY(), SQUARE_SIZE, SQUARE_SIZE, null);
									break;
								case 2:
									if (guiDataController.getCurrentPlayer().getID() == 2) {
										graphics.drawImage(greenBackground, guiCoord.getX(),
												guiCoord.getY(), SQUARE_SIZE, SQUARE_SIZE, null);
									}
									graphics.drawImage(playerRedImage, guiCoord.getX(),
											guiCoord.getY(), SQUARE_SIZE, SQUARE_SIZE, null);
									
									break;
								default:
									break;
							}
						}
					}
					
					// Show the items in the list that the current player can
					// interact with
					Vector<IItem> itemsSquare = new Vector<IItem>();
					List<IItem> itemsOfSquare = guiDataController.getItemsOnSquareOfCurrentPlayer();
					
					// add them into a Vector, because setListData of our list
					// doesn't accept a List object.
					for (IItem i : itemsOfSquare) {
						itemsSquare.add((Item) i);
					}
					
					itemList.setListData(itemsSquare);
					
					// Show the player's inventory items in the inventory list
					Vector<Item> itemsInventory = new Vector<Item>();
					List<IItem> itemsOfPlayer = guiDataController.getCurrentPlayerInventoryItems();
					
					// add them into a Vector, because setListData of our list
					// doesn't accept a List object.
					for (IItem i : itemsOfPlayer) {
						itemsInventory.add((Item) i);
					}
					
					inventoryList.setListData(itemsInventory);
					
				}
			}
		};
		
		// Initialize images
		this.playerRedImage = gui.loadImage("player_red.png", SQUARE_SIZE, SQUARE_SIZE);
		this.playerBlueImage = gui.loadImage("player_blue.png", SQUARE_SIZE, SQUARE_SIZE);
		this.wallImage = gui.loadImage("wall.png", SQUARE_SIZE, SQUARE_SIZE);
		this.lightGrenadeImage = gui.loadImage("lightgrenade.png", SQUARE_SIZE, SQUARE_SIZE);
		this.lightGrenadeExplodedImage = gui.loadImage("lightgrenade_exploded.png", SQUARE_SIZE,
				SQUARE_SIZE);
		this.teleporterImage = gui.loadImage("icon.png", SQUARE_SIZE, SQUARE_SIZE);
		this.identityDiskImage = gui.loadImage("identity_disk.png", SQUARE_SIZE, SQUARE_SIZE);
		this.chargedIdentityDiskImage = gui.loadImage("charged_identity_disk.png", SQUARE_SIZE,
				SQUARE_SIZE);
		this.lightTrailImage = gui.loadImage("lighttrail_custom.png", SQUARE_SIZE, SQUARE_SIZE);
		this.finish = gui.loadImage("cell_finish.png", SQUARE_SIZE, SQUARE_SIZE);
		this.powerfailure = gui.loadImage("powerfailure.png", SQUARE_SIZE, SQUARE_SIZE);
		this.greenBackground = gui.loadImage("currentplayer_background.png", SQUARE_SIZE,
				SQUARE_SIZE);
		this.squareBackground = gui.loadImage("square_background.png", SQUARE_SIZE, SQUARE_SIZE);
		
		// Create the width and height config text fields
		gridWidthTextField = gui.createTextField(35, 20, 25, 20);
		gridHeightTextField = gui.createTextField(75, 20, 25, 20);
		gridWidthTextField.setText("12");
		gridHeightTextField.setText("12");
		
		// create the grid from file text field
		gridFileTextField = gui.createTextField(11, 90, 119, 20);
		gridFileTextField.setText("grid.txt");
		
		/* ---- ---- ---- ---- MOVE ARROWS ---- ---- ---- ---- */
		
		// Use the two offsets below to move all arrows at once
		int moveArrowsOffsetX = 10;
		int moveArrowsOffsetY = 166;
		
		Button upButton = gui.createButton(moveArrowsOffsetX + 40, moveArrowsOffsetY + 0, 40, 40,
				new Runnable() {
					
					public void run() {
						try {
							moveController.move(Direction.NORTH);
						}
						catch (IllegalMoveException e) {
							JOptionPane.showMessageDialog(gui.getFrame(), e.getMessage());
						}
						
						gui.repaint();
					}
				});
		upButton.setImage(gui.loadImage("arrow_N.png", SQUARE_SIZE, SQUARE_SIZE));
		// --
		Button leftButton = gui.createButton(moveArrowsOffsetX + 0, moveArrowsOffsetY + 40, 40, 40,
				new Runnable() {
					
					public void run() {
						try {
							moveController.move(Direction.WEST);
						}
						catch (IllegalMoveException e) {
							JOptionPane.showMessageDialog(gui.getFrame(), e.getMessage());
						}
						
						gui.repaint();
					}
				});
		leftButton.setImage(gui.loadImage("arrow_W.png", SQUARE_SIZE, SQUARE_SIZE));
		// ---
		Button rightButton = gui.createButton(moveArrowsOffsetX + 80, moveArrowsOffsetY + 40, 40,
				40, new Runnable() {
					
					public void run() {
						try {
							moveController.move(Direction.EAST);
						}
						catch (IllegalMoveException e) {
							JOptionPane.showMessageDialog(gui.getFrame(), e.getMessage());
						}
						
						gui.repaint();
					}
				});
		rightButton.setImage(gui.loadImage("arrow_E.png", SQUARE_SIZE, SQUARE_SIZE));
		// --
		Button downButton = gui.createButton(moveArrowsOffsetX + 40, moveArrowsOffsetY + 80, 40,
				40, new Runnable() {
					
					public void run() {
						try {
							moveController.move(Direction.SOUTH);
						}
						catch (IllegalMoveException e) {
							JOptionPane.showMessageDialog(gui.getFrame(), e.getMessage());
						}
						
						gui.repaint();
					}
				});
		downButton.setImage(gui.loadImage("arrow_S.png", SQUARE_SIZE, SQUARE_SIZE));
		// --
		Button NEButton = gui.createButton(moveArrowsOffsetX + 80, moveArrowsOffsetY, 40, 40,
				new Runnable() {
					
					public void run() {
						try {
							moveController.move(Direction.NORTHEAST);
						}
						catch (IllegalMoveException e) {
							JOptionPane.showMessageDialog(gui.getFrame(), e.getMessage());
						}
						gui.repaint();
					}
				});
		NEButton.setImage(gui.loadImage("arrow_NE.png", SQUARE_SIZE, SQUARE_SIZE));
		// --
		Button SEButton = gui.createButton(moveArrowsOffsetX + 80, moveArrowsOffsetY + 80, 40, 40,
				new Runnable() {
					
					public void run() {
						try {
							moveController.move(Direction.SOUTHEAST);
						}
						catch (IllegalMoveException e) {
							JOptionPane.showMessageDialog(gui.getFrame(), e.getMessage());
						}
						
						gui.repaint();
					}
				});
		SEButton.setImage(gui.loadImage("arrow_SE.png", SQUARE_SIZE, SQUARE_SIZE));
		// --
		Button SWButton = gui.createButton(moveArrowsOffsetX, moveArrowsOffsetY + 80, 40, 40,
				new Runnable() {
					
					public void run() {
						try {
							moveController.move(Direction.SOUTHWEST);
						}
						catch (IllegalMoveException e) {
							JOptionPane.showMessageDialog(gui.getFrame(), e.getMessage());
						}
						gui.repaint();
					}
				});
		SWButton.setImage(gui.loadImage("arrow_SW.png", SQUARE_SIZE, SQUARE_SIZE));
		// --
		Button NWButton = gui.createButton(moveArrowsOffsetX, moveArrowsOffsetY, 40, 40,
				new Runnable() {
					
					public void run() {
						try {
							moveController.move(Direction.NORTHWEST);
						}
						catch (IllegalMoveException e) {
							JOptionPane.showMessageDialog(gui.getFrame(), e.getMessage());
						}
						gui.repaint();
					}
				});
		NWButton.setImage(gui.loadImage("arrow_NW.png", SQUARE_SIZE, SQUARE_SIZE));
		
		/* ---- ---- ---- ---- END OF MOVE ARROWS ---- ---- ---- ---- */
		
		/* ---- ---- ---- ---- ACTION BUTTONS ---- ---- ---- ---- */
		// Use these offsets to move all the action buttons at once:
		int actionButtonsOffsetX = 10;
		int actionButtonsOffsetY = 5;
		
		Button pickItemUpButton = gui.createButton(actionButtonsOffsetX + 140,
				actionButtonsOffsetY, 120, 30, new Runnable() {
					
					public void run() {
						// Use the itemListSelected to access the Item that is
						// selected in the items on square list!
						if (itemListSelected != null) {
							pickUpController.pickUpItem((IItem) itemListSelected);
							itemListSelected = null;
							gui.repaint();
						}
						else {
							JOptionPane.showMessageDialog(gui.getFrame(),
									"Please select an item to pick up.");
						}
					}
				});
		pickItemUpButton.setText("Pick up item");
		// ----
		Button useItemButton = gui.createButton(actionButtonsOffsetX + 270, actionButtonsOffsetY,
				120, 30, new Runnable() {
					
					public void run() {
						// Use the inventoryListSelected to access the Item that
						// is selected in the inventory!
						if (inventoryListSelected != null) {
							try {
								useItemController.useItem((IItem) inventoryListSelected);
							}
							catch (IllegalUseException e) {
								JOptionPane.showMessageDialog(gui.getFrame(),
										e.getMessage());
							}
							inventoryListSelected = null;
							gui.repaint();
						}
						else {
							JOptionPane.showMessageDialog(gui.getFrame(),
									"Please select an item to use.");
						}
					}
				});
		useItemButton.setText("Use item");
		// ----
		Button randomGameButton = gui.createButton(actionButtonsOffsetX, actionButtonsOffsetY + 40,
				120, 30, new Runnable() {
					
					public void run() {
						int width = Integer.parseInt(gridWidthTextField.getText());
						int height = Integer.parseInt(gridHeightTextField.getText());
						
						try {
							newGameController.newGame(width, height);
						}
						catch (IllegalArgumentException e) {
							JOptionPane.showMessageDialog(gui.getFrame(),
									"Please specify valid dimensions for the grid");
						}
					}
				});
		randomGameButton.setText("Rndm Game");
		// ----
		Button fileGameButton = gui.createButton(actionButtonsOffsetX, actionButtonsOffsetY + 111,
				120, 30, new Runnable() {
					
					public void run() {
						String fileName = gridFileTextField.getText();
						
						try {
							newGameController.newGame(fileName);
						}
						catch (GridBuildException e) {
							JOptionPane.showMessageDialog(gui.getFrame(),
									"The specified file is invalid.");
						}
					}
				});
		fileGameButton.setText("File Game");
		// ----
		Button endTurnButton = gui.createButton(actionButtonsOffsetX + 400, actionButtonsOffsetY,
				120, 30, new Runnable() {
					
					public void run() {
						// Custom button text
						Object[] options = { "Yes", "No" };
						int response = JOptionPane.showOptionDialog(gui.getFrame(),
								"Are you sure? If you have not moved yet, you will lose the game.",
								"Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,
								null, options, options[1]);
						
						if (response == 0) {
							endTurnController.endTurn();
						}
						
						gui.repaint();
					}
				});
		endTurnButton.setText("End Turn");
		/* ---- ---- ---- ----END OF ACTION BUTTONS---- ---- ---- ---- */
		
		/* ---- ---- ---- ---- LISTS ---- ---- ---- ---- */
		int listsOffsetX = 10;
		int listsOffsetY = 320;
		
		itemList = gui.createList(listsOffsetX, listsOffsetY, 120, 100, new Runnable() {
			
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
		inventoryList = gui.createList(listsOffsetX, listsOffsetY + 130, 120, 100, new Runnable() {
			
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
	 * @param grid
	 *        The Grid to draw.
	 */
	public void draw(Grid grid) {
		this.guiDataController.setGrid(grid);
		
		if (this.gui != null)
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
	private Coordinate toGUIGridCoord(Coordinate c) {
		int x = (c.getX() * 40) + topLeftGridOffsetX;
		int y = (c.getY() * 40) + topLeftGridOffsetY;
		
		return new Coordinate(x, y);
	}
	
	/**
	 * Ask the user for a basic direction and return this.
	 * 
	 * @return the direction the user has chosen
	 */
	public Direction getBasicDirection() {
		Object[] options = { "North", "East", "South", "West" };
		
		int response = JOptionPane
				.showOptionDialog(null, "Choose a direction in which to fire the identity disk:",
						"Identity disk", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
						null, options, options[0]);
		
		switch (response) {
			case 0:
				return Direction.NORTH;
			case 1:
				return Direction.EAST;
			case 2:
				return Direction.SOUTH;
			case 3:
				return Direction.WEST;
			default:
				throw new IllegalStateException("User chose illegal direction to fire ID");
		}
		
	}
}
