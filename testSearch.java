package ce2;

import static org.junit.Assert.*;
import org.junit.Test;

public class testSearch {

	public void test() {
		TextBuddy tb = new TextBuddy();
		String output = tb.executeCommand("clear");
		assertEquals("Your task list is empty", output);
	}

}
