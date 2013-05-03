package example;

import parser.RecursiveDescentParser;
import parser.Value;
import valueTypes.BooleanValue;
import valueTypes.DecimalValue;

/**
 * A unit test for the RecursiveDescentParser. An exception is thrown and
 * printed to the console if an error is found (if the result from the
 * evaluation does not match the expected one)
 * 
 * @author Curran Kelleher
 * 
 */
public class RecursiveDescentParserTest {

	// create the single parser we will use
	static RecursiveDescentParser parser = new RecursiveDescentParser();

	/**
	 * Tests the parser with many expressions.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// test expressions

		assertParseResultEquals("2+2", 4.0);
		assertParseResultEquals("2+(2+(2+2))", 2 + (2 + (2 + 2)));

		assertParseResultEquals("2*(2+(2+2))", 2 * (2 + (2 + 2)));
		assertParseResultEquals("2+(2*(2+2))", 2 + (2 * (2 + 2)));

		assertParseResultEquals("-2", -2.0);
		assertParseResultEquals("-(5^-2)", -Math.pow(5, -2));

		assertParseResultEquals("5^5", Math.pow(5, 5));
		assertParseResultEquals("5+5*5^5", 5 + 5 * Math.pow(5, 5));
		assertParseResultEquals("5+(5*5)^5", 5 + Math.pow(5 * 5, 5));
		assertParseResultEquals("(5+5)*5^5", (5 + 5) * Math.pow(5, 5));

		assertParseResultEquals("5E3", 5000);
		assertParseResultEquals("2+5E3", 5002);

		assertParseResultEquals("sin(3)", Math.sin(3));
		assertParseResultEquals("3Esin(-3+(7^3-400*(6*-3)))", 3 * Math.pow(10,
				Math.sin(-3 + (Math.pow(7, 3) - 400 * (6 * -3)))));

		double a, b, c, d, f;
		assertParseResultEquals("a = -(b = 6^(c = 8+(d = 6-9^(f=99^-2))*5))",
				a = -(b = Math.pow(6, (c = 8 + (d = 6 - Math.pow(9, (f = Math
						.pow(99, -2)))) * 5))));
		assertParseResultEquals("a/b", a / b);
		assertParseResultEquals("c%d", c % d);
		assertParseResultEquals("f%a/d*c", f % a / d * c);

		assertParseResultEquals("cos(5)", Math.cos(5));
		assertParseResultEquals("tan(5)", Math.tan(5));
		assertParseResultEquals("cot(.5)", Math.tan(Math.PI / 2 - 0.5));
		assertParseResultEquals("sec(.5)", 1 / Math.cos(0.5));
		assertParseResultEquals("csc(.5)", 1 / Math.sin(0.5));

		assertParseResultEquals("asin(0.5)", Math.asin(0.5));
		assertParseResultEquals("acos(.5)", Math.acos(0.5));
		assertParseResultEquals("atan(.5)", Math.atan(0.5));

		// sin(3)+cos(5)+tan(5)+cot(.5)+sec(.5)+csc(.5)+asin(0.5)+acos(.5)+atan(.5)

		assertParseResultEquals("ln(5)", Math.log(5));
		assertParseResultEquals("log(1000)", 3);

		assertParseResultEquals("abs(-5)", 5);

		assertParseResultEquals("3!", 6);
		assertParseResultEquals("(2+1)!+4", 10);

		// all mathematical binary operators
		assertParseResultEquals("1+2-3*4/5^6E-7%8!", 1
				+ 2
				- 3
				* 4
				/ Math.pow(5, (6 * Math.pow(10,
						(-7 % (8 * 7 * 6 * 5 * 4 * 3 * 2))))));

		assertParseResultEquals("sqrt(25)", 5);
		assertParseResultEquals("round(6.5)", 7);
		assertParseResultEquals("round(6.4)", 6);
		assertParseResultEquals("floor(6.6)", 6);
		assertParseResultEquals("floor(6.4)", 6);
		assertParseResultEquals("ceil(6.6)", 7);
		assertParseResultEquals("ceil(6.4)", 7);

		// ln(5)+log(1000)+abs(-5)+sqrt(25)+round(6.5)+floor(6.6)+ceil(6.4)

		assertParseResultEquals("pi", Math.PI);
		assertParseResultEquals("e", Math.E);
		assertParseResultEquals("Infinity", Double.POSITIVE_INFINITY);

		assertParseResultEquals("true", true);
		assertParseResultEquals("false", false);

		assertParseResultEquals("1<2", true);
		assertParseResultEquals("2<1", false);
		assertParseResultEquals("2<2", false);
		assertParseResultEquals("10^2-50*2<2E2", true);

		assertParseResultEquals("1<=2", true);
		assertParseResultEquals("2<=1", false);
		assertParseResultEquals("2<=2", true);

		assertParseResultEquals("1>2", false);
		assertParseResultEquals("2>1", true);
		assertParseResultEquals("2>2", false);
		assertParseResultEquals("10^2-50*2>2E2", false);

		assertParseResultEquals("1>=2", false);
		assertParseResultEquals("2>=1", true);
		assertParseResultEquals("2>=2", true);

		assertParseResultEquals("2 == 2", true);
		assertParseResultEquals("2 == 6", false);

		assertParseResultEquals("2 != 2", false);
		assertParseResultEquals("2 != 6", true);

		assertParseResultEquals("true && true", true);
		assertParseResultEquals("true && false", false);
		assertParseResultEquals("false && true", false);

		assertParseResultEquals("true || true", true);
		assertParseResultEquals("true || false", true);
		assertParseResultEquals("false || true", true);
		assertParseResultEquals("false || false", false);

		assertParseResultEquals("4!>=5^2-1&&2*2==2^2&&3!!=4!*2", true);

		assertParseResultEquals("a = 3", 3);
		assertParseResultEquals("b = 4", 4);
		evaluate("f = createFunction(a+b)");
		evaluate("executeFunction(f)");
		assertParseResultEquals("b = 7", 7);
		evaluate("executeFunction(f)");
		evaluate("g = {c = (a*b)}");// same as "g = createFunction(c = (a+b))"
		evaluate("executeFunction(g)");
		assertParseResultEquals("c", 21);

		evaluate("f = {a = 0;b = 1; c = 2; d = false}");
		evaluate("executeFunction(f)");
		assertParseResultEquals("a", 0);
		assertParseResultEquals("b", 1);
		assertParseResultEquals("c", 2);
		assertParseResultEquals("d", false);
		
//		if
		evaluate("if(0<1){a = 5}");
		assertParseResultEquals("a", 5);
		
		// our first recursive progam!!
		assertParseResultEquals("a = 5.0", 5);
		assertParseResultEquals("a", 5);
		evaluate("sum = {if((a = a+1)<500){executeFunction(sum)}}");
		evaluate("executeFunction(sum)");
		assertParseResultEquals("a", 500);
		
		

		// the turnary ? : operator
		assertParseResultEquals("true?5:4", 5);
		assertParseResultEquals("4!>=5^2-1&&2*2==2^2&&3!!=4!*2?5:4", 5);
		assertParseResultEquals("false?5:4", 4);
		evaluate("f={d=(a=0)<1;b=1;c =4!>=5^2-1&&2*2==2^2&&3!!=4!*2?(a==0&&b==1&&d?567:568):678;d=false}");
		evaluate("executeFunction(f)");
		assertParseResultEquals("c", 567);

		// the while loop
		// assertParseResultEquals("a = 1", 1);
		assertParseResultEquals("b = 10", 10);
		evaluate("f = {while(b < 100){b = b+1}}");
		evaluate("executeFunction(f)");
		assertParseResultEquals("b", 100);

		assertParseResultEquals("a = 1", 1);
		evaluate("f = {for(i = 1;i<20;i = i+1){a = a*i}}");
		evaluate("executeFunction(f)");

		int i;
		a = 1;
		for (i = 1; i < 20; i = i + 1) {
			a = a * i;
		}
		assertParseResultEquals("i", i);
		assertParseResultEquals("a", a);
		
		//testing lists
		assertParseResultEquals("[1,2,3]","[1.0, 2.0, 3.0]");
		
		assertParseResultEquals("createErrorValue(\"test error value\")","test error value");
		
		

	}

	/**
	 * Parses and executes the specified expression string. Prints out the
	 * result.
	 * 
	 * @param expression
	 *            the expression string
	 */
	private static void evaluate(String expression) {
		Value result = parser.parse(expression).evaluate();
		System.out.println("executed " + expression);
		System.out.println("  result is " + result);
	}

