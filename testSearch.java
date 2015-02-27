package ce2;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestSearch {
	
	@Test
	public void test() {
		String[] name = {"tasklist.txt"};
		TextBuddy tb = TextBuddy.setUp(name);
		
		String output = null;
		output = tb.executeCommand("search say");
		assertEquals("1.say hi!\n2.say goodbye!\n", output);
		
		tb.executeCommand("add meeting at school");
		tb.executeCommand("add play basketball");
		tb.executeCommand("add meeting at Starbucks");
		tb.executeCommand("add meeting at home");
		tb.executeCommand("add play soccor");
		tb.executeCommand("add play badminton");
		
		output = tb.executeCommand("search meeting");
		assertEquals("1.meeting at school\n"
				+ "2.meeting at Starbucks\n"
				+ "3.meeting at home\n", output);
		output = tb.executeCommand("search play");
		assertEquals("1.play basketball\n"
				+ "2.play soccor\n"
				+ "3.play badminton\n", output);
		
	}

}
