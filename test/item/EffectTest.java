package item;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import player.DummyPlayer;
import effects.Effect;

@SuppressWarnings("javadoc")
public class EffectTest {
	
	private Effect		effect;
	private DummyPlayer	player;
	
	@Before
	public void setUp() {
		player = new DummyPlayer();
		effect = new DummyEffect();
	}
	
	@Test
	public void testEffectChain() {
		List<Effect> list = new ArrayList<Effect>();
		list.add(effect);
		for (int i = 0; i < 5; i++) {
			Effect newEffect = new DummyEffect();
			effect.addEffect(newEffect);
			list.add(newEffect);
		}
		effect.execute(player);
		for (int i = 0; i < 5; i++) {
			assertTrue(((DummyEffect) list.get(i)).isExecuted());
			if (i != 0)
				assertTrue(((DummyEffect) list.get(i)).isModified());
			else
				assertFalse(((DummyEffect) list.get(i)).isModified());
		}
		
	}
	
}
