package tests;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ CoordinateTest.class, GridTest.class, WallTest.class })
    public class TestSuite {
	
	    @BeforeClass 
	    public static void setUpClass() {
	    }
	
	    @AfterClass
	    public static void tearDownClass() { 
	    }

}