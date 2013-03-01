package actions;

import grid.Grid;


public abstract class GridAction extends Action {
	
	private Grid grid;

	Grid getGrid() {
		return grid;
	}

	public void setGrid(Grid grid) {
		this.grid = grid;
	}
}
