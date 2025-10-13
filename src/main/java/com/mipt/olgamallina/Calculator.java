package com.mipt.olgamallina;

public class Calculator<T extends Number> {

    private double toDouble(T x) {
        return x == null ? Double.NaN : x.doubleValue();
    }
    public double sum(T a, T b) {
        final double x = toDouble(a);
        final double y = toDouble(b);
        return x + y;
    }

    public double subtract(T a, T b) {
        final double x = toDouble(a);
        final double y = toDouble(b);
        return x - y;
    }

    public double multiply(T a, T b) {
        final double x = toDouble(a);
        final double y = toDouble(b);
        return x * y;
    }

    public double divide(T a, T b) {
        final double x = toDouble(a);
        final double y = toDouble(b);
        if (Double.isNaN(y) || y == 0.0) {
            return Double.NaN;
        }
        return x / y;
    }


    public static void main(String[] args) {
        final Calculator<Integer> intCalc = new Calculator<>();
        final double result = intCalc.sum(5, 3);
        System.out.println(result);

        final Calculator<Double> doubleCalc = new Calculator<>();
        final double div = doubleCalc.divide(10.0, 4.0);
        System.out.println(div);

        System.out.println(intCalc.sum(null, 3));
        System.out.println(doubleCalc.divide(10.0, 0.0));
        System.out.println(doubleCalc.divide(10.0, null));

    }
}
