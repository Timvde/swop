package grid.builder;

import grid.builder.expressions.CombinedExpression;
import grid.builder.expressions.EmptyExpression;
import grid.builder.expressions.Expression;
import grid.builder.expressions.ForceFieldGeneratorExpression;
import grid.builder.expressions.IdentityDiskExpression;
import grid.builder.expressions.LightGrenadeExpression;
import grid.builder.expressions.SquareExpression;
import grid.builder.expressions.StartingSquareExpression;
import grid.builder.expressions.TeleporterExpression;
import grid.builder.expressions.WallExpression;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import ObjectronExceptions.builderExceptions.InvalidGridFileException;

/**
 * File parser to parse a file for the TRON game and return an according expression
 * Squares can be defined by one of the following chars: *,# and ' '. Each square can have 
 * several arguments. Arguments are separated by a : char. If the items require an additional argument
 * these can be added with a . separator. 
 *
 */
public class TronFileParser {
	
	private final File	file;
	private Scanner		scanner;
	
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
		scanner.useDelimiter("");
	}
	
	/**
	 * Returns whether the specified file, is valid and can be parsed.
	 * 
	 * @param file
	 *        the file to test
	 * @return true if the file can be parsed, else false
	 */
	public boolean isValidFile(File file) {
		if (file == null)
			return false;
		else
			return true;
	}
	
	public Expression nextExpression() {
		String statement = scanner.next("([ *#\\d])(?::(\\S+?(?=[ *#\\d])))?");
		Expression expression = null;
		if (statement == null || statement.length() <= 0)
			return expression;
		
		expression = getSquareExpression(statement.charAt(0));
		
		if (statement.length() == 1)
			return expression;
		else
			return new CombinedExpression(expression, getItemExpression(statement));
	}
	
	public boolean isAtEndOfLine() {
		return scanner.hasNext("\\n");
	}
	
	public boolean hasNextValidStatement() {
		return scanner.hasNext("([ \\d*#])(?::(\\S+?(?=[ \\d*#])))?");
	}
	
	/* The following could be place in a separate factory */
	
	private Expression getItemExpression(String statement) {
		for (String item : statement.split(":")) {
			switch (item.split(".")[0]) {
				case "LG":
					return new LightGrenadeExpression();
				case "CID":
					return new IdentityDiskExpression(true);
				case "UID":
					return new IdentityDiskExpression(false);
				case "T":
					return new TeleporterExpression(item);
				case "FFG":
					return new ForceFieldGeneratorExpression();
				default:
					break;
			}
		}
		
		return null;
	}
	
	private Expression getSquareExpression(char square) {
		if (Character.isDigit(square))
			return new StartingSquareExpression(Character.getNumericValue(square));
		
		switch (square) {
			case ' ':
				return new SquareExpression();
			case '#':
				return new WallExpression();
			case '*':
				return new EmptyExpression();
			default:
				throw new InvalidGridFileException("The char was not recognized!");
		}
	}
	
	public void readEndOfLine() {
		scanner.next("\\n");
	}
}
