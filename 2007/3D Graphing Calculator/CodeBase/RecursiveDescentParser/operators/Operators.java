package operators;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import parser.ExpressionNode;
import parser.RecursiveDescentParser;
import parser.Value;
import valueTypes.BooleanValue;
import valueTypes.CharacterSeparatedStatementPair;
import valueTypes.ColorValue;
import valueTypes.DecimalValue;
import valueTypes.ErrorValue;
import valueTypes.FunctionValue;
import valueTypes.IntegerValue;
import valueTypes.NullValue;
import valueTypes.StringValue;
import variables.Variable;

/**
 * The class which defines most operators, such as +,-,*,/,sin,cos,sqrt, etc.
 * 
 * @author Curran Kelleher
 * 
 */
public class Operators {

	/**
	 * The static Maps of the unary and binary operators. They only get filled
	 * once, this is an optimization for the case where many new instances of
	 * RecursiveDescentParser are created. (These maps first get populated when
	 * the first RecursiveDescentParser is constructed.)
	 */
	private static Map<String, BinaryOperatorCreator> binaryOperators = null;

	private static Map<String, UnaryOperatorCreator> unaryOperators = null;

	/**
	 * Populates the specified parent RecursiveDescentParser, (via callbacks to
	 * the addBinaryOperator() method), with all available binary operators.
	 * 
	 * @param parent
	 *            the parent RecursiveDescentParser which will be populated with
	 *            unary operators via callbacks to the addBinaryOperator()
	 *            method
	 */
	public static void getBinaryOperators(RecursiveDescentParser parent) {
		createAllBinaryOperators();

		for (Iterator it = binaryOperators.entrySet().iterator(); it.hasNext();) {
			Entry entry = (Entry) it.next();
			parent.addBinaryOperator((String) entry.getKey(),
					(BinaryOperatorCreator) entry.getValue());
		}
	}

