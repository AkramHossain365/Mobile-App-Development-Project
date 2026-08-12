package com.example.smarthallmanagement;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CalculatorActivity extends AppCompatActivity {

    private EditText firstNumber;
    private EditText secondNumber;

    private Spinner operatorSpinner;

    private Button calculateButton;

    private TextView resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_calculator);

        // Connect XML components

        firstNumber = findViewById(R.id.firstNumber);

        secondNumber = findViewById(R.id.secondNumber);

        operatorSpinner = findViewById(R.id.operatorSpinner);

        calculateButton = findViewById(R.id.calculateButton);

        resultText = findViewById(R.id.resultText);


        // Operators

        String[] operators = {
                "Addition (+)",
                "Subtraction (-)",
                "Multiplication (*)",
                "Division (/)",
                "Modulus (%)"
        };


        // Spinner Adapter

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                operators
        );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        operatorSpinner.setAdapter(adapter);


        // Calculate button

        calculateButton.setOnClickListener(v -> calculateResult());
    }


    private void calculateResult() {

        String firstInput =
                firstNumber.getText().toString().trim();

        String secondInput =
                secondNumber.getText().toString().trim();


        // First number validation

        if (firstInput.isEmpty()) {

            firstNumber.setError(
                    "Please enter the first number"
            );

            firstNumber.requestFocus();

            return;
        }


        // Second number validation

        if (secondInput.isEmpty()) {

            secondNumber.setError(
                    "Please enter the second number"
            );

            secondNumber.requestFocus();

            return;
        }


        double number1;
        double number2;


        // Convert input to numbers

        try {

            number1 = Double.parseDouble(firstInput);

            number2 = Double.parseDouble(secondInput);

        } catch (NumberFormatException e) {

            resultText.setText(
                    "Please enter valid numbers"
            );

            return;
        }


        // Get selected operator

        String operator =
                operatorSpinner
                        .getSelectedItem()
                        .toString();


        double result;


        // Perform operation

        switch (operator) {

            case "Addition (+)":

                result = number1 + number2;

                resultText.setText(
                        String.valueOf(result)
                );

                break;


            case "Subtraction (-)":

                result = number1 - number2;

                resultText.setText(
                        String.valueOf(result)
                );

                break;


            case "Multiplication (*)":

                result = number1 * number2;

                resultText.setText(
                        String.valueOf(result)
                );

                break;


            case "Division (/)":

                if (number2 == 0) {

                    resultText.setText(
                            "Error: Cannot divide by zero"
                    );

                    return;
                }

                result = number1 / number2;

                resultText.setText(
                        String.valueOf(result)
                );

                break;


            case "Modulus (%)":

                if (number2 == 0) {

                    resultText.setText(
                            "Error: Cannot calculate modulus by zero"
                    );

                    return;
                }

                result = number1 % number2;

                resultText.setText(
                        String.valueOf(result)
                );

                break;


            default:

                resultText.setText(
                        "Invalid operator"
                );

                break;
        }
    }
}