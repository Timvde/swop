package item;

import item.lightgrenade.LightGrenade;
import square.TronObject;
import effects.AbstractEffect;
import effects.Effect;


@SuppressWarnings("javadoc")
public class DummyEffect extends AbstractEffect {
	private boolean isExecuted = false;
	private boolean	isModified = false;
	
	@Override
	public void execute(TronObject object) {
		if (object instanceof LightGrenade)
			((LightGrenade) object).getEffect().increaseStrength();
		isExecuted = true;
		super.execute(object);
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
