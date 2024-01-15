package de.tum.ls4.locators.comparisons;

public class DimensionComparisons {
    public static double area(int[] dimensions1, int[] dimensions2) {
        return area(dimensions1[0], dimensions1[1], dimensions2[0], dimensions2[1]);
    }

    public static double area(int width1, int height1, int width2, int height2) {
        return ratio(width1 * height1, width2 * height2);
    }

    public static double aspectRatio(int[] dimensions1, int[] dimensions2) {
        return aspectRatio(dimensions1[0], dimensions1[1], dimensions2[0], dimensions2[1]);
    }

    public static double aspectRatio(int width1, int height1, int width2, int height2) {
        if (width1 == 0 || height1 == 0 || width2 == 0 || height2 == 0) {
            return 0.0;
        }

        return ration((double) width1 / (double) height1, (double) width2 / (double) height2);
    }

    public static double perimeter(int[] dimensions1, int[] dimensions2) {
        return perimeter(dimensions1[0], dimensions1[1], dimensions2[0], dimensions2[1]);
    }

    public static double perimeter(int width1, int height1, int width2, int height2) {
        return ratio(width1 * 2 + height1 * 2, width2 * 2 + height2 * 2);
    }

    private static double ratio(int a, int b) {
        return ration(a, b);
    }

    private static double ration(double a, double b) {
        if (a == 0 || b == 0) {
            return 0.0;
        }
        return Math.min(a, b) / Math.max(a, b);
    }
}
