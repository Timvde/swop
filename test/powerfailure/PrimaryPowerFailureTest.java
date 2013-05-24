package powerfailure;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import player.TurnEvent;
import square.Direction;
import square.NormalSquare;
import square.SquareContainer;

@SuppressWarnings("javadoc")
public class PrimaryPowerFailureTest {
	
	private SquareContainer		sq;
	private PrimaryPowerFailure	pf;
	
	@Before
	public void setUp() throws Exception {
		Map<Direction, SquareContainer> neighbours = new HashMap<Direction, SquareContainer>();
		sq = new SquareContainer(neighbours, new NormalSquare());
		pf = new PrimaryPowerFailure(sq);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public final void testConstructor_nullArgument() {
		new PrimaryPowerFailure(null);
	}
	
	@Test
	public final void testConstructor() {
		assertEquals(sq, pf.getSquare());
		assertTrue(sq.hasPowerFailure());
		assertEquals(2, pf.getTimeToLive());
	}
	
	@Test
	public final void testUpdateStatus() {
		assertEquals(sq, pf.getSquare());
		pf.updateStatus(TurnEvent.END_ACTION);
		assertTrue(sq.hasPowerFailure());
		
		pf.updateStatus(TurnEvent.END_TURN);
		assertTrue(sq.hasPowerFailure());
		
		pf.updateStatus(TurnEvent.END_TURN);
		assertEquals(0, pf.getTimeToLive());
		assertFalse(sq.hasPowerFailure());
	}
	
}
