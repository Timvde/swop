package actions;

import player.IPlayer;


public abstract class Action {
	
	private IPlayer player;

	IPlayer getPlayer() {
		return player;
	}

	public void setPlayer(IPlayer player) {
		this.player = player;
	}
	
	public abstract void execute();
}
