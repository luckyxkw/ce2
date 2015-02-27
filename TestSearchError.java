package ce2;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestSearchError {

	@Test
	public void test() {
		String[] name = {"tasklist.txt"};
		TextBuddy tb = TextBuddy.setUp(name);
		String output = null;
		
		tb.executeCommand("clear");
		tb.executeCommand("add wake up at 9 am");
		tb.executeCommand("add go to lecture at 10 am");
		tb.executeCommand("add have lunch at 12");
		tb.executeCommand("add project meeting at 2 pm");
		tb.executeCommand("add CCA meeting at 6 pm");
		tb.executeCommand("add project due 11 pm today");
		
		output = tb.executeCommand("search basketball");
		assertEquals("Sorry, no match task!", output);
		output = tb.executeCommand("search ");
		assertEquals(tb.executeCommand("display"), output);
		output = tb.executeCommand("search");
		assertEquals(tb.executeCommand("display"), output);
		
		tb.executeCommand("clear");
	}

}
