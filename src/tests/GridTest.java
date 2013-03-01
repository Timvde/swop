package tests;

import static org.junit.Assert.*;
import grid.ASquare;
import grid.Grid;
import org.junit.Test;
import grid.Wall.WallPart;


public class GridTest {
	
	@Test
	public void testConstructor() {
		Grid grid = new Grid.Builder().build();
		int numberOfWalls = 2;
		for (ASquare sq : grid.getGrid().values()) 
			if(sq.getClass() == WallPart.class)
				numberOfWalls++;
		assertTrue(numberOfWalls >= 2); // Minimum wall size
		assertTrue(numberOfWalls <= 20);// 20% of 100 squares filled
	}
	
}
