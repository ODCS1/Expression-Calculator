package com.project.project;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;



public class Controller {

    @FXML
    private TextField textfieldExpression;

    @FXML
    private Button buttomCalculate;

    @FXML
    private Label textResult;

    @FXML
    private Label textInstructions;

    @FXML
    public void initialize() {
        textInstructions.setText("Welcome! Enter an expression to calculate.");
        textResult.setText("The result will appear here.");

        buttomCalculate.setOnAction(event -> {
            try {
                calculateExpression();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void calculateExpression() {
        String expression = textfieldExpression.getText();
        CalculateNumericalExpression calcula = new CalculateNumericalExpression();

        try {
            if (calcula.isValidInput(expression)) {
                String resultado = calcula.calculateExpression(expression);
                textInstructions.setText("Expression calculated!");
                textResult.setText("The result of this expression is: " + resultado);
            } else {
                textResult.setText("Waiting for a new expression...");
                textInstructions.setText("Error: " + calcula.getErrorText());
                System.out.println("Invalid Expression: " + calcula.getErrorText());
            }
        } catch (Exception e) {
            textInstructions.setText("Unexpected error: " + e.getMessage());
            textResult.setText("Review your expression.");
        }
    }
}