package ce2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Application name: TextBuddy
 * Version: 2.0
 * This application is meant to manage tasks of users. 
 * It will support add, delete, display, sort, exit, clear and drop methods right now.
 * All the above methods support slight flexibility with regard to user's instruction.
 * For unknown methods, it will print a MESSAGE and continue to listen to user's next instruction.
 * The application will rewrite the text file(recording the tasks), every time an add, delete or 
 * sort method is called, so that even if the system suddenly crash down, the user will not lost
 * any of his task.
 * @author Colonel
 *
 */
public class TextBuddy {
	private static final String MESSAGE_WELCOM = "Welcom for using TextBuddy!";
	private static final String MESSAGE_INPUT = ">>> ";
	private static final String MESSAGE_ADD = " added into ";
	private static final String MESSAGE_ADD_NULL = "You cannot add a null task";
	private static final String MESSAGE_DELETE = " removed from ";
	private static final String MESSAGE_DELETE_NULL = "Please specify which task you want to delete.";
	private static final String MESSAGE_EMPTY_LIST = "Your task list is empty";
	private static final String MESSAGE_NOTSUPPORTED = "Sorry, the function you are requesting is not currently supported.";
	private static final String MESSAGE_CLEAR = "Your task list has been cleared!";
	private static final String MESSAGE_DROP = "The current task list has been deleted!";
	private static final String MESSAGE_NOTASK = "Sorry, no such task!";
	
	private static final String[] KEYWORD_ADD = {"add", "insert"};
	private static final String[] KEYWORD_DELETE = {"delete", "remove", "rm"};
	private static final String[] KEYWORD_DISPLAY = {"display", "ls", "show"};
	private static final String[] KEYWORD_SORT = {"sort"};
	private static final String[] KEYWORD_EXIT = {"exit", "quit"};
	private static final String[] KEYWORD_CLEAR = {"clear", "clrscr"};
	private static final String[] KEYWORD_DROP = {"drop"};
	private static final String[] KEYWORD_SEARCH = {"search"};
	
	private static final int INDEX_ADD = 1;
	private static final int INDEX_DELETE = 2;
	private static final int INDEX_CLEAR = 3;
	private static final int INDEX_DROP = 4;
	private static final int INDEX_DISPLAY = 5;
	private static final int INDEX_EXIT = 6;
	private static final int INDEX_SORT = 7;
	private static final int INDEX_SEARCH = 8;
	
	private static final int BREAK_TRUE = 0;
	private static final int BREAK_FALSE = 1;
	
	private static final Scanner scan = new Scanner(System.in);
	
	public static void main(String args[]) {
		exitIfIllegalArgument(args);
		TextBuddy CurrentPool = setUpEnvironment(args);
		CurrentPool.addInSelectedFeatures();
		CurrentPool.solveUserRequest();
	}

	private static String fileName = null;
	private static ArrayList<String> buffer = null;
	private Hashtable<String, Integer> featureList = null;
	
	public TextBuddy() {
		buffer = new ArrayList<String>();
		featureList = new Hashtable<String, Integer>();
	}

	public static TextBuddy setUp(String[] name) {
		TextBuddy tb = setUpEnvironment(name);
		tb.addInSelectedFeatures();
		return tb;
	}
	public String executeCommand(String command) {
		String Command = getCommand(command);
		String Content = getContent(command);
		Integer CommandType = featureList.get(Command);
		if (isUnknown(CommandType)) {
			return MESSAGE_NOTSUPPORTED;
		}
		switch (CommandType) {
		case INDEX_ADD: 
			return add(Content);
		case INDEX_DELETE: 
			return delete(Content);
		case INDEX_DISPLAY: 
			return display();
		case INDEX_SORT: 
			return sort(Content);
		case INDEX_EXIT: 
			return null;
		case INDEX_CLEAR: 
			return clear();
		case INDEX_DROP:
			return drop();
		case INDEX_SEARCH:
			return search(Content);
		default: 
			break;
		}
		return null;
	}
	
	private void solveUserRequest() {
		printWelcomInfo();		
		executeApp();
	}
	
	private void printWelcomInfo() {
		showToUser(MESSAGE_WELCOM);
	}
	
	private void showToUser(String str) {
		System.out.println(str);
	}
	
	private void executeApp() {
		String currentCommand = null;
		while (true) {
			System.out.print(MESSAGE_INPUT);
			currentCommand = scan.nextLine();
			//Once accept break message, stop executing the application
			if (isTerminateCommand(currentCommand)) 
				break;
		}
	}	

	private boolean isTerminateCommand(String currentCommand) {
		return this.solve(currentCommand) == BREAK_TRUE;
	}
	/**
	 * Decide which features are implemented, convenient for adding or deleting features.
	 */
	private void addInSelectedFeatures() {
		addFeature(KEYWORD_ADD, INDEX_ADD);
		addFeature(KEYWORD_DELETE, INDEX_DELETE);
		addFeature(KEYWORD_DISPLAY, INDEX_DISPLAY);
		addFeature(KEYWORD_SORT, INDEX_SORT);
		addFeature(KEYWORD_EXIT, INDEX_EXIT);
		addFeature(KEYWORD_CLEAR, INDEX_CLEAR);
		addFeature(KEYWORD_DROP, INDEX_DROP);
		addFeature(KEYWORD_SEARCH, INDEX_SEARCH);
	}
	/**
	 * Determine which method the user is calling and apply appropriate method
	 */
	private int solve(String currentCommand) {
		//Separate the command field and the content field  
		String Command = getCommand(currentCommand);
		String Content = getContent(currentCommand);
		Integer CommandType = featureList.get(Command);
		if (isUnknown(CommandType)) {
			methodNotSupported();
			return BREAK_FALSE;
		}
		switch (CommandType) {
		case INDEX_ADD: 
			showToUser(add(Content));
			break;
		case INDEX_DELETE: 
			showToUser(delete(Content));
			break;
		case INDEX_DISPLAY: 
			showToUser(display());
			break;
		case INDEX_SORT: 
			showToUser(sort(Content));
			break;
		case INDEX_EXIT: 
			return BREAK_TRUE;
		case INDEX_CLEAR: 
			showToUser(clear());
			break;
		case INDEX_DROP:
			showToUser(drop());
			return BREAK_TRUE;
		case INDEX_SEARCH:
			showToUser(search(Content));
			break;
		default: 
			break;
		}
		return BREAK_FALSE;
	}

