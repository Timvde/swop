package item;

import square.TronObject;

/**
 * An empty effect which does nothing ...
 * 
 */
public class EmptyEffect extends AbstractEffect {
	
	@Override
	public void execute(TronObject object) {
		
		if (next != null)
			super.next.execute(object);
		
	}
	
}
