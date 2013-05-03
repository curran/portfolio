package example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import parser.ExpressionNode;
import parser.RecursiveDescentParser;
import parser.Value;

/**
 * An interactive example demonstrating the use of RecursiveDescentParser.
 * 
 * @author Curran Kelleher
 * 
 */
public class RecursiveDescentParserExample {
	
	/**
	 * A simple program which evaluates expressions typed by the user into the console.
	 * @param args command line arguments, not used
	 */
	public static void main(String[] args) {
		//initialize the parser
		RecursiveDescentParser parser = new RecursiveDescentParser();
		
		//set up input reading
		BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
		String expression = null;
		System.out.println("type expressions or \"exit\" to exit");
		while (true) {
			try {
				expression = keyboard.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(expression.equals("exit"))
				break;
			
			//parse the expression string into an evaluation tree
			ExpressionNode evaluationTree = parser.parse(expression);
			
			//evaluate the expression tree
			Value answer = evaluationTree.evaluate();
			
			//print the result
			System.out.println(expression+" = "+answer);
		}
	}//soliloquey samali origami
}
