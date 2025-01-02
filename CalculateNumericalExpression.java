package com.project.project;

import java.util.Scanner;
import java.util.StringTokenizer;

public class CalculateNumericalExpression {
    private String infixExpression;
    private String result;
    private Queue<String> queue;
    private Stack<String> stack;
    private Boolean[][] table;
    private String[] operators;
    private String errorText;

    private int NOT_FOUND;

    public CalculateNumericalExpression() {
        this.queue = new Queue<>();
        this.stack = new Stack<>();

        this.table = new Boolean[][]{
                {false, false, false, false, false, false, true},
                {false, false, true, true, true, true, true},
                {false, false, true, true, true, true, true},
                {false, false, true, true, true, true, true},
                {false, false, false, false, true, true, true},
                {false, false, false, false, true, true, true},
                {false, false, false, false, false, false, false}
        };

        this.operators = new String[]{ "(", "^", "*", "/", "+", "-", ")" };

        this.NOT_FOUND = -1;
    }

    private void menu() {
        System.out.println("CALCULATOR - ENTER THE NUMERICAL EXPRESSION");
        System.out.println("The following operators are accepted: \n(+) ADDITION \n(-) SUBTRACTION \n(*) MULTIPLICATION \n(/) DIVISION \n(^) EXPONENTIATION \nPARENTHESES ARE ALLOWED");
    }

    private void input() {
        Scanner input = new Scanner(System.in);

        System.out.println("Enter the expression: ");
        String currentExpression = input.nextLine().trim();

        while (!isValidInput(currentExpression)) {
            System.out.println(this.errorText);
            System.out.println("Enter the expression again: ");
            currentExpression = input.nextLine().trim();
        }

        input.close();
    }

    public void calculateMain() throws Exception{

        menu();
        input();

        if (!infixToPostfix()) { throw new Exception("An error occurred while converting the expression to postfix! "); }

        calculatePostfixExpressionResult();
        System.out.println("Result: " + this.result);
    }


    public Boolean isValidInput(String input) {
        if (input.isEmpty()) {
            this.errorText = "The expression cannot be empty!";
            return false;
        }

        // CANNOT START WITH ARITHMETIC OPERATORS OR )
        if ( Character.toString(input.charAt(0)).matches("[+\\-*/^)]") ) {
            this.errorText = "Do not place operators '+', '-', '*', '/', '^', or ')' at the beginning!";
            return false;
        }

        StringBuilder result = new StringBuilder();
        char previous = ' ';
        int open = 0;
        int close = 0;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            if (c == ' ') {
                previous = ' ';
                continue;
            }

            // CHECKS IF THERE ARE 2 NUMBERS WITHOUT AN OPERATOR BETWEEN THEM
            if (i > 0 && isNumber(Character.toString(c)) && isNumber(Character.toString(result.charAt(result.length() - 1))) && previous == ' ') {
                this.errorText = "Do not place numbers without an operator between them!";
                return false;
            }

            if ((Character.toString(c).matches("[+\\-*/^]") && Character.toString(result.charAt(result.length() - 1)).matches("[+\\-*/^]"))){
                this.errorText = "Do not place the operators '+', '-', '*', and '*' consecutively! ";
                return false;
            }

            if ((i == input.length() - 1) && (Character.toString(c).matches("[+\\-*/^]"))){
                this.errorText = "Do not place the operators '+', '-', '*' and '*' without a number after them to calculate!";
                return false;
            }

            if (i > 0) {
                // HANDLING ERRORS WITH PARENTHESES in the middle of the expression
                if (c == '(' && result.charAt(result.length() - 1) == ')'){
                    this.errorText = "Do not place parentheses without an arithmetic operator between them ') ('! ";
                    return false;
                }
                if (c == ')' && result.charAt(result.length() - 1) == '('){
                    this.errorText = "Do not place parentheses without internal elements '( )'! ";
                    return false;
                }
                if (c == '(' && isNumber(Character.toString(result.charAt(result.length() - 1)))){
                    this.errorText = String.format("Do not place number and open parenthesis consecutively '%c('! ", result.charAt(result.length() - 1));
                    return false;
                }
                if (Character.toString(c).matches("[+\\-*/^]") && result.charAt(result.length() - 1) == '('){
                    this.errorText = "Do not place operator after open parenthesis '(*', '(/', '(+', '(-' and '(^' !";
                    return false;
                }
                if (c == ')' && Character.toString(result.charAt(result.length() - 1)).matches("[+\\-*/^]")){
                    this.errorText = "Do not place operator before closing parenthesis '*)', '/)', '+)', '-)' and '^)' !";
                    return false;
                }
            }

            // CORRECTLY ADD
            if (Character.toString(c).matches("[+\\-*/^()]") || isNumber(Character.toString(c))){
                if (c == '(') { open++; }
                if (c == ')') { close++; }

                result.append(c);
                previous = c;
                continue;
            }

            this.errorText = "Do not place characters outside of the specified ones!";
            return false;
        }

