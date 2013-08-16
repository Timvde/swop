package grid;

import java.util.Iterator;
import square.SquareContainer;

/**
 * This class represents an iterator to iterate over the squares of a grid.
 *
 */
public class GridIterator implements Iterator<SquareContainer> {
	
	Iterator<SquareContainer>	iterator;
	
	@SuppressWarnings("javadoc")
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
