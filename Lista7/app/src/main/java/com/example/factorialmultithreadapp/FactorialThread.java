package com.example.factorialmultithreadapp;

import java.math.BigInteger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

public class FactorialThread {

    private static int CHUNK_SIZE = 20;

    public BigInteger fac(int providedNumber) {
        if (providedNumber >= 20)
        {
            System.out.println("Running on: " + Runtime.getRuntime().availableProcessors() + " threads.");
            ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
            try {
                return IntStream.rangeClosed(0, (providedNumber - 1) / CHUNK_SIZE)
                        .mapToObj(val -> executor.submit(() -> prod(leftBound(val), rightBound(val, providedNumber))))
                        .map(future -> valueOf(future))
                        .reduce(BigInteger.ONE, BigInteger::multiply);
            } finally {
                executor.shutdown();
            }
        }
        else
        {
            System.out.println("Running on ONE thread.");
            return prod(1, providedNumber);
        }
    }

    private static int leftBound(int chunkNo) {
        return chunkNo * CHUNK_SIZE + 1;
    }

    private static int rightBound(int chunkNo, int max) {
        return Math.min((chunkNo + 1) * CHUNK_SIZE, max);
    }

    private static BigInteger valueOf(Future<BigInteger> future) {
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
