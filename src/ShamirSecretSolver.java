// src/ShamirSecretSolver.java

import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.util.*;
import org.json.JSONObject;

public class ShamirSecretSolver {

    public static void main(String[] args) throws Exception {
        solveFromJson("testcase1.json");
        solveFromJson("testcase2.json");
    }

    public static void solveFromJson(String filename) throws Exception {
        JSONObject json = new JSONObject(new String(Files.readAllBytes(new File(filename).toPath())));
        int k = json.getJSONObject("keys").getInt("k");

        List<BigInteger> xList = new ArrayList<>();
        List<BigInteger> yList = new ArrayList<>();

        for (String key : json.keySet()) {
            if (key.equals("keys")) continue;
            int base = Integer.parseInt(json.getJSONObject(key).getString("base"));
            String valueStr = json.getJSONObject(key).getString("value");
            BigInteger y = new BigInteger(valueStr, base);
            BigInteger x = new BigInteger(key);
            xList.add(x);
            yList.add(y);
        }

        List<BigInteger> xPoints = xList.subList(0, k);
        List<BigInteger> yPoints = yList.subList(0, k);

        BigInteger secret = lagrangeInterpolation(BigInteger.ZERO, xPoints, yPoints);
        System.out.println("Secret from " + filename + " = " + secret);
    }

    public static BigInteger lagrangeInterpolation(BigInteger x, List<BigInteger> xVals, List<BigInteger> yVals) {
        BigInteger result = BigInteger.ZERO;
        int k = xVals.size();

        for (int j = 0; j < k; j++) {
            BigInteger numerator = BigInteger.ONE;
            BigInteger denominator = BigInteger.ONE;

            for (int i = 0; i < k; i++) {
                if (i != j) {
                    numerator = numerator.multiply(x.subtract(xVals.get(i)));
                    denominator = denominator.multiply(xVals.get(j).subtract(xVals.get(i)));
                }
            }

            BigInteger term = yVals.get(j).multiply(numerator).divide(denominator);
            result = result.add(term);
        }

        return result;
    }
}
