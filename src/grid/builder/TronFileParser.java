package grid.builder;

import grid.builder.expressions.EmptyExpression;
import grid.builder.expressions.Expression;
import grid.builder.expressions.SquareExpression;
import grid.builder.expressions.WallExpression;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import java.util.regex.Pattern;

public class TronFileParser {
	
	private final File	file;
	private Scanner	scanner;
	
	/**
	 * Create a new parser to parse a specified
	 * 
	 * @param file
	 *        the file describing a new game board
	 * @throws FileNotFoundException
	 *         if the file does not exist
	 */
	public TronFileParser(File file) throws FileNotFoundException {
		if (!isValidFile(file))
			throw new IllegalArgumentException();
		
		this.file = file;
		scanner = new Scanner(file);
	}
	
	/**
	 * Returns whether the specified file, is valid and can be parsed.
	 * 
	 * @param file
	 *        the file to test
	 * @return true if the file can be parsed, else false
	 */
	public boolean isValidFile(File file) {
		return true;
	}
	
	public String nextStatement() {
		return scanner.next(Pattern.compile("([ *#])(?::(\\S+))?"));
	}
	
	public Expression nextExpression() {
		String statement = nextStatement();
		Expression expression = null; 
		if (statement == null || statement.length() <= 0)
			return expression; 
		
		/* The following could be place in a separate factory */
		switch (statement.charAt(0)) {
			case ' ':
				expression = new SquareExpression();
				break;
			case '#': 
				expression = new WallExpression();
				break;
			case '*' :
				expression = new EmptyExpression();
				break;
			default:
				break;
		}
			
	}
}
