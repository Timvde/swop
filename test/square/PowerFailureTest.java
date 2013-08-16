package square;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import item.lightgrenade.LightGrenade;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import player.DummyPlayer;
import player.TurnEvent;
import effects.RaceEffectFactory;
import effects.Effect;
import powerfailure.PowerFailure;
import powerfailure.PrimaryPowerFailure;

@SuppressWarnings("javadoc")
public class PowerFailureTest {
	
	private static final int	INCREASED_DAMAGE	= 4;
	
	@Test
	public final void testPowerFailure() {
		Map<Direction, SquareContainer> neighbours = new HashMap<Direction, SquareContainer>();
		
		Square sq1 = new SquareContainer(Collections.<Direction, SquareContainer> emptyMap(),
				new NormalSquare());
		Square sq2 = new SquareContainer(Collections.<Direction, SquareContainer> emptyMap(),
				new NormalSquare());
		Square sq3 = new SquareContainer(Collections.<Direction, SquareContainer> emptyMap(),
				new NormalSquare());
		Square sq4 = new SquareContainer(Collections.<Direction, SquareContainer> emptyMap(),
				new NormalSquare());
		Square sq5 = new SquareContainer(Collections.<Direction, SquareContainer> emptyMap(),
				new NormalSquare());
		Square sq6 = new SquareContainer(Collections.<Direction, SquareContainer> emptyMap(),
				new NormalSquare());
		Square sq7 = new SquareContainer(Collections.<Direction, SquareContainer> emptyMap(),
				new NormalSquare());
		Square sq8 = new SquareContainer(Collections.<Direction, SquareContainer> emptyMap(),
				new NormalSquare());
		
		neighbours.put(Direction.NORTH, (SquareContainer) sq1);
		neighbours.put(Direction.NORTHEAST, (SquareContainer) sq2);
		neighbours.put(Direction.EAST, (SquareContainer) sq3);
		neighbours.put(Direction.SOUTHEAST, (SquareContainer) sq4);
		neighbours.put(Direction.SOUTH, (SquareContainer) sq5);
		neighbours.put(Direction.SOUTHWEST, (SquareContainer) sq6);
		neighbours.put(Direction.WEST, (SquareContainer) sq7);
		neighbours.put(Direction.NORTHWEST, (SquareContainer) sq8);
		
		SquareContainer sq = new SquareContainer(neighbours, new NormalSquare());
		
		PrimaryPowerFailure pf = new PrimaryPowerFailure(sq, new RaceEffectFactory());
		assertEquals(pf, getPowerFailure(sq));
		
		pf.updateStatus(TurnEvent.END_ACTION);
		pf.updateStatus(TurnEvent.END_ACTION);
	}
	
	private PowerFailure getPowerFailure(SquareContainer square) {
		for (Property property : square.getProperties())
			if (property instanceof PowerFailure)
				return ((PowerFailure) property);
		
		return null;
	}
	
	@Test
	public final void testDecreaseTimeToLive() {
		Map<Direction, SquareContainer> neighbours = new HashMap<Direction, SquareContainer>();
		SquareContainer sq = new SquareContainer(neighbours, new NormalSquare());
		
		PrimaryPowerFailure pf = new PrimaryPowerFailure(sq, new RaceEffectFactory());
		
		pf.updateStatus(TurnEvent.END_TURN);
		
		assertEquals(pf, getPowerFailure(sq));
		pf.updateStatus(TurnEvent.END_TURN);
		
		assertNull(getPowerFailure(sq));
	}
	
	@Test
	public final void testExecute_alreadyModifiedAnItem() {
		SquareContainer emptySquare = new SquareContainer(
				Collections.<Direction, SquareContainer> emptyMap(), new NormalSquare());
		PrimaryPowerFailure powerFailure = new PrimaryPowerFailure(emptySquare, new RaceEffectFactory());
		DummyPlayer player = new DummyPlayer();
		LightGrenade lightGrenade = new LightGrenade(new RaceEffectFactory());
		lightGrenade.use(emptySquare, null);
		Effect effect = powerFailure.getEffect();
		effect.addEffect(lightGrenade.getEffect());
		effect.execute(player);
		
		assertFalse(player.isDamagedByPowerFailure());
		assertEquals(INCREASED_DAMAGE, player.getNumberOfActionsSkipped());
	}
}