	/**
	 * Evaluates the specified expression string, and throws an exception if the
	 * result does not equal the specified value.
	 * 
	 * @param expression
	 *            the expression to evaluate
	 * @param b
	 *            the value to test against
	 */
	private static void assertParseResultEquals(String expression, boolean b) {
		Value aVal = parser.parse(expression).evaluate();
		if (aVal instanceof BooleanValue)
			if (((BooleanValue) aVal).value == b) {
				System.out.println("success! " + expression + " == " + b);
				return;
			}

		(new Exception(aVal + " != " + b)).printStackTrace();
	}

	/**
	 * Evaluates the specified expression string, and throws an exception if the
	 * result does not equal the specified value.
	 * 
	 * @param expression
	 *            the expression to evaluate
	 * @param b
	 *            the value to test against
	 */
	private static void assertParseResultEquals(String expression, double b) {
		Value aVal = parser.parse(expression).evaluate();
		if (aVal instanceof DecimalValue)
			if (((DecimalValue) aVal).value == b) {
				System.out.println("success! " + expression + " == " + b);
				return;
			}

		(new Exception(aVal + " != " + b)).printStackTrace();
	}

	/**
	 * Evaluates the specified expression string, and throws an exception if the
	 * result does not equal the specified value.
	 * 
	 * @param expression
	 *            the expression to evaluate
	 * @param b
	 *            the value to test against
	 */
	private static void assertParseResultEquals(String expression, String result) {
		Value aVal = parser.parse(expression).evaluate();
		if (aVal.toString().equals(result)) {
			System.out.println("success! " + expression + " == " + result);
			return;
		}
		(new Exception(aVal + " != " + result)).printStackTrace();
	}
}
