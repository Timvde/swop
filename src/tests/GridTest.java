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
		int numberOfWalls = 0;
		for (ASquare sq : grid.getGrid().values()) 
			if(sq.getClass() == WallPart.class)
				numberOfWalls++;
		assertEquals(20, numberOfWalls);
	}
	
}
