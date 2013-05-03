package parser;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import operators.BinaryOperatorCreator;
import operators.UnaryOperatorCreator;
import variables.Constant;
import variables.Variable;

/**
 * A utilities class which extracts and summarizes information from the parser.
 * 
 * @author Curran Kelleher
 * 
 */
public class ParserUtilities {

	/**
	 * Extracts all Variables from the current symbol table, and
	 * assembles a String which describes them. This String includes variable
	 * names and descriptions of each Observer which has been added to it.
	 * 
	 * @return a description of all Variables.
	 */
	public static String getObservedVariablesAndDescriptions() {

		StringBuffer variablesStringBuffer = new StringBuffer();
		Map<String, Variable> symbolTable = Variable.getSymbolTable();
		List<Variable> variables = new LinkedList<Variable>();
		for (Iterator<Entry<String, Variable>> it = symbolTable.entrySet()
				.iterator(); it.hasNext();) {
			Variable currentVariable = it.next().getValue();
			if (currentVariable.hasObservers())
				variables.add((Variable) currentVariable);
		}

		for (Iterator<Variable> it = variables.iterator(); it
				.hasNext();) {
			Variable currentVariable = it.next();
			variablesStringBuffer.append(currentVariable.toString()
					+ "\t- ");

			for (Iterator<String> i = currentVariable
					.getObserverExplainations().iterator(); i.hasNext();)
				// append the descriptions to the StringBuffer
				variablesStringBuffer.append(i.next() + " ");

			if (it.hasNext())
				variablesStringBuffer.append("\n");
		}
		return variablesStringBuffer.toString();
	}

	/**
	 * Extracts all constants from the current symbol table, and assembles a
	 * String which describes them. This String includes variable names and
	 * descriptions.
	 * 
	 * @return a description of all constants.
	 */
	public static String getConstantsAndDescriptions() {
		StringBuffer variablesStringBuffer = new StringBuffer();
		Map<String, Variable> symbolTable = Variable.getSymbolTable();
		List<Constant> variables = new LinkedList<Constant>();
		for (Iterator<Entry<String, Variable>> it = symbolTable.entrySet()
				.iterator(); it.hasNext();) {
			Variable currentVariable = it.next().getValue();
			if (currentVariable instanceof Constant)
				variables.add((Constant) currentVariable);
		}

		for (Iterator<Constant> it = variables.iterator(); it.hasNext();) {
			Constant currentConstant = it.next();
			variablesStringBuffer.append(currentConstant.toString() + "\t- "
					+ currentConstant.getDescription()
					+ (it.hasNext() ? "\n" : ""));
		}

		return variablesStringBuffer.toString();
	}

	/**
	 * Extracts descriptions of all unary operators of the specified type from
	 * the specified parser.
	 * 
	 * @return A String which summarizes all mathematical unary operators in the
	 *         specified parser.
	 * @param parserToExtractFrom
	 *            the parser to extract the descriptions from.
	 * @param type
	 *            the general category of the operator. It can be either
	 *            UnaryOperatorCreator.MATHEMATICAL,
	 *            UnaryOperatorCreator.LANGUAGE_CONSTRUCT,
	 *            UnaryOperatorCreator.COMMAND or
	 *            UnaryOperatorCreator.MISCELLANEOUS
	 * @return a description of all mathematical unary operators in the
	 *         parserToExtractFrom.
	 */
	public static String getUnopsAndDescriptions(
			RecursiveDescentParser parserToExtractFrom, int type) {
		StringBuffer stringBuffer = new StringBuffer();

		Map<String, UnaryOperatorCreator> allUnops = parserToExtractFrom.unaryOperators;

		List<Entry<String, UnaryOperatorCreator>> unops = new LinkedList<Entry<String, UnaryOperatorCreator>>();

		for (Iterator<Entry<String, UnaryOperatorCreator>> it = allUnops
				.entrySet().iterator(); it.hasNext();) {
			Entry<String, UnaryOperatorCreator> currentEntry = it.next();
			if (currentEntry.getValue().getType() == type)
				unops.add(currentEntry);
		}

		for (Iterator<Entry<String, UnaryOperatorCreator>> it = unops
				.iterator(); it.hasNext();) {
			Entry<String, UnaryOperatorCreator> currentEntry = it.next();
			stringBuffer.append(currentEntry.getKey() + "()\t- "
					+ currentEntry.getValue().getDescription()
					+ (it.hasNext() ? "\n" : ""));
		}

		return stringBuffer.toString();
	}

	/**
	 * Extracts descriptions of all binary operators from the specified parser.
	 * 
	 * @return A String which summarizes all binary operators in the specified
	 *         parser.
	 * @param parserToExtractFrom
	 *            the parser to extract the descriptions from.
	 * @return a description of all mathematical unary operators in the
	 *         parserToExtractFrom.
	 */
	public static String getBinopsAndDescriptions(
			RecursiveDescentParser parserToExtractFrom) {
		StringBuffer stringBuffer = new StringBuffer();
		Map<String, BinaryOperatorCreator> allBinops = parserToExtractFrom.binaryOperators;
		for (Iterator<Entry<String, BinaryOperatorCreator>> it = allBinops
				.entrySet().iterator(); it.hasNext();) {
			Entry<String, BinaryOperatorCreator> currentEntry = it.next();
			BinaryOperatorCreator currentBinop = currentEntry.getValue();
			stringBuffer.append(currentEntry.getKey() + "\t- "
					+ currentBinop.getDescription()
					+ (it.hasNext() ? "\n" : ""));
		}
		return stringBuffer.toString();
	}
}
