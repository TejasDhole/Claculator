package com.tejas.claculator;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
public class MainActivity extends AppCompatActivity {

    private TextView displayArea;
    private StringBuilder currentInput;
    private String currentOperation = "";
    private boolean isResultDisplayed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        displayArea = findViewById(R.id.display_area);
        currentInput = new StringBuilder();
    }

    public void onNumberClick(View view) {
        if (isResultDisplayed) {
            clearDisplay();
            isResultDisplayed = false;
        }
        Button button = (Button) view;
        currentInput.append(button.getText().toString());
        updateDisplay(currentOperation + currentInput.toString());
    }

    public void onOperatorClick(View view) {
        if (isResultDisplayed) {
            isResultDisplayed = false;
        }
        Button button = (Button) view;
        if (currentInput.length() > 0) {
            currentOperation += currentInput.toString();
            currentInput.setLength(0); // Clear current input
        }
        currentOperation += button.getText().toString();
        updateDisplay(currentOperation);
    }

    public void onEqualsClick(View view) {
        if (!isResultDisplayed && currentInput.length() > 0) {
            currentOperation += currentInput.toString();
            double result = performCalculation();
            updateDisplay(currentOperation + " = " + result);
            isResultDisplayed = true;
        }
    }

    public void onClearClick(View view) {
        clearDisplay();
        currentOperation = "";
        isResultDisplayed = false;
    }

    private void clearDisplay() {
        displayArea.setText("0");
        currentInput.setLength(0);
    }

    private void updateDisplay(String text) {
        displayArea.setText(text);
    }

    private double performCalculation() {
        double result = 0.0;
        try {
            result = evaluateExpression(currentOperation);
        } catch (ArithmeticException | NumberFormatException e) {
            // Handle any arithmetic exceptions or invalid expressions
            result = Double.NaN; // Indicate an error in calculation
        }
        return result;
    }

    private double evaluateExpression(String expression) throws ArithmeticException, NumberFormatException {
        String[] additionParts = expression.split("\\+");
        if (additionParts.length > 1) {
            double sum = 0.0;
            for (String additionPart : additionParts) {
                sum += evaluateExpression(additionPart);
            }
            return sum;
        } else {
            String[] subtractionParts = expression.split("-");
            if (subtractionParts.length > 1) {
                double subtractionResult = evaluateExpression(subtractionParts[0]);
                for (int i = 1; i < subtractionParts.length; i++) {
                    subtractionResult -= evaluateExpression(subtractionParts[i]);
                }
                return subtractionResult;
            } else {
                String[] multiplicationParts = expression.split("\\*");
                if (multiplicationParts.length > 1) {
                    double multiplicationResult = 1.0;
                    for (String multiplicationPart : multiplicationParts) {
                        multiplicationResult *= evaluateExpression(multiplicationPart);
                    }
                    return multiplicationResult;
                } else {
                    String[] divisionParts = expression.split("/");
                    if (divisionParts.length > 1) {
                        double divisionResult = evaluateExpression(divisionParts[0]);
                        for (int i = 1; i < divisionParts.length; i++) {
                            double operand = evaluateExpression(divisionParts[i]);
                            if (operand != 0.0) {
                                divisionResult /= operand;
                            } else {
                                // Handle division by zero error
                                throw new ArithmeticException("Division by zero error");
                            }
                        }
                        return divisionResult;
                    } else {
                        return Double.parseDouble(expression);
                    }
                }
            }
        }
    }

}
