package com.example.factorialmultithreadapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.math.BigInteger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.stream.IntStream;

public class MainActivity extends AppCompatActivity {

    private EditText etFactorialInput;
    private Button bSendArgument;

    public static BigInteger sResult;
    private FactorialThread mFactorialThread;
    public static boolean sLocked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sResult = new BigInteger(String.valueOf(1));
        sLocked = false;

        mFactorialThread = new FactorialThread();

        // Assign the input.
        etFactorialInput = findViewById(R.id.etFactorialInput);

        // Asign the button.
        bSendArgument = findViewById(R.id.bSendArgument);
    }

    public void buttonSendArgumentClicked(View view) throws InterruptedException {
        int providedNumber = Integer.valueOf(etFactorialInput.getText().toString());
        sResult = new BigInteger(String.valueOf(1));

        System.out.println("Value of providedNumber is: " + providedNumber);
        System.out.println("Getting value of runnable threads: " + Runtime.getRuntime().availableProcessors());

        System.out.println("Res: " + mFactorialThread.fac(providedNumber));


        // int numberOfThreads = Runtime.getRuntime().availableProcessors();
        // ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);

        // for (int i = 1; i <= numberOfThreads; i++)
        // {
            // int start = i * (providedNumber/ numberOfThreads) + 1;
            // int end = (i + 1) * (providedNumber/numberOfThreads) + 1;
            // executor.execute(new Factorial(start, end));
        // }
        //executor.shutdown();
        //executor.awaitTermination(1, TimeUnit.DAYS);

        // Easiest way?
        // BigInteger result = IntStream.rangeClosed(1, providedNumber).parallel().mapToObj(val -> BigInteger.valueOf(val)).reduce(BigInteger.ONE, BigInteger::multiply);
        // System.out.println(result);
    }
}