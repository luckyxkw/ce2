package ce2;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestAdd {

	@Test
	public void test() {
		String[] name = {"tasklist.txt"};
		TextBuddy tb = TextBuddy.setUp(name);
		
		String output = null; 
		output = tb.executeCommand("clear");
		assertEquals("Your task list has been cleared!", output);
		output = tb.executeCommand("add say hi!");
		assertEquals("say hi! added into tasklist.txt", output);
		output = tb.executeCommand("add say goodbye!");
		assertEquals("say goodbye! added into tasklist.txt", output);
		output = tb.executeCommand("add see you again");
		assertEquals("see you again added into tasklist.txt", output);
		
	}

}
