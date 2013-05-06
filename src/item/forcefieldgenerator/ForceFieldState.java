package item.forcefieldgenerator;

/**
 * This enum represents the state of a force field. It can either be active or
 * inactive.
 */
public enum ForceFieldState {
	/**
	 * The force field is active and exhibits its effects
	 */
	ACTIVE {
		public ForceFieldState switchState() {
			return INACTIVE;
		}
	},
	/**
	 * The force field is inactive and acts transparent.
	 */
	INACTIVE {
		public ForceFieldState switchState() {
			return ACTIVE;
		}
	};
	
	/**
	 * Returns the state after switching
	 * 
	 * @return The state after switching
	 */
	public ForceFieldState switchState() {
		return this.switchState();
	}
}