        if (open != close) {
            this.errorText = "Ensure you open and close parentheses the same amount! ";
            return false;
        }

        this.infixExpression = result.toString();
        return true;
    }

    private Boolean isNumber(String n){
        try {
            Integer.parseInt(n);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    private int operatorPositionInStack() throws Exception {
        if (this.stack.isEmpty()) {
            return NOT_FOUND;
        }
        String top = this.stack.peek();
        int topOperatorPos;
        for (topOperatorPos = 0; topOperatorPos < this.operators.length; topOperatorPos++) {
            if (operators[topOperatorPos].equals(top)) return topOperatorPos;
        }
        return NOT_FOUND;
    }


    private int operatorPositionInSequence(String current){
        int opSeqPos;
        for (opSeqPos = 0; opSeqPos < this.operators.length; opSeqPos++){
            if (operators[opSeqPos].equals(current)) return opSeqPos;
        }
        return NOT_FOUND;
    }


    private Boolean operatorCheckElementInStack(String current) throws Exception {
        if (!this.stack.isEmpty()) {
            int opStackPos = operatorPositionInStack();
            int opSeqPos = operatorPositionInSequence(current);

            if (opSeqPos == NOT_FOUND) return false; // FAILED (USER TYPED INCORRECTLY)

            while (this.table[opStackPos][opSeqPos]) { // REMOVE TOP OF STACK
                this.queue.enqueue(this.stack.peek());
                this.stack.pop();

                opStackPos = operatorPositionInStack();

                if (opStackPos == -1) break; // STACK EMPTY, CAN ENQUEUE THE ELEMENT DIRECTLY
            }
        }

        this.stack.push(current);
        return true;
    }


    private Boolean popUntilCloseParenthesis() throws Exception {
        String top;
        while (!this.stack.isEmpty()) {
            top = this.stack.peek();
            this.stack.pop();
            if (top.equals("(")) {
                return true;
            }
            this.queue.enqueue(top);
        }
        return false; // USER ERROR FOR NOT "MATCHING" OPEN AND CLOSE ()
    }


    private Boolean infixToPostfix() throws Exception {

        StringTokenizer tokenizer = new StringTokenizer(this.infixExpression, "+-*/^()", true);
        String current;

        while (tokenizer.hasMoreTokens()) {
            current = tokenizer.nextToken().trim();

            // ADD DIRECTLY TO QUEUE IF IT'S A NUMBER
            if (isNumber(current)) {
                queue.enqueue(current);
                continue;
            }

            // PUSH DIRECTLY
            if (current.equals("(")) {
                stack.push(current);
                continue;
            }

            // POP UNTIL FIND "("
            if (current.equals(")")) {
                if (!popUntilCloseParenthesis()) return false;
                continue;
            }

            // OPERATOR
            if (current.matches("[+\\-*/^]")) {
                if (!operatorCheckElementInStack(current)) return false;
            }
        }

        // PROCESSED ALL TOKENS, POP ELEMENTS IF EXIST
        while (!stack.isEmpty()) {
            queue.enqueue(stack.peek());
            stack.pop();
        }

        return true;
    }

    private String calculateV1OpV2(double v1, double v2, char op){
        double result;
        switch (op) {
            case '+' -> result = v1 + v2;

            case '-' -> result = v1 - v2;

            case '*' -> result = v1 * v2;

            case '/' -> result = v1 / v2;

            case '^' -> result = Math.pow(v1, v2);

            default -> {
                result = 0;
                System.out.println("UNKNOWN OPERATOR");
            }
        }
        return Double.toString(result);
    }

    public Boolean calculatePostfixExpressionResult() throws Exception {
        double v1 = 0.0;
        double v2 = 0.0;
        char op = ' ';
        String current;

        while (!this.queue.isEmpty()) {

            current = this.queue.peek();


            // OPERATOR
            if (current.matches("[+\\-*/^]")) {
                op = current.charAt(0);
                v2 = Double.parseDouble(this.stack.peek());
                this.stack.pop();

                v1 = Double.parseDouble(this.stack.peek());
                this.stack.pop();

                this.stack.push(calculateV1OpV2(v1, v2, op));
            }


            // ADD DIRECTLY TO THE STACK IF IT IS A NUMBER
            if (isNumber(current)) {
                stack.push(current);
            }

            this.queue.dequeue();

        }

        this.result = this.stack.peek();
        this.stack.pop();

        return true;
    }

    public String calculateExpression(String expression) throws Exception {
        if (!isValidInput(expression)) { throw new IllegalArgumentException("You passed an invalid expression! "); }

        if(!infixToPostfix()) { throw new Exception("Unable to convert the expression to postfix! "); }

        if (!calculatePostfixExpressionResult()) { throw new Exception("Unable to calculate the final result! "); }


        return this.result;
    }

    public String getResult() {
        return result;
    }

    public String getErrorText() {
        return errorText;
    }

    public String getPassedInfixExpression(){
        return this.infixExpression;
    }

}
