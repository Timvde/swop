package powerfailure;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import player.TurnEvent;
import scenariotests.SetupTestGrid;
import square.Direction;
import square.NormalSquare;
import square.SquareContainer;
import effects.PowerFailureEffect;
import effects.RaceEffectFactory;

@SuppressWarnings("javadoc")
public class PowerFailureTest {
	
	@Test
	public final void testPowerFailure() {
		Map<Direction, SquareContainer> neighbours = new HashMap<Direction, SquareContainer>();
		
		SquareContainer sq1 = new SquareContainer(neighbours, new NormalSquare());
		SquareContainer sq2 = new SquareContainer(neighbours, new NormalSquare());
		SquareContainer sq3 = new SquareContainer(neighbours, new NormalSquare());
		SquareContainer sq4 = new SquareContainer(neighbours, new NormalSquare());
		SquareContainer sq5 = new SquareContainer(neighbours, new NormalSquare());
		SquareContainer sq6 = new SquareContainer(neighbours, new NormalSquare());
		SquareContainer sq7 = new SquareContainer(neighbours, new NormalSquare());
		SquareContainer sq8 = new SquareContainer(neighbours, new NormalSquare());
		
		neighbours.put(Direction.NORTH, sq1);
		neighbours.put(Direction.NORTHEAST, sq2);
		neighbours.put(Direction.EAST, sq3);
		neighbours.put(Direction.SOUTHEAST, sq4);
		neighbours.put(Direction.SOUTH, sq5);
		neighbours.put(Direction.SOUTHWEST, sq6);
		neighbours.put(Direction.WEST, sq7);
		neighbours.put(Direction.NORTHWEST, sq8);
		
		SquareContainer sq = new SquareContainer(neighbours, new NormalSquare());
		
		PrimaryPowerFailure pf = new PrimaryPowerFailure(sq, new RaceEffectFactory());
		assertEquals(true, sq.hasPowerFailure());
		
		pf.updateStatus(TurnEvent.END_ACTION);
		pf.updateStatus(TurnEvent.END_ACTION);
	}
	
	@Test
	public final void testDecreaseTimeToLive() {
		Map<Direction, SquareContainer> neighbours = new HashMap<Direction, SquareContainer>();
		SquareContainer sq = new SquareContainer(neighbours, new NormalSquare());
		
		PrimaryPowerFailure pf = new PrimaryPowerFailure(sq, new RaceEffectFactory());
		assertTrue(sq.hasPowerFailure());
		
		pf.updateStatus(TurnEvent.END_TURN);
		assertTrue(sq.hasPowerFailure());
		
		pf.updateStatus(TurnEvent.END_TURN);
		assertFalse(sq.hasPowerFailure());		
	}
	
	@Test 
	public final void testGetEffect() {
		Map<Direction, SquareContainer> neighbours = new HashMap<Direction, SquareContainer>();
		SquareContainer sq = new SquareContainer(neighbours, new NormalSquare());
		
		PrimaryPowerFailure pf = new PrimaryPowerFailure(sq, new RaceEffectFactory());
		assertEquals(PowerFailureEffect.class, pf.getEffect().getClass());
	}
}