	/**
	 * Ensures the static binaryOperators Map is populated with all standard
	 * binary operators.
	 * 
	 */
	private static void createAllBinaryOperators() {
		if (binaryOperators == null) {
			binaryOperators = new HashMap<String, BinaryOperatorCreator>();

			binaryOperators.put("+", new BinaryOperatorCreator(1) {
				public BinaryOperator create(ExpressionNode left,
						ExpressionNode right) {
					return new RealNumberBinaryOperator('+', left, right) {
						double evaluate(double l, double r) {
							return l + r;
						}
					};
				}

				public String getDescription() {
					return "plus, the addition operator";
				}
			});

			binaryOperators.put("-", new Minus());

			binaryOperators.put("*", new BinaryOperatorCreator(2) {
				public BinaryOperator create(ExpressionNode left,
						ExpressionNode right) {
					return new RealNumberBinaryOperator('*', left, right) {
						double evaluate(double l, double r) {
							return l * r;
						}
					};
				}

				public String getDescription() {
					return "times, the multiplication operator";
				}
			});

			binaryOperators.put("/", new BinaryOperatorCreator(2) {
				public BinaryOperator create(ExpressionNode left,
						ExpressionNode right) {
					return new RealNumberBinaryOperator('/', left, right) {
						double evaluate(double l, double r) {
							return l / r;
						}
					};
				}

				public String getDescription() {
					return "divide by, the division operator";
				}
			});

			binaryOperators.put("%", new BinaryOperatorCreator(2) {
				public BinaryOperator create(ExpressionNode left,
						ExpressionNode right) {
					return new RealNumberBinaryOperator('%', left, right) {
						double evaluate(double l, double r) {
							return l % r;
						}
					};
				}

				public String getDescription() {
					return "modulo, calculates the remainder. For example, 10%7 is read \"ten mod (or modulo) seven\" and results in 3. The % operator is not restricted to integers.";
				}
			});

			binaryOperators.put("^", new BinaryOperatorCreator(3) {
				public BinaryOperator create(ExpressionNode left,
						ExpressionNode right) {
					return new RealNumberBinaryOperator('^', left, right) {
						double evaluate(double l, double r) {
							return Math.pow(l, r);
						}
					};
				}

				public String getDescription() {
					return "a^b = a to the power of b.";
				}
			});

			binaryOperators.put("E", new BinaryOperatorCreator(4) {
				public BinaryOperator create(ExpressionNode left,
						ExpressionNode right) {
					return new RealNumberBinaryOperator('E', left, right) {
						double evaluate(double l, double r) {
							return l * Math.pow(10, r);
						}
					};
				}

				public String getDescription() {
					return "times 10 to the power of... (a)E(b) = a*(10^b). For example, 5E2 = 500";
				}
			});

			binaryOperators.put("<", new BinaryOperatorCreator(0) {
				public BinaryOperator create(ExpressionNode left,
						ExpressionNode right) {
					return new BinaryOperator("<", left, right) {
						BooleanValue persistantResult = new BooleanValue(false);

						@SuppressWarnings("unchecked")
						public Value evaluate() {

							Value leftResult = leftChild.evaluate();
							Value rightResult = rightChild.evaluate();

							if (leftResult instanceof Comparable
									&& rightResult instanceof Comparable) {
								try {
									persistantResult.value = ((Comparable) leftResult)
											.compareTo(rightResult) < 0;
									return persistantResult;
								} catch (Exception e) {
								}
							}

							return new ErrorValue(leftResult + " and "
									+ rightResult + " cannot be compared");

						}

					};
				}

				public String getDescription() {
					return "the \"less than\" comparison. a<b yields true if a is less than b, false otherwise";
				}
			});

			binaryOperators.put("<=", new BinaryOperatorCreator(0) {
				public BinaryOperator create(ExpressionNode left,
						ExpressionNode right) {
					return new BinaryOperator("<=", left, right) {
						BooleanValue persistantResult = new BooleanValue(false);

						@SuppressWarnings("unchecked")
						public Value evaluate() {

							Value leftResult = leftChild.evaluate();
							Value rightResult = rightChild.evaluate();

							if (leftResult instanceof Comparable
									&& rightResult instanceof Comparable) {
								try {
									persistantResult.value = ((Comparable) leftResult)
											.compareTo(rightResult) < 1;
									return persistantResult;
								} catch (Exception e) {
								}
							}

							return new ErrorValue(leftResult + " and "
									+ rightResult + " cannot be compared");

						}

					};
				}

				public String getDescription() {
					return "the \"less than or equal to\" comparison. a<=b yields true if a is less than or equal to b, false otherwise";
				}
			});

			binaryOperators.put(">", new BinaryOperatorCreator(0) {
				public BinaryOperator create(ExpressionNode left,
						ExpressionNode right) {
					return new BinaryOperator(">", left, right) {
						BooleanValue persistantResult = new BooleanValue(false);

						@SuppressWarnings("unchecked")
						public Value evaluate() {

							Value leftResult = leftChild.evaluate();
							Value rightResult = rightChild.evaluate();

							if (leftResult instanceof Comparable
									&& rightResult instanceof Comparable) {
								try {
									persistantResult.value = ((Comparable) leftResult)
											.compareTo(rightResult) > 0;
									return persistantResult;
								} catch (Exception e) {
								}
							}

							return new ErrorValue(leftResult + " and "
									+ rightResult + " cannot be compared");

						}

					};
				}

				public String getDescription() {
					return "the \"greater than\" comparison. a>b yields true if a is greater than b, false otherwise";
				}
			});

			binaryOperators.put(">=", new BinaryOperatorCreator(0) {
				public BinaryOperator create(ExpressionNode left,
						ExpressionNode right) {
					return new BinaryOperator(">=", left, right) {
						BooleanValue persistantResult = new BooleanValue(false);

						@SuppressWarnings("unchecked")
						public Value evaluate() {

							Value leftResult = leftChild.evaluate();
							Value rightResult = rightChild.evaluate();

							if (leftResult instanceof Comparable
									&& rightResult instanceof Comparable) {
								try {
									persistantResult.value = ((Comparable) leftResult)
											.compareTo(rightResult) > -1;
									return persistantResult;
								} catch (Exception e) {
								}
							}

							return new ErrorValue(leftResult + " and "
									+ rightResult + " cannot be compared");

						}

					};
				}

				public String getDescription() {
					return "the \"greater than or equal to\" comparison. a>=b yields true if a is greater than or equal to b, false otherwise";
				}
			});

			binaryOperators.put("==", new BinaryOperatorCreator(0) {
				public BinaryOperator create(ExpressionNode left,
						ExpressionNode right) {
					return new BinaryOperator("==", left, right) {
						BooleanValue persistantResult = new BooleanValue(false);

						public Value evaluate() {
							Value leftResult = leftChild.evaluate();
							Value rightResult = rightChild.evaluate();
							persistantResult.value = leftResult
									.equals(rightResult);
							return persistantResult;
						}

					};
				}

				public String getDescription() {
					return "the \"equal to\" comparison. a==b yields true if a is equal to b, false otherwise";
				}
			});

			binaryOperators.put("!=", new BinaryOperatorCreator(0) {
				public BinaryOperator create(ExpressionNode left,
						ExpressionNode right) {
					return new BinaryOperator("!=", left, right) {
						BooleanValue persistantResult = new BooleanValue(false);

						public Value evaluate() {
							Value leftResult = leftChild.evaluate();
							Value rightResult = rightChild.evaluate();
							persistantResult.value = !leftResult
									.equals(rightResult);
							return persistantResult;
						}

					};
				}

				public String getDescription() {
					return "the \"not equal to\" comparison. a!=b yields true if a is not equal to b, false otherwise";
				}
			});

			binaryOperators.put("&&", new BinaryOperatorCreator(-1) {
				public BinaryOperator create(ExpressionNode left,
						ExpressionNode right) {
					return new BinaryOperator("&&", left, right) {
						BooleanValue persistantResult = new BooleanValue(false);

						public Value evaluate() {
							Value leftResult = leftChild.evaluate();
							Value rightResult = rightChild.evaluate();

							try {
								persistantResult.value = ((BooleanValue) leftResult).value
										&& ((BooleanValue) rightResult).value;
								return persistantResult;
							} catch (Exception e) {
								return new ErrorValue(
										"&& is not a valid operator for "
												+ leftResult + " and "
												+ rightResult);
							}
						}

					};
				}

				public String getDescription() {
					return "the boolean AND operation, only works with two booleans.";
				}
			});

			binaryOperators.put("||", new BinaryOperatorCreator(-1) {
				public BinaryOperator create(ExpressionNode left,
						ExpressionNode right) {
					return new BinaryOperator("||", left, right) {
						BooleanValue persistantResult = new BooleanValue(false);

						public Value evaluate() {
							Value leftResult = leftChild.evaluate();
							Value rightResult = rightChild.evaluate();

							try {
								persistantResult.value = ((BooleanValue) leftResult).value
										|| ((BooleanValue) rightResult).value;
								return persistantResult;
							} catch (Exception e) {
								return new ErrorValue(
										"|| is not a valid operator for "
												+ leftResult + " and "
												+ rightResult);
							}
						}

					};
				}

				public String getDescription() {
					return "the boolean OR operation, only works with two booleans.";
				}
			});

			// The turnary ? : operator set:
			binaryOperators.put("?", new BinaryOperatorCreator(-3) {
				public BinaryOperator create(ExpressionNode left,
						ExpressionNode right) {
					return new BinaryOperator("?", left, right) {
						public Value evaluate() {
							Value leftResult = leftChild.evaluate();
							// Value rightResult = rightChild.evaluate();
							if (leftResult instanceof BooleanValue)
								if (rightChild instanceof CharacterSeparatedStatementPair ? ((CharacterSeparatedStatementPair) rightChild)
										.getSymbol().equals(":")
										: false)
									if (((BooleanValue) leftResult).value)
										return ((CharacterSeparatedStatementPair) rightChild)
												.getLeftStatement().evaluate();
									else
										return ((CharacterSeparatedStatementPair) rightChild)
												.getRightStatement().evaluate();
								else
									return new ErrorValue(
											rightChild
													+ " should be a colon separated statement pair for the ? : operator");
							else
								return new ErrorValue(
										leftResult
												+ " is an invalid condition for the ? : operator");

						}
					};
				}

				public String getDescription() {
					return "the \"?\" part of the ?: ternary operator. \"a<b?c=2:c=3\" executes \"c=2\" if a is less than b, or executes \"c=3\" otherwise.";
				}
			});

			binaryOperators.put(":", new BinaryOperatorCreator(-2) {
				public BinaryOperator create(ExpressionNode left,
						ExpressionNode right) {
					return new CharacterSeparatedStatementPair(":", left, right);
				}

				public String getDescription() {
					return "the \":\" part of the ?: ternary operator. \"a<b?c=2:c=3\" executes \"c=2\" if a is less than b, or executes \"c=3\" otherwise.";
				}
			});

			binaryOperators.put(";", new BinaryOperatorCreator(-5) {
				public BinaryOperator create(ExpressionNode left,
						ExpressionNode right) {
					return new CharacterSeparatedStatementPair(";", left, right);
				}

				public String getDescription() {
					return "a statement separator";
				}
			});

			binaryOperators.put(",", new BinaryOperatorCreator(-5) {
				public BinaryOperator create(ExpressionNode left,
						ExpressionNode right) {
					return new CharacterSeparatedStatementPair(",", left, right);
				}

				public String getDescription() {
					return "an argument separator";
				}
			});

			binaryOperators.put("=", new BinaryOperatorCreator(-4) {
				public BinaryOperator create(ExpressionNode left,
						ExpressionNode right) {
					return new BinaryOperator("=", left, right) {
						public Value evaluate() {
							Value rightValue = rightChild.evaluate();
							try {
								return ((Variable) leftChild).set(rightValue);
							} catch (Exception e) {
								Value leftValue = leftChild.evaluate();
								return new ErrorValue(
										"'=' is not a valid operator for the types "
												+ leftValue.getType() + " and "
												+ rightValue.getType()
												+ ", so " + leftValue + " = "
												+ rightValue
												+ " could not be evaluated");
							}
						}
					};
				}

				public String getDescription() {
					return "the assignment operator. Takes a variable name on the left, and a value on the right. For example, \"a=5\" changes the value of a to 5";
				}
			});

		}
	}

