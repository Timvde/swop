package item.lightgrenade;

import properties.powerfailure.PowerFailure;
import item.lightgrenade.LightGrenade;

/**
 * A dummy implementation of {@link LightGrenade}. This implementation of this light grenade
 * focuses on the testing of the {@link PowerFailure} class. The method
 * {@link LightGrenade#increaseStrength()} is overwritten for testing purposes.
 * 
 */
public class DummyLightGrenade extends LightGrenade {

	private boolean strengthIncreased;
	
	/**
	 * create a new dummy light grenade
	 */
	public DummyLightGrenade() {
		strengthIncreased = false;
	}
	
	@Override
	public void increaseStrength() {
		strengthIncreased = true;
		super.increaseStrength();
	}

	/**
	 * returns whether the strength of the light grenade has increased
	 * @return the strengthIncreased
	 */
	public boolean isStrengthIncreased() {
		return strengthIncreased;
	}
	
}
