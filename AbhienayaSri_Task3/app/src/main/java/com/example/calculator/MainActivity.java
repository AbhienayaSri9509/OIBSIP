package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView display;

    private String currentInput = "";
    private String operator = "";
    private double operand1 = 0.0;
    private boolean justCalculated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        display = findViewById(R.id.display);
        display.setText("0");

        // Digit buttons
        int[] digitIds = {
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9
        };
        for (int id : digitIds) {
            Button btn = findViewById(id);
            btn.setOnClickListener(v -> onDigitClick(((Button) v).getText().toString()));
        }

        // Decimal
        findViewById(R.id.btnDot).setOnClickListener(v -> onDecimalClick());

        // Operators
        findViewById(R.id.btnAdd).setOnClickListener(v -> onOperatorClick("+"));
        findViewById(R.id.btnSub).setOnClickListener(v -> onOperatorClick("-"));
        findViewById(R.id.btnMul).setOnClickListener(v -> onOperatorClick("*"));
        findViewById(R.id.btnDiv).setOnClickListener(v -> onOperatorClick("/"));

        // Equals
        findViewById(R.id.btnEquals).setOnClickListener(v -> onEqualsClick());

        // Clear
        findViewById(R.id.btnClear).setOnClickListener(v -> onClearClick());

        // Backspace
        findViewById(R.id.btnBackspace).setOnClickListener(v -> onBackspaceClick());

        // Percent
        findViewById(R.id.btnPercent).setOnClickListener(v -> onPercentClick());
    }

    private void onDigitClick(String digit) {
        if (justCalculated) {
            currentInput = "";
            justCalculated = false;
        }
        // Prevent multiple leading zeros
        if (currentInput.equals("0") && digit.equals("0")) return;
        if (currentInput.equals("0") && !digit.equals(".")) {
            currentInput = digit;
        } else {
            currentInput += digit;
        }
        updateDisplay();
    }

    private void onDecimalClick() {
        if (justCalculated) {
            currentInput = "0";
            justCalculated = false;
        }
        if (currentInput.isEmpty()) {
            currentInput = "0";
        }
        if (!currentInput.contains(".")) {
            currentInput += ".";
        }
        updateDisplay();
    }

    private void onOperatorClick(String op) {
        if (!currentInput.isEmpty()) {
            operand1 = Double.parseDouble(currentInput);
            operator = op;
            currentInput = "";
            justCalculated = false;
        } else if (!operator.isEmpty()) {
            // Update operator if no new input yet
            operator = op;
        } else {
            // Allow negation if nothing entered yet
            operator = op;
        }
    }

    private void onEqualsClick() {
        if (operator.isEmpty() || currentInput.isEmpty()) return;

        double operand2 = Double.parseDouble(currentInput);
        double result = 0.0;

        switch (operator) {
            case "+":
                result = operand1 + operand2;
                break;
            case "-":
                result = operand1 - operand2;
                break;
            case "*":
                result = operand1 * operand2;
                break;
            case "/":
                if (operand2 == 0) {
                    display.setText("Error");
                    currentInput = "";
                    operator = "";
                    justCalculated = true;
                    return;
                }
                result = operand1 / operand2;
                break;
        }

        String resultStr = formatResult(result);
        display.setText(resultStr);
        currentInput = resultStr;
        operator = "";
        justCalculated = true;
    }

    private void onClearClick() {
        currentInput = "";
        operator = "";
        operand1 = 0.0;
        justCalculated = false;
        display.setText("0");
    }

    private void onBackspaceClick() {
        if (justCalculated) return;
        if (currentInput.length() > 0) {
            currentInput = currentInput.substring(0, currentInput.length() - 1);
            updateDisplay();
        }
    }

    private void onPercentClick() {
        if (currentInput.isEmpty()) return;
        try {
            double val = Double.parseDouble(currentInput) / 100.0;
            currentInput = formatResult(val);
            updateDisplay();
        } catch (NumberFormatException e) {
            // ignore
        }
    }

    private void updateDisplay() {
        display.setText(currentInput.isEmpty() ? "0" : currentInput);
    }

    private String formatResult(double result) {
        if (result == (long) result) {
            return String.valueOf((long) result);
        } else {
            return String.valueOf(result);
        }
    }
}
