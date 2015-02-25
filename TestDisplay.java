package ce2;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestDisplay {

	@Test
	public void test() {
		String[] name = {"tasklist.txt"};
		TextBuddy tb = TextBuddy.setUp(name);
		
		String output = null;
		output = tb.executeCommand("display");
		assertEquals("1.say hi!\n2.say goodbye!\n3.see you again\n", output);
	}

}
