package gui;

import java.awt.Graphics2D;
import java.awt.Image;
import gui.Button;
import gui.GUI;

public class GUILoader {
	
	public static void main(String[] args) {
		// All code that accesses the simple GUI must run in the AWT event
		// handling thread.
		// A simple way to achieve this is to run the entire application in the
		// AWT event handling thread.
		// This is done by simply wrapping the body of the main method in a call
		// of EventQueue.invokeLater.
		java.awt.EventQueue.invokeLater(new Runnable() {
			
			Image	playerRed;
			Image	playerBlue;
			String	status	= "teststatus";
			int		y;
			int		x;
			
			// Non-changing labels
			String statusLBL = "Status:";
			
			public void run() {
				
				final GUI gui = new GUI("GUI", 400, 600) {
					
					@Override
					public void paint(Graphics2D graphics) {
						// Deze blok wordt bij elke repaint uitgevoerd:
						
						// (img, x, y, width, height, Observer)
						graphics.drawImage(playerRed, x * 40, y * 40, 40, 40, null);
						graphics.drawImage(playerBlue, x * 40, y * 40, 40, 40, null);

						
						graphics.drawString(status, 60, 20);
						graphics.drawString(statusLBL, 10, 20);
					}
					
					@Override
					public void handleMouseClick(int x, int y, boolean doubleClick) {
						System.out.println((doubleClick ? "Doubleclicked" : "Clicked") + " at ("
								+ x + ", " + y + ")");
					}
					
				};
				
				// Initialize player images
				playerRed = gui.loadImage("player_red.png", 40, 40);
				playerBlue = gui.loadImage("player_red.png", 40, 40);

				
				/* ---- ---- ---- ---- MOVE ARROWS ---- ---- ---- ---- */
				
				// Use the two offsets below to move all arrows at once
				int moveArrowsOffsetX = 10;
				int moveArrowsOffsetY = 40;
				
				Button upButton = gui.createButton(moveArrowsOffsetX + 40, moveArrowsOffsetY + 0,
						40, 40, new Runnable() {
							
							public void run() {
								y--;
								gui.repaint();
							}
						});
				upButton.setImage(gui.loadImage("arrow_N.png", 40, 40));
				// --
				Button leftButton = gui.createButton(moveArrowsOffsetX + 0, moveArrowsOffsetY + 40,
						40, 40, new Runnable() {
							
							public void run() {
								x--;
								gui.repaint();
							}
						});
				leftButton.setImage(gui.loadImage("arrow_W.png", 40, 40));
				// --
				Button rightButton = gui.createButton(moveArrowsOffsetX + 80,
						moveArrowsOffsetY + 40, 40, 40, new Runnable() {
							
							public void run() {
								x++;
								gui.repaint();
							}
						});
				rightButton.setImage(gui.loadImage("arrow_E.png", 40, 40));
				// --
				Button downButton = gui.createButton(moveArrowsOffsetX + 40,
						moveArrowsOffsetY + 80, 40, 40, new Runnable() {
							
							public void run() {
								y++;
								gui.repaint();
							}
						});
				downButton.setImage(gui.loadImage("arrow_S.png", 40, 40));
				
				/* ---- ---- ---- ---- END OF MOVE ARROWS ---- ---- ---- ---- */
				
			}
		});
		
	}
}
