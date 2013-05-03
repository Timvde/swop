package square;

import static org.junit.Assert.*;
import item.lightgrenade.DummyLightGrenade;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import player.DummyPlayer;

@SuppressWarnings("javadoc")
public class PowerFailureTest {
	
	private PrimaryPowerFailure	powerFailure;

	@Before
	public void setUp() throws Exception {
		powerFailure = new PrimaryPowerFailure(new Square(Collections.<Direction, ASquare> emptyMap()));
	}
	
	@Test
	public final void testPowerFailure() {
		Map<Direction, ASquare> neighbours = new HashMap<Direction, ASquare>();
		Square sq = new Square(neighbours);
		neighbours.put(Direction.SOUTH, sq);
		Square sq1 = new Square(neighbours);
		neighbours.put(Direction.NORTH, sq);
		neighbours.remove(Direction.SOUTH);
		Square sq2 = new Square(neighbours);
		neighbours.put(Direction.WEST, sq);
		neighbours.remove(Direction.NORTH);
		Square sq3 = new Square(neighbours);
		
		PrimaryPowerFailure pf = new PrimaryPowerFailure(sq);
		assertEquals(pf, sq.getPowerFailure());
		assertEquals(pf, sq1.getPowerFailure());
		assertEquals(pf, sq2.getPowerFailure());
		assertEquals(pf, sq3.getPowerFailure());
	}
	
	@Test
	public final void testDecreaseTimeToLive() {
		Map<Direction, ASquare> neighbours = new HashMap<Direction, ASquare>();
		Square sq = new Square(neighbours);
		neighbours.put(Direction.SOUTH, sq);
		Square sq1 = new Square(neighbours);
		neighbours.put(Direction.NORTH, sq);
		neighbours.remove(Direction.SOUTH);
		Square sq2 = new Square(neighbours);
		neighbours.put(Direction.WEST, sq);
		neighbours.remove(Direction.NORTH);
		Square sq3 = new Square(neighbours);
		
		PrimaryPowerFailure pf = new PrimaryPowerFailure(sq);
		
		pf.decreaseTimeToLive();
		pf.decreaseTimeToLive();
		
		assertEquals(pf, sq.getPowerFailure());
		assertEquals(pf, sq1.getPowerFailure());
		assertEquals(pf, sq2.getPowerFailure());
		assertEquals(pf, sq3.getPowerFailure());
		
		pf.decreaseTimeToLive();
		assertNull(sq.getPowerFailure());
		assertNull(sq1.getPowerFailure());
		assertNull(sq2.getPowerFailure());
		assertNull(sq3.getPowerFailure());
		
		pf.decreaseTimeToLive();
	}
	
	@Test
	public final void testModify() {
		DummyLightGrenade lightGrenade = new DummyLightGrenade();
		// modify an item
		powerFailure.modify(lightGrenade);
		
		// test whether the strength has increased
		assertTrue(lightGrenade.isStrengthIncreased());
	}
	
	@Test
	public final void testExecute() {
		DummyPlayer player = new DummyPlayer();
		
		// inflict damage
		powerFailure.execute(player);
		
		assertTrue(player.isDamagedByPowerFailure());
	}
	
	@Test
	public final void testExecute_alreadyModifiedAnItem() {
		DummyPlayer player = new DummyPlayer();
		DummyLightGrenade lightGrenade = new DummyLightGrenade();
		
		powerFailure.modify(lightGrenade);
		powerFailure.execute(player);
		
		assertTrue(lightGrenade.isStrengthIncreased());
		assertFalse(player.isDamagedByPowerFailure());
	}
	
}
