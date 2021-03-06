package item;

import item.lightgrenade.LightGrenade;
import square.TronObject;
import effects.AbstractEffect;
import effects.Effect;
import effects.ExplodeEffect;


@SuppressWarnings("javadoc")
public class DummyEffect extends AbstractEffect {
	private boolean isExecuted = false;
	private boolean	isModified = false;
	
	@Override
	public void preExecutionHook(TronObject object) {
		if (object instanceof LightGrenade)
			((ExplodeEffect) ((LightGrenade) object).getEffect()).increaseStrength();
		isExecuted = true;
	}
	
	@Override
	public void addEffect(Effect effect) {
		if (effect instanceof DummyEffect)
			((DummyEffect) effect).modify();
		super.addEffect(effect);
	}

	private void modify() {
		isModified  = true;
	}
	
	public boolean isExecuted() {
		return isExecuted;
	}
	
	public boolean isModified() {
		return isModified;
	}
	
}
