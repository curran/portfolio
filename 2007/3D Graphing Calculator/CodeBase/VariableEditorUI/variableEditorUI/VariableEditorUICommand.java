package variableEditorUI;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import logEnabledComponents.LogEnabledJFrame;
import operators.UnaryOperator;
import operators.UnaryOperatorCreator;
import parser.ExpressionNode;
import parser.RecursiveDescentParser;
import parser.Value;
import valueTypes.CharacterSeparatedStatementPair;
import valueTypes.ErrorValue;
import valueTypes.StringValue;
import variables.Variable;

/**
 * A class which provides a static method which sets up the edit(var1, var2 ...)
 * command.
 * 
 * @author Curran Kelleher
 * 
 */
public class VariableEditorUICommand implements UnaryOperatorCreator {
	private VariableEditorUICommand() {
	}

	/**
	 * Adds the edit(var1, var2 ...) command to the specified parser. This
	 * command takes an arbitrary number of variables as arguments and pops up a
	 * frame which has in it real time graphical editors for all of the argument
	 * variables.
	 * 
	 * @param parser
	 *            The parser to add the edit(var1, var2 ...) command to as a
	 *            unary operator
	 */
	public static void setUpEditVariablesCommand(RecursiveDescentParser parser) {
		parser.addUnaryOperator("edit", new VariableEditorUICommand());
	}

	public UnaryOperator create(ExpressionNode child) {
		return new UnaryOperator(child) {
			public Value evaluate() {
				List<Variable> variables = new LinkedList<Variable>();
				if (child instanceof Variable) {
					variables.add((Variable) child);
				} else if (child instanceof CharacterSeparatedStatementPair) {
					CharacterSeparatedStatementPair listOfVariables = (CharacterSeparatedStatementPair) child;
					if (listOfVariables.getSymbol().equals(",")) {
						ExpressionNode[] allVariables = ((CharacterSeparatedStatementPair) child)
								.extractAllStatements();
						for (int i = 0; i < allVariables.length; i++)
							if (allVariables[i] instanceof Variable)
								variables.add((Variable) allVariables[i]);
							else
								return new ErrorValue(
										"edit(var1, var2, var3 ...) takes only variables as arguments. "
												+ "Argument number " + (i + 1)
												+ ", "
												+ allVariables[i].toString()
												+ ", is an invalid argument");
					} else
						return new ErrorValue(
								"edit(var1, var2, var3 ...) takes only variables separated by commas as arguments. "
										+ listOfVariables.getSymbol()
										+ " is an invalid argument separator");
				} else
					return new ErrorValue(
							"edit(var1, var2, var3 ...) takes only variables as arguments. "
									+ child.toString()
									+ " is an invalid argument");

				VariableEditorUI editorUI = new VariableEditorUI(variables);

				StringBuffer buff = new StringBuffer();
				for (Iterator<Variable> it = variables.iterator(); it.hasNext();)
					buff.append(it.next().toString()
							+ (it.hasNext() ? "_" : ""));
				JFrame frame = new LogEnabledJFrame(
						"VariableEditorFrameForVariables_" + buff.toString());
				frame.setTitle("Variable Editor");
				frame.setBounds(200, 200, 300,
						editorUI.getPreferredSize().height + 30);
				/*
				 * 30 is added because it is the approximate height taken up by
				 * the top bar of the frame
				 */
				frame.getContentPane().add(editorUI);
				frame
						.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				frame.setVisible(true);
				return new StringValue("Successfully created variable editor.");
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
