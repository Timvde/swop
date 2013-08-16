package powerfailure;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import effects.RaceEffectFactory;
import player.TurnEvent;
import square.Direction;
import square.NormalSquare;
import square.Property;
import square.SquareContainer;

@SuppressWarnings("javadoc")
public class PrimaryPowerFailureTest {
	
	private SquareContainer		sq;
	private PrimaryPowerFailure	pf;
	
	@Before
	public void setUp() throws Exception {
		Map<Direction, SquareContainer> neighbours = new HashMap<Direction, SquareContainer>();
		sq = new SquareContainer(neighbours, new NormalSquare());
		pf = new PrimaryPowerFailure(sq, new RaceEffectFactory());
	}
	
	private boolean hasPowerFailure(SquareContainer square) {
		for (Property property : square.getProperties())
			if (property instanceof PowerFailure)
				return true;
		return false;
	}
	
	@Test(expected = IllegalArgumentException.class)
	public final void testConstructor_nullArgument() {
		new PrimaryPowerFailure(null, new RaceEffectFactory());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public final void testConstructor_nullArgument2() {
		new PrimaryPowerFailure(sq, null);
	}
	
	@Test
	public final void testConstructor() {
		assertEquals(sq, pf.getSquare());
		assertTrue(hasPowerFailure(sq));
		assertEquals(2, pf.getTimeToLive());
	}
	
	@Test
	public final void testUpdateStatus() {
		assertEquals(sq, pf.getSquare());
		pf.updateStatus(TurnEvent.END_ACTION);
		assertTrue(hasPowerFailure(sq));
		
		pf.updateStatus(TurnEvent.END_TURN);
		assertTrue(hasPowerFailure(sq));
		
		pf.updateStatus(TurnEvent.END_TURN);
		assertEquals(0, pf.getTimeToLive());
		assertFalse(hasPowerFailure(sq));
	}
	
}
