package item;

import ObjectronExceptions.IllegalGrenadeStateTranisitionException;

import com.sun.istack.internal.NotNull;

public class LightGrenade extends Item implements ILightGrenade{

	@NotNull
	private LightGrenadeState state = LightGrenadeState.INACTIVE; // initial state

	@Override
	public LightGrenadeState getState() {
		return this.state;
	}

	public void explode() throws IllegalGrenadeStateTranisitionException {
		if (!this.state.isAllowedTransistionTo(LightGrenadeState.EXPLODED))
			throw new IllegalGrenadeStateTranisitionException();
		this.state = LightGrenadeState.EXPLODED;
	}

	public void enable() throws IllegalGrenadeStateTranisitionException {
		if (!this.state.isAllowedTransistionTo(LightGrenadeState.ACTIVE))
			throw new IllegalGrenadeStateTranisitionException();
		this.state = LightGrenadeState.ACTIVE;
	}

	public void use() throws IllegalGrenadeStateTranisitionException {
		if (!this.state.isAllowedTransistionTo(LightGrenadeState.WAITING_FOR_PLAYER_LEAVE))
			throw new IllegalGrenadeStateTranisitionException();
		this.state = LightGrenadeState.WAITING_FOR_PLAYER_LEAVE;
	}
	
	@Override
	public boolean isCarriable() {
		// exploded grenades cannot be carried
		return this.state != LightGrenadeState.EXPLODED;
	}

}
