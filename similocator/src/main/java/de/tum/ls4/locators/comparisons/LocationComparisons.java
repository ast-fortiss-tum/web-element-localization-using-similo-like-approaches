package de.tum.ls4.locators.comparisons;

public class LocationComparisons {
    private static final int HEIGHT = 1080;
    private static final int WIDTH = 1920;
    private static final int MAX_DISTANCE = (int) Math.sqrt(HEIGHT * HEIGHT + WIDTH * WIDTH);
    private static final double SMALL_LAMBDA = 0.005;
    private static final double MEDIUM_LAMBDA = 0.001;
    private static final double LARGE_LAMBDA = 0.01;

    public static double distanceLinear(int[] location1, int[] location2) {
        return distanceLinear(location1[0], location1[1], location2[0], location2[1]);
    }

    public static double distanceLinear(int x1, int y1, int x2, int y2) {
        return Math.max(1.0 - distance(x1, y1, x2, y2) / MAX_DISTANCE, 0.0);
    }

    public static double distanceManhattan(int[] location1, int[] location2) {
        return distanceManhattan(location1[0], location1[1], location2[0], location2[1]);
    }

    public static double distanceManhattan(int x1, int y1, int x2, int y2) {
        return Math.max(1.0 - (double) (Math.abs(x1 - x2) + Math.abs(y1 - y2)) / MAX_DISTANCE, 0.0);
    }

    public static double distanceDecaySmall(int[] location1, int[] location2) {
        return distanceDecaySmall(location1[0], location1[1], location2[0], location2[1]);
    }

    public static double distanceDecaySmall(int x1, int y1, int x2, int y2) {
        return distanceDecay(x1, y1, x2, y2, SMALL_LAMBDA);
    }

    public static double distanceDecayMedium(int[] location1, int[] location2) {
        return distanceDecayMedium(location1[0], location1[1], location2[0], location2[1]);
    }

    public static double distanceDecayMedium(int x1, int y1, int x2, int y2) {
        return distanceDecay(x1, y1, x2, y2, MEDIUM_LAMBDA);
    }

    public static double distanceDecayLarge(int[] location1, int[] location2) {
        return distanceDecayLarge(location1[0], location1[1], location2[0], location2[1]);
    }

    public static double distanceDecayLarge(int x1, int y1, int x2, int y2) {
        return distanceDecay(x1, y1, x2, y2, LARGE_LAMBDA);
    }

    private static double distance(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    private static double distanceDecay(int x1, int y1, int x2, int y2, double lambda) {
        return Math.exp(-lambda * distance(x1, y1, x2, y2));
    }
}
