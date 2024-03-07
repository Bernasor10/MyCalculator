package com.example.mycalculator;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Switch;
import java.math.BigDecimal;
import java.math.RoundingMode;
import androidx.constraintlayout.widget.ConstraintLayout;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    private BigDecimal firstValue, secondValue, result, lastSecondValue;
    private String operator;
    private boolean lastCalculation;
    private Switch themeSwitch;
    private ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);
        textView.setText("0");
        constraintLayout = findViewById(R.id.constraintLayout);
        themeSwitch = findViewById(R.id.themeSwitch);

        // Restore the text of the textView if the activity is being recreated
        if (savedInstanceState != null) {
            String textViewText = savedInstanceState.getString("textViewText", "0");
            textView.setText(textViewText);
        } else {
            textView.setText("0");
        }

        // Number buttons
        Button buttonOne = findViewById(R.id.buttonOne);
        buttonOne.setOnClickListener(v -> appendNumber("1"));

        Button buttonTwo = findViewById(R.id.buttonTwo);
        buttonTwo.setOnClickListener(v -> appendNumber("2"));

        Button buttonThree = findViewById(R.id.buttonThree);
        buttonThree.setOnClickListener(v -> appendNumber("3"));

        Button buttonFour = findViewById(R.id.buttonFour);
        buttonFour.setOnClickListener(v -> appendNumber("4"));

        Button buttonFive = findViewById(R.id.buttonFive);
        buttonFive.setOnClickListener(v -> appendNumber("5"));

        Button buttonSix = findViewById(R.id.buttonSix);
        buttonSix.setOnClickListener(v -> appendNumber("6"));

        Button buttonSeven = findViewById(R.id.buttonSeven);
        buttonSeven.setOnClickListener(v -> appendNumber("7"));

        Button buttonEight = findViewById(R.id.buttonEight);
        buttonEight.setOnClickListener(v -> appendNumber("8"));

        Button buttonNine = findViewById(R.id.buttonNine);
        buttonNine.setOnClickListener(v -> appendNumber("9"));

        Button buttonZero = findViewById(R.id.buttonZero);
        buttonZero.setOnClickListener(v -> appendNumber("0"));

        // Operator buttons
        Button buttonAdd = findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(v -> setOperator("+"));

        Button buttonSubtract = findViewById(R.id.buttonSubtract);
        buttonSubtract.setOnClickListener(v -> setOperator("-"));

        Button buttonMultiply = findViewById(R.id.buttonMultiply);
        buttonMultiply.setOnClickListener(v -> setOperator("*"));

        Button buttonDivide = findViewById(R.id.buttonDivide);
        buttonDivide.setOnClickListener(v -> setOperator("/"));

        Button buttonDecimal = findViewById(R.id.buttonDecimal);
        buttonDecimal.setOnClickListener(v -> appendNumber("."));

        // Equal button
        Button buttonEqual = findViewById(R.id.buttonEqual);
        buttonEqual.setOnClickListener(v -> calculate());

        // Clear button
        Button buttonClear = findViewById(R.id.buttonClear);
        buttonClear.setOnClickListener(v -> clear());

        // Delete button
        Button buttonDelete = findViewById(R.id.buttonDelete);
        buttonDelete.setOnClickListener(v -> deleteLastCharacter());

        // Theme switch
        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Toggle is enabled, switch to light mode
                constraintLayout.setBackgroundColor(getResources().getColor(R.color.white));
                themeSwitch.setText("Light Mode");
                themeSwitch.setTextColor(getResources().getColor(R.color.black));
                textView.setTextColor(getResources().getColor(R.color.black));
            } else {
                // Toggle is disabled, switch to dark mode
                constraintLayout.setBackgroundColor(getResources().getColor(R.color.black));
                themeSwitch.setText("Dark Mode");
                themeSwitch.setTextColor(getResources().getColor(R.color.white));
                textView.setTextColor(getResources().getColor(R.color.white));
            }
        });
    }

    private void appendNumber(String number) {
        if (textView.getText().toString().equals("Cannot divide by 0")) {
            clear(); // Clear the error message
        }
        if (number.equals(".") && textView.getText().toString().contains(".")) {
            return; // Avoid adding multiple decimal points
        }
        if ((textView.getText().toString().equals("0") || lastCalculation) && !number.equals(".")) {
            textView.setText(number); // Replace "0" or clear if last operation was calculation
        } else {
            textView.append(number);
        }
        lastCalculation = false; // Reset the flag
    }

    private void setOperator(String op) {
        if (textView.getText().toString().equals("Cannot divide by 0")) {
            clear(); // Clear the error message
        }
        if (!textView.getText().toString().isEmpty()) {
            firstValue = new BigDecimal(textView.getText().toString());
            operator = op;
            textView.setText("");
        }
        lastCalculation = false; // Reset the flag
    }

    private void calculate() {
        if (operator == null) {
            return; // Do nothing if no operator has been set
        }

        try {
            if (lastCalculation) {
                // Use the last secondValue for repeated calculations
                firstValue = result;
            } else if (!textView.getText().toString().isEmpty()) {
                secondValue = new BigDecimal(textView.getText().toString());
                lastSecondValue = secondValue; // Store the secondValue for repeated calculations
            }

            switch (operator) {
                case "+":
                    result = firstValue.add(lastSecondValue);
                    break;
                case "-":
                    result = firstValue.subtract(lastSecondValue);
                    break;
                case "*":
                    result = firstValue.multiply(lastSecondValue);
                    break;
                case "/":
                    if (lastSecondValue.compareTo(BigDecimal.ZERO) == 0) {
                        textView.setText("Cannot divide by 0");
                        return;
                    } else {
                        result = firstValue.divide(lastSecondValue, 10, RoundingMode.HALF_UP);
                    }
                    break;
            }
            textView.setText(result.stripTrailingZeros().toPlainString());
            firstValue = result; // Update firstValue for chained calculations
            lastCalculation = true; // Set the flag to indicate the last operation was a calculation
        } catch (Exception e) {
            textView.setText("Error");
        }
    }

    private void clear() {
        textView.setText("0");
        firstValue = null;
        secondValue = null;
        lastSecondValue = null;
        operator = null;
        result = null;
        lastCalculation = false; // Reset the flag
    }

    private void deleteLastCharacter() {
        if (textView.getText().toString().equals("Cannot divide by 0")) {
            clear(); // Clear the error message
            return;
        }
        String currentText = textView.getText().toString();
        if (!currentText.isEmpty() && !currentText.equals("0")) {
            textView.setText(currentText.substring(0, currentText.length() - 1));
        }
        if (textView.getText().toString().isEmpty()) {
            textView.setText("0"); // Set text to "0" if empty
        }
    }
}