package grid.builder;

import static org.junit.Assert.assertEquals;
import grid.Grid;
import org.junit.Test;

@SuppressWarnings("javadoc")
public class DeterministicDirectorTest {
	
	@Test
	public void testDeterministicDirector() {
		TronGridBuilder builder = new TronGridBuilder();
		GridBuilderDirector director = new DeterministicGridBuilderDirector(builder, false);
		director.construct();
		Grid grid = builder.getResult();
				
		assertEquals(grid.toString(), "s s s s s s s u s s \n" + "s s s s s s s s s s \n"
				+ "s s s s s s s l s s \n" + "s s s s s s s s s s \n" + "s s s s s s s s s s \n"
				+ "s s s s w w w w w s \n" + "s s s s s s s l s s \n" + "s s l s s s s s l s \n"
				+ "s s s s s l l l l s \n" + "s s u s s s s s s s \n");
	}
}
