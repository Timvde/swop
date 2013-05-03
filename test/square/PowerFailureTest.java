package square;

import static org.junit.Assert.*;
import item.Effect;
import item.lightgrenade.LightGrenade;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import player.DummyPlayer;
import player.TurnEvent;

@SuppressWarnings("javadoc")
public class PowerFailureTest {
	
	private static final int	INCREASED_DAMAGE	= 4;
	
	@Test
	public final void testPowerFailure() {
		Map<Direction, ASquare> neighbours = new HashMap<Direction, ASquare>();
		
		Square sq1 = new Square(Collections.<Direction, ASquare> emptyMap());
		Square sq2 = new Square(Collections.<Direction, ASquare> emptyMap());
		Square sq3 = new Square(Collections.<Direction, ASquare> emptyMap());
		Square sq4 = new Square(Collections.<Direction, ASquare> emptyMap());
		Square sq5 = new Square(Collections.<Direction, ASquare> emptyMap());
		Square sq6 = new Square(Collections.<Direction, ASquare> emptyMap());
		Square sq7 = new Square(Collections.<Direction, ASquare> emptyMap());
		Square sq8 = new Square(Collections.<Direction, ASquare> emptyMap());
		
		neighbours.put(Direction.NORTH, sq1);
		neighbours.put(Direction.NORTHEAST, sq2);
		neighbours.put(Direction.EAST, sq3);
		neighbours.put(Direction.SOUTHEAST, sq4);
		neighbours.put(Direction.SOUTH, sq5);
		neighbours.put(Direction.SOUTHWEST, sq6);
		neighbours.put(Direction.WEST, sq7);
		neighbours.put(Direction.NORTHWEST, sq8);
		
		Square sq = new Square(neighbours);
		
		PrimaryPowerFailure pf = new PrimaryPowerFailure(sq);
		assertEquals(pf, sq.getPowerFailure());
		
		pf.updateStatus(TurnEvent.END_ACTION);
		pf.updateStatus(TurnEvent.END_ACTION);
	}
	
	@Test
	public final void testDecreaseTimeToLive() {
		Map<Direction, ASquare> neighbours = new HashMap<Direction, ASquare>();
		Square sq = new Square(neighbours);
		
		PrimaryPowerFailure pf = new PrimaryPowerFailure(sq);
		
		pf.updateStatus(TurnEvent.END_TURN);
		
		assertEquals(pf, sq.getPowerFailure());
		pf.updateStatus(TurnEvent.END_TURN);
		
		assertEquals(pf, sq.getPowerFailure());
		
		pf.updateStatus(TurnEvent.END_TURN);
		
		assertNull(sq.getPowerFailure());
	}
	
	@Test
	public final void testExecute_alreadyModifiedAnItem() {
		Square emptySquare = new Square(Collections.<Direction, ASquare> emptyMap());
		PrimaryPowerFailure powerFailure = new PrimaryPowerFailure(emptySquare);
		DummyPlayer player = new DummyPlayer();
		LightGrenade lightGrenade = new LightGrenade();
		lightGrenade.use(emptySquare);
		Effect effect = powerFailure.getEffect();
		effect.addEffect(lightGrenade.getEffect());
		effect.execute(player);
		
		assertFalse(player.isDamagedByPowerFailure());
		assertEquals(INCREASED_DAMAGE, player.getNumberOfActionsSkipped());
	}
}
