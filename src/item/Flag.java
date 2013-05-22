package item;

import square.AbstractSquare;
import square.SquareContainer;

public class Flag extends Item {
	
	private AbstractSquare	home;
	
	public Flag(AbstractSquare homeCoordinate) {
		this.home = homeCoordinate;
	}
	
	@Override
	public void use(SquareContainer square) {
		this.teleportBack();
	}
	
	@Override
	public boolean isCarriable() {
		return true;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private void teleportBack() {
		this.home.addItem(this);
	}
	
}
