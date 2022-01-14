package com.example.factorialmultithreadapp;

import java.math.BigInteger;

public class Factorial implements Runnable {

    private int test;
    private BigInteger mFactorial;
    private int mStart;
    private int mEnd;

    public Factorial(int start, int end)
    {
        test = start;
        mStart = start;
        mEnd = end;

        System.out.println(mStart + " " + mEnd);
    }

    @Override
    public void run()
    {
        for (int i = mStart; i <= mEnd; i++)
        {
            test *= i;
        }

        System.out.println("Res: " + test);
    }

    public void Print()
    {
        System.out.println("Factorial result: " + mFactorial);
    }
}