	/**
	 * Populates the specified parent RecursiveDescentParser, (via callbacks to
	 * the addUnaryOperator() method), with all available unary operators.
	 * 
	 * @param parent
	 *            the parent RecursiveDescentParser which will be populated with
	 *            unary operators via callbacks to the addUnaryOperator() method
	 */
	public static void getUnaryOperators(RecursiveDescentParser parent) {

		createAllUnaryOperators();

		for (Iterator it = unaryOperators.entrySet().iterator(); it.hasNext();) {
			Entry entry = (Entry) it.next();
			parent.addUnaryOperator((String) entry.getKey(),
					(UnaryOperatorCreator) entry.getValue());
		}

	}

	/**
	 * Ensures the static unaryOperators Map is populated with all standard
	 * unary operators.
	 * 
	 */
	private static void createAllUnaryOperators() {

		if (unaryOperators == null) {

			unaryOperators = new HashMap<String, UnaryOperatorCreator>();

			unaryOperators.put("sin", new MathematicalUnaryOperatorCreator() {
				public UnaryOperator create(ExpressionNode child) {
					return new RealNumberUnaryOperator("sin", child) {
						double evaluate(double x) {
							return Math.sin(x);
						}
					};
				}

				public String getDescription() {
					return "the sine function";
				}
			});

			unaryOperators.put("cos", new MathematicalUnaryOperatorCreator() {
				public UnaryOperator create(ExpressionNode child) {
					return new RealNumberUnaryOperator("cos", child) {
						double evaluate(double x) {
							return Math.cos(x);
						}
					};
				}

				public String getDescription() {
					return "the cosine function";
				}
			});

			unaryOperators.put("tan", new MathematicalUnaryOperatorCreator() {
				public UnaryOperator create(ExpressionNode child) {
					return new RealNumberUnaryOperator("tan", child) {
						double evaluate(double x) {
							return Math.tan(x);
						}
					};
				}

				public String getDescription() {
					return "the tangent function, tan(x) = sin(x)/cos(x)";
				}
			});

			unaryOperators.put("asin", new MathematicalUnaryOperatorCreator() {
				public UnaryOperator create(ExpressionNode child) {
					return new RealNumberUnaryOperator("asin", child) {
						double evaluate(double x) {
							return Math.asin(x);
						}
					};
				}

				public String getDescription() {
					return "arcSine, the inverse of the sine function";
				}
			});

			unaryOperators.put("acos", new MathematicalUnaryOperatorCreator() {
				public UnaryOperator create(ExpressionNode child) {
					return new RealNumberUnaryOperator("acos", child) {
						double evaluate(double x) {
							return Math.acos(x);
						}
					};
				}

				public String getDescription() {
					return "arcCosine, the inverse of the cosine function";
				}
			});

			unaryOperators.put("atan", new MathematicalUnaryOperatorCreator() {
				public UnaryOperator create(ExpressionNode child) {
					return new RealNumberUnaryOperator("atan", child) {
						double evaluate(double x) {
							return Math.atan(x);
						}
					};
				}

				public String getDescription() {
					return "arcTangent, the inverse of the tangent function";
				}
			});

			unaryOperators.put("cot", new MathematicalUnaryOperatorCreator() {
				public UnaryOperator create(ExpressionNode child) {
					return new RealNumberUnaryOperator("cot", child) {
						double HALF_PI = Math.PI / 2;

						double evaluate(double x) {
							return Math.tan(HALF_PI - x);
						}
					};
				}

				public String getDescription() {
					return "the cotangent function, cot(x) = cos(x)/sin(x)";
				}
			});

			unaryOperators.put("sec", new MathematicalUnaryOperatorCreator() {
				public UnaryOperator create(ExpressionNode child) {
					return new RealNumberUnaryOperator("sec", child) {
						double evaluate(double x) {
							return 1.0 / Math.cos(x);
						}
					};
				}

				public String getDescription() {
					return "the secant function, sec(x) = 1/cos(x)";
				}
			});

			unaryOperators.put("csc", new MathematicalUnaryOperatorCreator() {
				public UnaryOperator create(ExpressionNode child) {
					return new RealNumberUnaryOperator("csc", child) {
						double evaluate(double x) {
							return 1.0 / Math.sin(x);
						}
					};
				}

				public String getDescription() {
					return "the cosecant function, csc(x) = 1/sin(x)";
				}
			});

			unaryOperators.put("ln", new MathematicalUnaryOperatorCreator() {
				public UnaryOperator create(ExpressionNode child) {
					return new RealNumberUnaryOperator("ln", child) {
						double evaluate(double x) {
							return Math.log(x);
						}
					};
				}

				public String getDescription() {
					return "natural logarithm, log base e";
				}
			});

			unaryOperators.put("log", new MathematicalUnaryOperatorCreator() {
				public UnaryOperator create(ExpressionNode child) {
					return new RealNumberUnaryOperator("ln", child) {
						double conversion = Math.log(10);

						double evaluate(double x) {
							// cast down and up in order to round to whole
							// numbers
							// when they are very close.
							return (double) ((float) (Math.log(x) / conversion));
						}
					};
				}

				public String getDescription() {
					return "logarithm base 10";
				}
			});

			unaryOperators.put("abs", new MathematicalUnaryOperatorCreator() {
				public UnaryOperator create(ExpressionNode child) {
					return new RealNumberUnaryOperator("abs", child) {
						double evaluate(double x) {
							return Math.abs(x);
						}
					};
				}

				public String getDescription() {
					return "absolute value";
				}
			});

			unaryOperators.put("sqrt", new MathematicalUnaryOperatorCreator() {
				public UnaryOperator create(ExpressionNode child) {
					return new RealNumberUnaryOperator("sqrt", child) {
						double evaluate(double x) {
							return Math.sqrt(x);
						}
					};
				}

				public String getDescription() {
					return "square root";
				}
			});

			unaryOperators.put("round", new MathematicalUnaryOperatorCreator() {
				public UnaryOperator create(ExpressionNode child) {
					return new RealNumberUnaryOperator("round", child) {
						double evaluate(double x) {
							return Math.round(x);
						}
					};
				}

				public String getDescription() {
					return "round to the nearest whole number";
				}
			});

			unaryOperators.put("floor", new MathematicalUnaryOperatorCreator() {
				public UnaryOperator create(ExpressionNode child) {
					return new RealNumberUnaryOperator("floor", child) {
						double evaluate(double x) {
							return Math.floor(x);
						}
					};
				}

				public String getDescription() {
					return "floor: floor(3.3) = 3, floor(3.9) = 3";
				}
			});

			unaryOperators.put("ceil", new MathematicalUnaryOperatorCreator() {
				public UnaryOperator create(ExpressionNode child) {
					return new RealNumberUnaryOperator("ceil", child) {
						double evaluate(double x) {
							return Math.ceil(x);
						}
					};
				}

				public String getDescription() {
					return "ceiling: ceil(3.3) = 4, ceil(3.9) = 4";
				}
			});

			unaryOperators.put("createFunction", new UnaryOperatorCreator() {
				public UnaryOperator create(ExpressionNode child) {
					return new UnaryOperator(child) {

						public Value evaluate() {
							return new FunctionValue(child);
						}

						/**
						 * @return a string representation of this
						 *         UnaryOperator.
						 */
						public String toString() {
							return "createFunction(" + child + ")";
						}
					};
				}

				public String getDescription() {
					return "creates a function, which can be evaluated by the executeFunction() operator";
				}

				public int getType() {
					return UnaryOperatorCreator.LANGUAGE_CONSTRUCT;
				}
			});

			unaryOperators.put("executeFunction",
					new MathematicalUnaryOperatorCreator() {
						public UnaryOperator create(ExpressionNode child) {
							return new UnaryOperator(child) {

								public Value evaluate() {
									// return new FunctionValue(child);
									Value value = child.evaluate();
									if (value instanceof FunctionValue)
										return ((FunctionValue) value)
												.executeFunction();
									else
										return new ErrorValue(
												"executeFunction only takes a FunctionValue as an argument, not a "
														+ value.getType()
														+ "; "
														+ value.toString());
								}

								/**
								 * @return a string representation of this
								 *         UnaryOperator.
								 */
								public String toString() {
									return "executeFunction(" + child + ")";
								}

							};
						}

						public String getDescription() {
							return "executes a function, which can be created by the createFunction() operator";
						}

						public int getType() {
							return UnaryOperatorCreator.LANGUAGE_CONSTRUCT;
						}
					});

			unaryOperators.put("if", new UnaryOperatorCreator() {
				public UnaryOperator create(ExpressionNode child) {
					return new UnaryOperator(child) {
						FunctionValue associatedFunction = null;

						public Value evaluate() {
							Value condition = child.evaluate();
							if (condition instanceof BooleanValue)
								if (((BooleanValue) condition).value) {
									if (associatedFunction != null) {
										associatedFunction.executeFunction();
										return NullValue.NULL;
									} else
										return new ErrorValue(
												"no associated function for \"if\" statement!");
								} else
									return NullValue.NULL;
							else
								return new ErrorValue(
										"the value "
												+ condition
												+ " is not a BooleanValue, so it is an invalid condition for an \"if\" statement");
						}

						public String toString() {
							return "if(" + child + ")";
						}

						public boolean linkAssociatedFunction(
								FunctionValue associatedFunction) {
							this.associatedFunction = associatedFunction;
							return true;
						}
					};
				}

				public String getDescription() {
					return "the \"if\" construct, which executes an associated function if the argument is true. For example, when \"if(a<b){c = 5}\" is executed, c will be set to 5 if a is less than b";
				}

				public int getType() {
					return UnaryOperatorCreator.LANGUAGE_CONSTRUCT;
				}
			});

			unaryOperators.put("while", new MathematicalUnaryOperatorCreator() {
				public UnaryOperator create(ExpressionNode child) {
					return new UnaryOperator(child) {
						FunctionValue associatedFunction = null;

						public Value evaluate() {
							Value condition = child.evaluate();
							if (condition instanceof BooleanValue)
								if (((BooleanValue) condition).value) {
									if (associatedFunction != null) {
										boolean conditionBoolean = true;

										try {
											while (conditionBoolean) {
												associatedFunction
														.executeFunction();
												conditionBoolean = ((BooleanValue) child
														.evaluate()).value;
											}
										} catch (Exception e) {
											return new ErrorValue(
													"error occured in evaluating the condition "
															+ child);
										}
										return NullValue.NULL;
									} else
										return new ErrorValue(
												"no associated function for \"while\" statement!");
								} else
									return NullValue.NULL;
							else
								return new ErrorValue(
										"the value "
												+ condition
												+ " is not a BooleanValue, so it is an invalid condition for a \"while\" statement");
						}

						public String toString() {
							return "while(" + child + ")" + associatedFunction;
						}

						public boolean linkAssociatedFunction(
								FunctionValue associatedFunction) {
							this.associatedFunction = associatedFunction;
							return true;
						}
					};
				}

				public String getDescription() {
					return "the \"while\" loop construct, which executes an associated function as long as the argument is true. For example, when \"{while(b < 100){b = b+1}\" is executed, the function \"b = b+1\" will be executed as many times as it takes for \"b < 100\" to be false, so after the execution of the while loop is finished, b will be 100";
				}

				public int getType() {
					return UnaryOperatorCreator.LANGUAGE_CONSTRUCT;
				}
			});

			unaryOperators.put("for", new MathematicalUnaryOperatorCreator() {
				public UnaryOperator create(ExpressionNode child) {
					return new ForLoop(child);
				}

				public String getDescription() {
					return "the \"for\" loop construct, of the form \"for(initialisation;condition;after){statement(s)}\", which executes an associated function as long as the condition is true.";
				}

				public int getType() {
					return UnaryOperatorCreator.LANGUAGE_CONSTRUCT;
				}
			});

			unaryOperators.put("createErrorValue", new UnaryOperatorCreator() {
				public UnaryOperator create(ExpressionNode child) {
					return new UnaryOperator(child) {
						public Value evaluate() {
							Value value = child.evaluate();
							if (value instanceof StringValue)
								return new ErrorValue(((StringValue) value)
										.toString());
							else
								return new ErrorValue(
										"createErrorValue() takes a string as an argument, "
												+ value
												+ " is an invalid argument.");
						}
					};
				}

				/**
				 * 
				 * @return a human-readable description of the unary operator
				 *         which this UnaryOperatorCreator creates.
				 */
				public String getDescription() {
					return "creates an ErrorValue with the specified message. for example, \"createErrorValue(\"this error is because of ...\")";
				}

				/**
				 * Gets the type flag which denotes which type of operator the
				 * unary operator which this UnaryOperatorCreator creates can be
				 * generally classified as.
				 * 
				 * @return MATHEMATICAL, LANGUAGE_CONSTRUCT, COMMAND or
				 *         MISCELLANEOUS
				 */
				public int getType() {
					return UnaryOperatorCreator.MISCELLANEOUS;
				}
			});

			unaryOperators.put("createColor", new UnaryOperatorCreator() {
				public UnaryOperator create(ExpressionNode child) {
					return new UnaryOperator(child) {
						ColorValue reUsableValue = new ColorValue(.5, .5, .5);

						public Value evaluate() {
							if (child instanceof CharacterSeparatedStatementPair) {
								CharacterSeparatedStatementPair listOfVariables = (CharacterSeparatedStatementPair) child;
								if (listOfVariables.getSymbol().equals(",")) {
									ExpressionNode[] statements = ((CharacterSeparatedStatementPair) child)
											.extractAllStatements();
									if (statements.length == 3
											|| statements.length == 4) {
										double[] rgbValues = new double[statements.length];
										for (int i = 0; i < statements.length; i++) {
											Value currentValue = statements[i]
													.evaluate();
											if (currentValue instanceof DecimalValue)
												rgbValues[i] = ((DecimalValue) currentValue).value;
											else
												return new ErrorValue(
														"createColor(redBetweenZeroAndOne,greenBetweenZeroAndOne,blueBetweenZeroAndOne"
																+ (statements.length == 4 ? "opacityBetweenZeroAndOne"
																		: "")
																+ ") takes "
																+ statements.length
																+ " numbers as arguments. The type of argument number "
																+ (1 + i)
																+ " is "
																+ currentValue
																		.getType()
																+ ", and is therefore not a valid argument.");

										}
										if (statements.length == 3)
											reUsableValue.set(rgbValues[0],
													rgbValues[1], rgbValues[2]);
										else
											// if statements.length == 4
											reUsableValue.set(rgbValues[0],
													rgbValues[1], rgbValues[2],
													rgbValues[3]);
										return reUsableValue;
									} else
										return new ErrorValue(
												"createColor(redBetweenZeroAndOne,greenBetweenZeroAndOne,blueBetweenZeroAndOne,opacityBetweenZeroAndOne) takes 3 or 4, not"
														+ statements.length
														+ ", numbers as arguments.");
								} else
									return new ErrorValue(
											"createColor(redBetweenZeroAndOne,greenBetweenZeroAndOne,blueBetweenZeroAndOne,opacityBetweenZeroAndOne) takes 3 or 4 numbers separated by commas \",\" as arguments."
													+ listOfVariables
															.getSymbol()
													+ " is an invalid argument separator.");
							} else
								return new ErrorValue(
										"createColor(redBetweenZeroAndOne,greenBetweenZeroAndOne,blueBetweenZeroAndOne,opacityBetweenZeroAndOne) takes 3 or 4 numbers separated by commas \",\" as arguments. "
												+ child.toString()
												+ " is an invalid argument.");
						}
					};
				}

				/**
				 * 
				 * @return a human-readable description of the unary operator
				 *         which this UnaryOperatorCreator creates.
				 */
				public String getDescription() {
					return "creates a color based on the specified red, green, and blue arguments, which range from 0 to 1. A fourth alpha, or opacity, argument is optional, also ranging from 0 to 1, and defaults to 1 (completely opaque) if not specified.";
				}

				/**
				 * Gets the type flag which denotes which type of operator the
				 * unary operator which this UnaryOperatorCreator creates can be
				 * generally classified as.
				 * 
				 * @return MATHEMATICAL, LANGUAGE_CONSTRUCT, COMMAND or
				 *         MISCELLANEOUS
				 */
				public int getType() {
					return UnaryOperatorCreator.MISCELLANEOUS;
				}
			});
			unaryOperators.put("integer", new UnaryOperatorCreator() {
				public UnaryOperator create(ExpressionNode child) {
					return new UnaryOperator(child) {
						public Value evaluate() {
							Value value = child.evaluate();
							if (value instanceof DecimalValue)
								return new IntegerValue((int) Math
										.round(((DecimalValue) value).value));
							else
								return new ErrorValue(
										"createErrorValue() takes a number as an argument, "
												+ value
												+ " is an invalid argument.");
						}
					};
				}

				/**
				 * 
				 * @return a human-readable description of the unary operator
				 *         which this UnaryOperatorCreator creates.
				 */
				public String getDescription() {
					return "creates an IntegerValue with the specified number (which gets rounded if it's not an integer).";
				}

				/**
				 * Gets the type flag which denotes which type of operator the
				 * unary operator which this UnaryOperatorCreator creates can be
				 * generally classified as.
				 * 
				 * @return MATHEMATICAL, LANGUAGE_CONSTRUCT, COMMAND or
				 *         MISCELLANEOUS
				 */
				public int getType() {
					return UnaryOperatorCreator.MISCELLANEOUS;
				}
			});
		}
	}
}

