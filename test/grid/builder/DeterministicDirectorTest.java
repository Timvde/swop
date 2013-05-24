package grid.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import item.IItem;
import item.teleporter.Teleporter;
import org.junit.Before;
import org.junit.Test;
import square.AbstractSquare;
import square.PropertyType;
import effects.RaceEffectFactory;
import grid.Coordinate;
import grid.Grid;

@SuppressWarnings("javadoc")
public class DeterministicDirectorTest {
	
	private TronGridBuilder	builder;
	
	@Before
	public void setUp() {
		builder = new TronGridBuilder(new RaceEffectFactory());
		GridBuilderDirector director = new DeterministicGridBuilderDirector(builder, false);
		director.construct();
	}
	
	@Test
	public void testDeterministicNoPowerFailures() {
		Grid grid = builder.getResult();
		
		assertEquals(grid.toString(), "s s s s s s s u s s \n" + "s s s s s s s s s s \n"
				+ "s s s s s s s l s t \n" + "s s s s s s s s s s \n" + "s s s s s s s s s s \n"
				+ "s s s s w w w w w s \n" + "s s s s s s s l s s \n" + "t s l s s s s s l s \n"
				+ "s s s s s l l l l s \n" + "s s u s s s s s s s \n");
	}
	
	public void testDeterministicPowerFailures() {
		fail("not yet implemented");
	}
	
	@Test
	public void testDeterministicPlayerPositions() {
		Grid grid = builder.getResult();
		
		assertTrue(grid.getSquareAt(DeterministicGridBuilderDirector.PLAYER1_START_POS)
				.hasProperty(PropertyType.STARTING_POSITION));
		assertTrue(grid.getSquareAt(DeterministicGridBuilderDirector.PLAYER2_START_POS)
				.hasProperty(PropertyType.STARTING_POSITION));
	}
	
	@Test
	public void testDeterministicTeleporters() {
		Teleporter teleporter1 = getTeleporterAtLocation(new Coordinate(9, 2));
		Teleporter teleporter2 = getTeleporterAtLocation(new Coordinate(0, 7));
		
		assertNotNull(teleporter1);
		assertNotNull(teleporter2);
		
		assertEquals(teleporter2, teleporter1.getDestination());
		assertEquals(teleporter1, teleporter2.getDestination());
	}
	
	private Teleporter getTeleporterAtLocation(Coordinate coordinate) {
		AbstractSquare teleporterLocation1 = builder.getResult().getSquareAt(coordinate);
		
		for (IItem item : teleporterLocation1.getAllItems()) {
			if (item instanceof Teleporter) {
				return (Teleporter) item;
			}
		}
		return null;
	}
}
