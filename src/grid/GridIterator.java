package grid;

import java.util.Iterator;
import square.SquareContainer;

public class GridIterator implements Iterator<SquareContainer> {
	
	Iterator<SquareContainer>	iterator;
	
	public GridIterator(Iterator<SquareContainer> iterator) {
		if (iterator == null)
			throw new IllegalArgumentException("The specified iterator cannot be null");
		
		this.iterator = iterator;
	}
	
	@Override
	public boolean hasNext() {
		return this.iterator.hasNext();
	}
	
	@Override
	public SquareContainer next() {
		return this.iterator.next();
	}
	
	@Override
	public void remove() {
		throw new UnsupportedOperationException(
				"You are not allowed to remove squares from the grid");
	}
	
}
