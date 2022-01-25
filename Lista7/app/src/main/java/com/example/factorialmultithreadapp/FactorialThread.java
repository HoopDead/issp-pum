package com.example.factorialmultithreadapp;

import java.math.BigInteger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

public class FactorialThread {

    private static int CHUNK_SIZE = 20;

    public BigInteger factorialOf(int providedNumber) {
        ExecutorService executor;
        if (providedNumber >= 20)
        {
            executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        }
        else
        {
            executor = Executors.newFixedThreadPool(1);
        }
        System.out.println("Running on: " + Runtime.getRuntime().availableProcessors() + " threads.");
        try {
            return IntStream.rangeClosed(0, (providedNumber - 1) / CHUNK_SIZE)
                    .mapToObj(val -> executor.submit(() -> calculate(leftBound(val), rightBound(val, providedNumber))))
                    .map(future -> getFuture(future))
                    .reduce(BigInteger.ONE, BigInteger::multiply);
        } finally {
            executor.shutdown();
        }
    }

    private static int leftBound(int chunkNo) {
        return chunkNo * CHUNK_SIZE + 1;
    }

    private static int rightBound(int chunkNo, int max) {
        return Math.min((chunkNo + 1) * CHUNK_SIZE, max);
    }

    private static BigInteger getFuture(Future<BigInteger> future) {
        try {
            return future.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static BigInteger calculate(int start, int end) {
        BigInteger res = BigInteger.valueOf(start);

        for (int val = start + 1; val <= end; val++) {
            res = res.multiply(BigInteger.valueOf(val));
        }
        return res;
    }
}
