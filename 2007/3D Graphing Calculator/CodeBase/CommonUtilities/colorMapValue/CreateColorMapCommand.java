package colorMapValue;

import java.util.ArrayList;
import java.util.List;

import operators.UnaryOperator;
import operators.UnaryOperatorCreator;
import parser.ExpressionNode;
import parser.RecursiveDescentParser;
import parser.Value;
import valueTypes.CharacterSeparatedStatementPair;
import valueTypes.ColorValue;
import valueTypes.DecimalValue;
import valueTypes.ErrorValue;
import colorMap.ColorMap;
import colorMap.ColorNode;

/**
 * The class responsible for setting up the command which creates color maps.
 * 
 * @author Curran Kelleher
 * 
 */
public class CreateColorMapCommand implements UnaryOperatorCreator {

	/**
	 * The constructor is private because instances of this class are only
	 * created via the method CreateColorMapCommand.
	 * 
	 */
	private CreateColorMapCommand() {
	}

	/**
	 * Adds the createColorMap(color1, value1, color2, value2 ...) command to
	 * the specified parser. This command takes an arbitrary number of
	 * color-value pairs as arguments.
	 * 
	 * @param parser
	 *            The parser to add the createColorMap(color1, value1, color2,
	 *            value2 ...) command to as a unary operator
	 */
	public static void setUpCreateColorMapCommand(RecursiveDescentParser parser) {
		parser.addUnaryOperator("createColorMap", new CreateColorMapCommand());
	}

	public UnaryOperator create(ExpressionNode child) {
		return new UnaryOperator(child) {
			public Value evaluate() {
				List<Value> valuesFromArguments = new ArrayList<Value>();
				// used later in error messages
				String commandUseExample = "createColorMap(color1, value1, color2, value2 ...)";

				if (child instanceof CharacterSeparatedStatementPair) {
					CharacterSeparatedStatementPair listOfColorValuePairs = (CharacterSeparatedStatementPair) child;
					if (listOfColorValuePairs.getSymbol().equals(",")) {
						ExpressionNode[] allColorValuePairs = ((CharacterSeparatedStatementPair) child)
								.extractAllStatements();
						// add all of the values from the arguments for later
						// inspection
						for (int i = 0; i < allColorValuePairs.length; i++)
							valuesFromArguments.add(allColorValuePairs[i]
									.evaluate());

					} else
						return new ErrorValue(
								commandUseExample
										+ " takes only variables separated by commas as arguments. "
										+ listOfColorValuePairs.getSymbol()
										+ " is an invalid argument separator");
				} else
					return new ErrorValue(
							commandUseExample
									+ " takes only pairs of colors and decimal values as arguments. "
									+ child.toString()
									+ " is an invalid argument");

				// at this point we have extracted values from the arguments
				// into the list valuesFromArguments...
				List<ColorNode> colorNodes = new ArrayList<ColorNode>();
				for (int i = 0; i < valuesFromArguments.size() - 1; i += 2) {
					// detect color-value pairs
					if (valuesFromArguments.get(i) instanceof ColorValue
							&& valuesFromArguments.get(i + 1) instanceof DecimalValue) {
						colorNodes
								.add(new ColorNode(
										((ColorValue) valuesFromArguments
												.get(i)).value,
										((DecimalValue) valuesFromArguments
												.get(i + 1)).value));
					} else
						// if the values are not color-value pairs
						return new ErrorValue(
								commandUseExample
										+ " takes only pairs of colors and decimal values as arguments. The arguments are not arranged in color-value pairs.");
				}

				return new ColorMapValue(new ColorMap(colorNodes));
			}
		};
	}

	public int getType() {
		return UnaryOperatorCreator.COMMAND;
	}

	public String getDescription() {
		return "takes an arbitrary number of variables as arguments and pops up a frame which has in it real time graphical editors for all of the argument variables. For example \"edit(a,b,c)\"";
	}
}
