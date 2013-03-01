package tests;

import static org.junit.Assert.*;
import java.io.IOException;
import grid.Coordinate;
import grid.Wall;
import grid.Wall.WallPart;
import org.junit.BeforeClass;
import org.junit.Test;

public class WallTest {
	
	private static Coordinate	start;
	private static Coordinate	validEnd;
	private static Wall			wall;
	
	@BeforeClass
	public static void setUpBeforeClass() {
		start = new Coordinate(2, 5);
		validEnd = new Coordinate(2, 8);
		wall = new Wall(start, validEnd);
	}
	
	@Test
	public void testConstructorNormalCase() {
		new Wall(start, validEnd);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testConstructorUnAlignedCoords() {
		Coordinate end = new Coordinate(3, 7);
		
		new Wall(start, end);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testConstructorWallTooSmall() {
		new Wall(start, start);
	}
	
	@Test
	public void testTouchingWallNotTouching() {
		Wall wall2 = new Wall(new Coordinate(4, 3), new Coordinate(6, 3));
		Wall wall3 = new Wall(new Coordinate(2, 3), new Coordinate(6, 3));
		
		assertFalse(wall.touchesWall(wall2));
		assertFalse(wall.touchesWall(wall3));
	}
	
	@Test
	public void testTouchingWallIntersecting() {
		Wall wall2 = new Wall(new Coordinate(1, 6), new Coordinate(3, 6));
		
		assertTrue(wall.touchesWall(wall2));
	}
	
	@Test
	public void testTouchingWallTouching() {
		Wall wall2 = new Wall(new Coordinate(1, 6), new Coordinate(3, 6));
		Wall wall3 = new Wall(new Coordinate(2, 6), new Coordinate(4, 6));
		
		assertTrue(wall.touchesWall(wall2));
		assertTrue(wall2.touchesWall(wall3));
		
		// This is explicitly tested again, because it failed in our program at
		// some point.
		Wall first = new Wall(new Coordinate(8, 4), new Coordinate(8, 7));
		Wall second = new Wall(new Coordinate(8, 3), new Coordinate(4, 3));
		
		assertTrue(first.touchesWall(second));
	}
	
	@Test
	public void testGetWallPart() {
		WallPart part1 = wall.getWallPart();
		WallPart part2 = new Wall(start, new Coordinate(3, 5)).getWallPart();
		
		assertEquals(part1, part2);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testWallPartMethods() {
		WallPart part = wall.getWallPart();
		assertEquals(part.getCarryableItems().size(), 0);
		assertEquals(part.getPlayer(), null);
		assertFalse(part.hasLightTrail());
		assertFalse(part.hasItemWithID(0));
		
		// This one will throw
		part.pickupItem(0);
	}
}
