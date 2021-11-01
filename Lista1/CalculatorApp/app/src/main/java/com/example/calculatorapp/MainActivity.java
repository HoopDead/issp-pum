package com.example.calculatorapp;

import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class MainActivity extends AppCompatActivity {

    private long equationResult; // Numeric representation of equation result.
    private TextView equationDisplay; // TextView that displays actual state of equation.
    private String equationText = ""; // String representation of equation.
    private boolean operationLocked; // Checks, if {+, -, /, *} was used.

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        equationDisplay = findViewById(R.id.equation_result);
        operationLocked = false;

        if (savedInstanceState != null)
        {
            equationResult = savedInstanceState.getLong("equationResultState");
            equationText = savedInstanceState.getString("equationTextState");
            operationLocked = savedInstanceState.getBoolean("operationLockedState");

            equationDisplay.setText(equationText);
        }
    }

    @SuppressLint("SetTextI18n")
    public void numberButtonClicked(View view)
    {
        Button button = (Button) view;

        if (button != null)
        {
            equationText = equationText.concat(button.getText().toString());
        }

        if (equationDisplay != null)
        {
            equationDisplay.setText(equationText);
        }
    }

    @SuppressLint("SetTextI18n")
    public void clearButtonClicked(View view)
    {
        equationText = "";
        equationResult = 0;
        operationLocked = false;

        if (equationDisplay != null)
        {
            equationDisplay.setText("");
        }
    }

    @SuppressLint("SetTextI18n")
    public void operationButtonClicked(View view)
    {
        // Check if there are any numbers already and if operation is not empty
        if (!operationLocked && !equationText.isEmpty())
        {
            Button button = (Button) view;

            if (button != null)
            {
                equationText = equationText.concat(button.getText().toString());
            }

            if (equationDisplay != null)
            {
                equationDisplay.setText(equationText);
            }

            operationLocked = true;
        }
    }

    @SuppressLint("SetTextI18n")
    public void solveButtonClicked(View view)
    {
        try
        {
            Expression exec = new ExpressionBuilder(equationText).build();
            equationResult = (long) exec.evaluate();
            equationText = Long.toString(equationResult);
            equationDisplay.setText(equationText);
            operationLocked = false;
        }
        // Catch error with division by zero, or any arithmetic execption.
        catch (ArithmeticException e)
        {
            equationResult = 0;
            equationText = "";
            equationDisplay.setText("Nie dziel przez 0!");
            operationLocked = false;
        }
        // Catch error with unknown two sides of equation, or any illegalargument.
        catch (IllegalArgumentException e)
        {
            equationResult = 0;
            equationText = "";
            equationDisplay.setText("Musisz dostarczyć dwie strony równania!");
            operationLocked = false;
        }
        // Catch any exception if something fails.
        catch (Exception e)
        {
            equationResult = 0;
            equationText = "";
            equationDisplay.setText("Unknown error!");
            operationLocked = false;
        }
    }

        @Override
        protected void onSaveInstanceState(@NonNull Bundle outState)
        {
            super.onSaveInstanceState(outState);

            outState.putLong("equationResultState", equationResult);
            outState.putString("equationTextState", equationText);
            outState.putBoolean("operationLockedState", operationLocked);
        }
    }