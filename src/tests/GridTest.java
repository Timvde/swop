package tests;

import grid.Grid;
import org.junit.Test;


public class GridTest {
	
	@Test
	public void testConstructor() {
		Grid grid = new Grid.Builder().build();
	}
	
}
