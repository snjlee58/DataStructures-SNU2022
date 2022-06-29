import java.io.*;
import java.util.Stack;

public class CalculatorTest
{
	public static void main(String args[])
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		while (true)
		{
			try
			{
				String input = br.readLine();
				if (input.compareTo("q") == 0)
					break;

				command(input);
			}
			catch (Exception e)
			{
				System.out.println("ERROR");
			}
		}
	}

	private static void command(String input)
	{
		try {
			// Convert inputted infix to postfix
			String postfixExpression = convertToPostfix(input);

			// Evaluate postfix expression
			long result = calculatePostfix(postfixExpression);

			// Print postfix expression
			System.out.println(postfixExpression);

			// Print calculated result
			System.out.println(result);

		} catch (ArithmeticException e){
			// Print "ERROR" if any invalid expressions were detected.
			// All invalid expressions were counted as ArithmeticException objects.
			System.out.println("ERROR");
		}
	}

	private static int precedence(char operator){
		// Function that returns the precedence order of operators.
		if (operator == '+' || operator == '-'){
			return 1;
		} else if (operator == '*' || operator == '/' || operator == '%'){
			return 2;
		} else if (operator == '~'){
			return 3;
		} else if (operator == '^'){
			return 4;
		} else {
			return 0;
		}
	}

	private static boolean validInfixFormat(String[] splittedInput) {
		char lastChar = splittedInput[0].charAt(splittedInput[0].length()-1);
		for (int i = 1; i < splittedInput.length; i++){
			String splittedTerm = splittedInput[i];
			char firstChar = splittedTerm.charAt(0);
			if (Character.isDigit(lastChar) && Character.isDigit(firstChar)) return false;
			lastChar = splittedTerm.charAt(splittedTerm.length()-1);
		}
		return true;
	}

	private static String convertToPostfix(String input) throws ArithmeticException{
		// Throws ArithmeticException if any invalid format is found.

		String[] splittedInput = input.split("\\s+");

		// Validate inputted infix expression.
		if (!validInfixFormat(splittedInput)) throw new ArithmeticException();

		// Rebuild infix expression without any whitespaces.
		StringBuilder infix = new StringBuilder();
		for (String s : splittedInput){
			infix.append(s);
		}

		// Create stacks to store operators that will be popped and appended to the postfix expression in the correct order.
		Stack<Character> operatorStack = new Stack<>();
		Stack<Character> bracketStack = new Stack<>();
		StringBuilder postfix = new StringBuilder();

		// Tracks whether the previous character in the infix expression was a DIGIT.
		// Used to determine whether to add a space before appending currChar
		boolean previouslyDigit = false;

		// Tracks whether the previous character in the infix expression was an OPERATOR (excluding parentheses).
		// Used when a '-' operator appears: to determine whether to interpret it as unary or binary.
		boolean previouslyOperator = true;

		// Track consecutive unary minus '-' ('~')
		int consecutiveUnaryMinus = 0;

		for (int i = 0; i < infix.length(); i++){
			char currChar = infix.charAt(i);

			if (Character.isDigit(currChar)){
				// If currChar is a number
				if (!previouslyDigit) postfix.append(' ');
				postfix.append(currChar);
				previouslyDigit = true;
				previouslyOperator = false;
			} else {
				// If currChar is an operator
				boolean poppedUnaryMinus = false;

				if (!operatorStack.isEmpty() && operatorStack.peek().equals('~') && previouslyOperator){
					// ^ operator has higher precedence over ~
					if (currChar == '^'){
						postfix.append(' ');
						operatorStack.push(currChar);
						continue;
					} else if (previouslyDigit){
						// Add the corresponding number of ~ after the end of the corresponding number
						for (int j = 0; j < consecutiveUnaryMinus; j++){
							postfix.append(' ').append(operatorStack.pop());
						}
						consecutiveUnaryMinus = 0;
						poppedUnaryMinus = true;
					} else if (currChar == '-'){
						// Increment counter for consecutive ~
						consecutiveUnaryMinus++;
						operatorStack.push('~');
						continue;
					}
					previouslyDigit = false;
					previouslyOperator = true;
				}

				if (currChar == '('){
					// Add to bracketStack
					// Used to check balance of brackets
					bracketStack.push(currChar);

					operatorStack.push(currChar);
					previouslyDigit = false;
					previouslyOperator = true;
				} else if (currChar == '^'){
					operatorStack.push(currChar);
					previouslyDigit = false;
					previouslyOperator = true;
				} else if (currChar == ')') {
					// Check balance of brackets
					if (bracketStack.isEmpty()) throw new ArithmeticException();
					else if (!Character.isDigit(infix.charAt(i-1)) && infix.charAt(i-1) != ')') throw new ArithmeticException();
					else bracketStack.pop();

					while (!operatorStack.isEmpty() && !operatorStack.peek().equals('(')){
						postfix.append(' ');
						postfix.append(operatorStack.pop());
						previouslyDigit = false;
					}
					operatorStack.pop();
					previouslyOperator = false;
				} else if (currChar == '-' && previouslyOperator == true && poppedUnaryMinus == false){
					operatorStack.push('~');
					consecutiveUnaryMinus = 1;
					previouslyDigit = false;
					previouslyOperator = true;
				} else {
					if (operatorStack.isEmpty() || precedence(currChar) > precedence(operatorStack.peek())){
						operatorStack.push(currChar);
					} else{
						while (!operatorStack.isEmpty() && precedence(currChar) <= precedence(operatorStack.peek())){
							postfix.append(' ');
							postfix.append(operatorStack.pop());
						}
						operatorStack.push(currChar);
					}
					previouslyDigit = false;
					previouslyOperator = true;
				}
			}
		}

		// Append the remaining operators left in the operatorStack
		while (!operatorStack.isEmpty()){
			postfix.append(' ');
			postfix.append(operatorStack.pop());
		}

		// Check balance of brackets. If there are brackets left in the bracketStack, postfix expression is invalid.
		if (!bracketStack.isEmpty()) throw new ArithmeticException();

		// Remove extra space in the beginning of the postfix expression.
		return postfix.substring(1);
	}



	private static long calculatePostfix(String postfixExpression) throws ArithmeticException{
		// Initialize variables and stacks
		long num1, num2;
		Stack<Long> longStack = new Stack<>();
		int postfixValidationCounter = 0;
		String[] splittedPostfix = postfixExpression.split("\\s+");

		for (int i = 0; i < splittedPostfix.length; i++){
			// Iterate over each number or operator in the postfix expression.

			String currElement = splittedPostfix[i];
			try{
				// If currElement is a number
				long tempNumber = Long.parseLong(currElement);
				longStack.push(tempNumber);

				// Increment validation counter to validate format of the postfix expression later
				postfixValidationCounter++;
			}

			// If currElement is an operator
			catch (NumberFormatException e) {
				if (currElement.equals("~")){
					// Throw exception if there is no number before the ~ operator
					if (postfixValidationCounter <= 0) throw new ArithmeticException();

					long tempNumber = longStack.pop();
					tempNumber *= -1;
					longStack.push(tempNumber);
				} else {
					// Validate postfix expression
					// Logic: Throw exception if there is less than 2 operands before an operator
					if (postfixValidationCounter < 2) throw new ArithmeticException();

					// Pop top 2 operands from the stack
					num2 = longStack.pop();
					num1 = longStack.pop();

					// Perform operation
					long operationResult = performOperation(currElement.charAt(0), num1, num2);
					longStack.push(operationResult);

					// Validate postfix expression
					// Logic: There must be at least two number operands in the stack for each operator
					postfixValidationCounter -= 2;
					if (postfixValidationCounter < 0) throw new ArithmeticException();
					postfixValidationCounter++;
				}
			}
		}

		// Validate postfix expression
		// Logic: Throw exception if we don't end up with a single number (result) in the stack
		if (postfixValidationCounter != 1) throw new ArithmeticException();
		return longStack.pop();
	}

	private static long performOperation(char operator, long num1, long num2) throws ArithmeticException{
		long result = 0;
		if (operator == '^'){
			if (num1 == 0 && num2 < 0) {
				throw new ArithmeticException();
			}
			result = (long)Math.pow(num1, num2);
		} else if (operator == '+'){
			result = num1 + num2;
		} else if (operator == '-'){
			result = num1 - num2;
		} else if (operator == '*'){
			result = num1 * num2;
		} else if (operator == '/'){
			result = num1 / num2;
		} else if (operator == '%'){
			result = num1 % num2;
		} else {
			throw new ArithmeticException();
		}
		return result;
	}
}