abstract class MathematicalUnaryOperatorCreator implements UnaryOperatorCreator {
	public int getType() {
		return UnaryOperatorCreator.MATHEMATICAL;
	}
}

class ForLoop extends UnaryOperator {
	ExpressionNode[] threeStatements = null;

	public ForLoop(ExpressionNode child) {
		super(child);

		if (child instanceof CharacterSeparatedStatementPair)
			if (((CharacterSeparatedStatementPair) child).getSymbol().equals(
					";"))
				threeStatements = ((CharacterSeparatedStatementPair) child)
						.extractAllStatements();

		if (threeStatements == null) {
			threeStatements = new ExpressionNode[1];
			threeStatements[0] = new ErrorValue(
					"the \"for\" loop construct takes three statements separated by semicolons, so "
							+ child + " is invalid in a \"for\" loop.");
		}
	}

	FunctionValue associatedFunction = null;

	public Value evaluate() {
		if (associatedFunction != null) {
			if (threeStatements.length == 3) {

				Value resultFromInitializer = threeStatements[0].evaluate();
				if (resultFromInitializer instanceof ErrorValue)
					return resultFromInitializer;

				boolean conditionBoolean;
				Value resultFromCondition = threeStatements[1].evaluate();
				if (resultFromCondition instanceof BooleanValue)
					conditionBoolean = ((BooleanValue) resultFromCondition).value;
				else
					return new ErrorValue(
							"the condition in the \"for\" loop must be a BooleanValue, but the contidion "
									+ resultFromCondition + " is a "
									+ resultFromCondition.getType()
									+ ", not a BooleanValue.");

				Value endStatementResult;
				try {
					while (conditionBoolean) {
						associatedFunction.executeFunction();
						if ((endStatementResult = threeStatements[2].evaluate()) instanceof ErrorValue)
							return endStatementResult;
						conditionBoolean = ((BooleanValue) threeStatements[1]
								.evaluate()).value;

					}
				} catch (Exception e) {
					return new ErrorValue(
							"error occured in evaluating the condition "
									+ child);
				}
				return NullValue.NULL;
			} else
				return new ErrorValue(
						"the \"for\" loop construct takes three statements separated by semicolons, so "
								+ child + " is invalid in a \"for\" loop.");

		} else
			return new ErrorValue("no associated function for \"for\" loop!");

	}

	/**
	 * @return a string representation of this ForLoop.
	 */
	public String toString() {
		StringBuffer b = new StringBuffer();
		b.append("for(");
		for (int i = 0; i < threeStatements.length; i++)
			b.append(threeStatements[i].toString()
					+ ((i < threeStatements.length - 1) ? "; " : ""));
		b.append(")");
		b.append(associatedFunction.toString());
		return b.toString();
	}

	public boolean linkAssociatedFunction(FunctionValue associatedFunction) {
		this.associatedFunction = associatedFunction;
		return true;
	}
}