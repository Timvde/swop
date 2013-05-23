package powerfailure;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import player.TurnEvent;
import square.Direction;
import square.NormalSquare;
import square.SquareContainer;

@SuppressWarnings("javadoc")
public class SecondaryPowerFailureTest {
	
	private SquareContainer		sq;
	private PrimaryPowerFailure	pf;
	private ArrayList<SquareContainer> list;
	
	@Before
	public void setUp() throws Exception {
		Map<Direction, SquareContainer> neighbours = new HashMap<Direction, SquareContainer>();
		list = new ArrayList<SquareContainer>();
		sq = new SquareContainer(neighbours, new NormalSquare());
		
		for (Direction direction : Direction.values()) {
			HashMap<Direction, SquareContainer> squares = new HashMap<Direction, SquareContainer>();
			squares.put(direction, sq); 
			list.add(new SquareContainer(squares, new NormalSquare()));
		}
		
		pf = new PrimaryPowerFailure(sq);
	}
	
	@Test
	public final void testSecondaryPowerFailure() {
		SquareContainer pfSquare = null;
		
		for (SquareContainer square : list)
			if (square.hasPowerFailure())
				pfSquare = square;
		
		if (pfSquare == null)
			fail("no square was powerfailured");
	}
	
	@Test
	public final void testUpdateStatus() {
		int pfIndex = -1;
		for (SquareContainer square : list)
			if (square.hasPowerFailure())
				pfIndex = list.indexOf(square);
		
		if (pfIndex == -1)
			fail("no square was power failured");
		
		pf.updateStatus(TurnEvent.END_ACTION);
		pf.updateStatus(TurnEvent.END_ACTION);
		
		for (SquareContainer square : list)
			if (square.hasPowerFailure())
				assertEquals(1, Math.abs(pfIndex - list.indexOf(square)));
	}	
	
	@Test
	public final void testGetDirection() {
		fail("Not yet implemented");
	}
	
}
