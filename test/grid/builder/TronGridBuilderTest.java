package grid.builder;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import effects.RaceEffectFactory;
import grid.Coordinate;
import grid.Grid;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import square.Direction;
import square.SquareContainer;
import ObjectronExceptions.builderExceptions.GridBuildException;

@SuppressWarnings("javadoc")
public class TronGridBuilderTest {
	
	private TronGridBuilder	builder;
	
	@Before
	public void setUp() {
		builder = new TronGridBuilder(new RaceEffectFactory());
	}
	
	@Test
	public void testgetResult() {
		Grid result = builder.getResult();
		assertNotNull(result);
		assertEquals(0, result.getAllGridCoordinates().size());
		
		Coordinate c = Coordinate.random(100, 100);
		builder.addSquare(c);
		Grid result2 = builder.getResult();
		assertNotSame(result2, result);
		assertEquals(1, result.getAllGridCoordinates().size());
		assertNotNull(result.getSquareAt(c));
		
		builder.createNewEmptyGrid();
		assertEquals(0, builder.getResult().getAllGridCoordinates().size());
	}
	
	@Test
	public void testNumberOfSquares() {
		int numberOfSquares = 0;
		assertEquals(numberOfSquares, builder.getNumberOfSquares());
		assertEquals(builder.getResult().getAllGridCoordinates().size(),
				builder.getNumberOfSquares());
		
		builder.addSquare(Coordinate.random(100, 100));
		assertEquals(numberOfSquares + 1, builder.getNumberOfSquares());
		assertEquals(builder.getResult().getAllGridCoordinates().size(),
				builder.getNumberOfSquares());
		numberOfSquares++;
		
		builder.addWall(Coordinate.random(100, 100));
		assertEquals(numberOfSquares, builder.getNumberOfSquares());
		assertEquals(builder.getResult().getAllGridCoordinates().size() - 1,
				builder.getNumberOfSquares());
	}
	
	@Test
	public void testGetAllReachableNeighboursOf() {
		DeterministicGridBuilderDirector director = new DeterministicGridBuilderDirector(builder,
				false);
		director.construct();
		
		for (int i = 0; i < 100; i++) {
			Coordinate squarePosition = director.getRandomCoordinateOnTestGrid();
			SquareContainer square = (SquareContainer) builder.getResult().getSquareAt(squarePosition);
			
			// calculate reachable neigbours of square
			List<Coordinate> neigbours = new ArrayList<Coordinate>();
			for (Direction dir : Direction.values()) {
				if (square.getNeighbourIn(dir) != null && square.getNeighbourIn(dir).canAddPlayer())
					neigbours.add(squarePosition.getCoordinateInDirection(dir));
			}
			
			assertArrayEquals(neigbours.toArray(),
					builder.getAllReachableNeighboursOf(squarePosition).toArray());
		}
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testNullInputSquare() {
		builder.addSquare(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testNullInputPlayerStartingPosition() {
		builder.addPlayerStartingPosition(null, 1);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testNullInputWall() {
		builder.addWall(null);
	}
	
	/* ############## LG ############### */
	
	//TODO inactive
	
	@Test(expected = GridBuildException.class)
	public void testNullInputLG() {
		builder.placeLightGrenade(null);
	}
	
	@Test(expected = GridBuildException.class)
	public void testLGPositionNoSquare() {
		builder.placeLightGrenade(Coordinate.random(100, 100));
	}
	
	@Test(expected = GridBuildException.class)
	public void testLGPositionWall() {
		Coordinate c = Coordinate.random(100, 100);
		builder.addWall(c);
		builder.placeLightGrenade(c);
	}
	
	@Test(expected = GridBuildException.class)
	public void testLGPlayerPosition() {
		Coordinate c = Coordinate.random(100, 100);
		builder.addPlayerStartingPosition(c, 1);
		builder.placeLightGrenade(c);
	}
	
	/* ############## ID disk ################# */
	
	@Test(expected = GridBuildException.class)
	public void testNullInputID() {
		builder.placeUnchargedIdentityDisc(null);
	}
	
	@Test(expected = GridBuildException.class)
	public void testIDPositionNoSquare() {
		builder.placeUnchargedIdentityDisc(Coordinate.random(100, 100));
	}
	
	@Test(expected = GridBuildException.class)
	public void testIDPositionWall() {
		Coordinate c = Coordinate.random(100, 100);
		builder.addWall(c);
		builder.placeUnchargedIdentityDisc(c);
	}
	
	@Test(expected = GridBuildException.class)
	public void testIDPlayerPosition() {
		Coordinate c = Coordinate.random(100, 100);
		builder.addPlayerStartingPosition(c,1);
		builder.placeUnchargedIdentityDisc(c);
	}
	
	/* ############## CID disk ################# */
	
	@Test(expected = GridBuildException.class)
	public void testNullInputCID() {
		builder.placeChargedIdentityDisc(null);
	}
	
	@Test(expected = GridBuildException.class)
	public void testCIDPositionNoSquare() {
		builder.placeChargedIdentityDisc(Coordinate.random(100, 100));
	}
	
	@Test(expected = GridBuildException.class)
	public void testCIDPositionWall() {
		Coordinate c = Coordinate.random(100, 100);
		builder.addWall(c);
		builder.placeChargedIdentityDisc(c);
	}
	
	@Test(expected = GridBuildException.class)
	public void testCIDPlayerPosition() {
		Coordinate c = Coordinate.random(100, 100);
		builder.addPlayerStartingPosition(c,1);
		builder.placeChargedIdentityDisc(c);
	}
	
	/* ############## teleporter ################# */
	
	@Test(expected = GridBuildException.class)
	public void testNullInputTeleporter() {
		builder.placeTeleporter(null, null);
	}
	
	@Test(expected = GridBuildException.class)
	public void testTeleporterPositionNoSquare() {
		builder.placeTeleporter(Coordinate.random(100, 100), Coordinate.random(100, 100));
	}
	
	@Test(expected = GridBuildException.class)
	public void testTeleporterPositionWall() {
		Coordinate c = Coordinate.random(100, 100);
		builder.addWall(c);
		builder.placeTeleporter(c, Coordinate.random(100, 100));
	}
	
	@Test(expected = GridBuildException.class)
	public void testTeleporterPlayerPosition() {
		Coordinate c = Coordinate.random(100, 100);
		builder.addPlayerStartingPosition(c,1);
		builder.placeTeleporter(c, Coordinate.random(100, 100));
	}
	
}