	private boolean isUnknown(Integer commandType) {
		return commandType == null;
	}
	/**
	 * Remove the current task list from disk and exit
	 */
	private String drop() {
		File file = new File(fileName);
		file.delete();
		return MESSAGE_DROP; 
	}
	/**
	 * Delete all tasks from current task list
	 */
	private String clear() {
		try {
			buffer = new ArrayList<String>();
			BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
			bw.close();
			return MESSAGE_CLEAR;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * Copy the content in the buffer into the file
	 */
	private void updateFile() throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
		for (int i = 0; i < buffer.size(); i++) {
			bw.write(buffer.get(i));
			bw.newLine();
		}
		bw.close();
	}
	/**
	 * append a string to the end of a file
	 */
	private void appendFile(String str) {
		try {
			BufferedWriter bw;
			bw = new BufferedWriter(new FileWriter(fileName, true));
			bw.write(str);
			bw.newLine();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	/**
	 * Sort according to the content requires
	 * Now only support sort according to alphabet.
	 */
	private String sort(String content) {
		try {
			Collections.sort(buffer);
			updateFile();
			return display();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * Display all tasks with number labeled
	 */
	private String display() {
		if (buffer.size() == 0) {
			return MESSAGE_EMPTY_LIST;
		} else {
			String temp = "";
			for (int i = 0; i < buffer.size(); i++) {
				temp = temp + Integer.toString(i+1) + "." + buffer.get(i) + "\n";
			}
			return temp;
		}
	}
	/**
	 * Delete a certain task specified by the content
	 */
	private String delete(String content) {
		try {
			if (isLegalArgumentsDel(content)) {
				int index = getIndex(content);
				String TextRemoved = buffer.get(index);
				buffer.remove(index);
				updateFile();
				return TextRemoved + MESSAGE_DELETE + fileName;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static int getIndex(String content) {
		return Integer.parseInt(content) - 1;
	}
	
	private static boolean isNumeric(String content) { 
		Pattern pattern = Pattern.compile("[0-9]*"); 
		Matcher isNum = pattern.matcher(content);
		if (!isNum.matches()) {
			return false;
		}
		return true; 
	}
	
	private boolean isBounded(String content) {
		int index = getIndex(content);
		return index >= 0 && index < buffer.size();
	}
	
	private boolean isLegalArgumentsDel(String content) {
		if (content == null) {
			showToUser(MESSAGE_DELETE_NULL);
			return false;
		} else if (isNumeric(content) && isBounded(content)) {
			return true;
		} else {
			showToUser(MESSAGE_NOTASK);
			return false;
		}
	}
	/**
	 * Add a new task to the end of the task list
	 */
	private String add(String content) {
		if (isLegalArgumentsAdd(content)) {
			return MESSAGE_ADD_NULL;
		} else {
			buffer.add(content);
			appendFile(content);
			return content + MESSAGE_ADD + fileName;
		}
	}
	
	private String search(String content) {
		if (content == null) {
			return display();
		} else {
			String temp = "";
			int count = 1;
			for (int i = 0; i < buffer.size(); i++) {
				if (buffer.get(i).contains(content)) {
					temp = temp + Integer.toString(count) + "." + buffer.get(i) + "\n";
					count++;
				}
			}
			return temp;
		}
	}

	private boolean isLegalArgumentsAdd(String content) {
		return content == null || content.equals("");
	}

	private void methodNotSupported() {
		showToUser(MESSAGE_NOTSUPPORTED);
	}

	private String getContent(String currentCommand) {
		int FirstSpace = currentCommand.indexOf(" ");
		if (FirstSpace == -1) {
			return null;
		} else {
			return currentCommand.substring(FirstSpace + 1, currentCommand.length());
		}
	}

	private String getCommand(String currentCommand) {
		int FirstSpace = currentCommand.indexOf(" ");
		if (FirstSpace == -1) {
			return currentCommand;
		} else {
			return currentCommand.substring(0, FirstSpace);
		}
	}

	private void addFeature(String[] keyWordList, int index) {
		for (int i = 0; i < keyWordList.length; i ++) {
			featureList.put(keyWordList[i], index);
		}
	}

	private static TextBuddy setUpEnvironment(String[] args) {
		String name = args[0];
		TextBuddy tb = new TextBuddy();
		fileName = name;
		File file = new File(fileName);
		try {
			if (!file.exists() || !file.isFile()) {
				file.createNewFile();
			}
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			String temp = br.readLine();
			while (temp != null) {
				buffer.add(temp);
				temp = br.readLine();
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return tb;
	}

	private static void exitIfIllegalArgument(String[] args) {
		if (args.length != 1) {
			System.exit(-1);
		}
	}
}
