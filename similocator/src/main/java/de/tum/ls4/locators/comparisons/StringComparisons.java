package de.tum.ls4.locators.comparisons;

import org.apache.commons.text.similarity.JaccardSimilarity;
import org.apache.commons.text.similarity.JaroWinklerSimilarity;
import org.apache.commons.text.similarity.LevenshteinDistance;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class StringComparisons {
    public static double equality(String s1, String s2) {
        if (nullOrEmpty(s1, s2)) {
            return 0.0;
        }

        return s1.equals(s2) ? 1.0 : 0.0;
    }

    public static double equalityIgnoreCase(String s1, String s2) {
        if (nullOrEmpty(s1, s2)) {
            return 0.0;
        }

        return s1.equalsIgnoreCase(s2) ? 1.0 : 0.0;
    }

    public static double levenshtein(String s1, String s2) {
        if (nullOrEmpty(s1, s2)) {
            return 0.0;
        }

        if (s1.length() > 50 || s2.length() > 50) {
            return stringSet(s1, s2);
        }

        int distance = new LevenshteinDistance(50).apply(s1, s2);
        int maxLen = Math.max(s1.length(), s2.length());
        return 1.0 - (double) distance / (double) maxLen;
    }

    public static double jaccard(String s1, String s2) {
        if (nullOrEmpty(s1, s2)) {
            return 0.0;
        }

        if (s1.length() > 50 || s2.length() > 50) {
            return stringSet(s1, s2);
        }

        return new JaccardSimilarity().apply(s1, s2);
    }

    public static double jaroWinkler(String s1, String s2) {
        if (nullOrEmpty(s1, s2)) {
            return 0.0;
        }

        if (s1.length() > 50 || s2.length() > 50) {
            return stringSet(s1, s2);
        }

        return new JaroWinklerSimilarity().apply(s1, s2);
    }

    public static double stringSet(String s1, String s2) {
        if (nullOrEmpty(s1, s2)) {
            return 0.0;
        }

        String[] s1Tokens = s1.split("\\s+|\n");
        String[] s2Tokens = s2.split("\\s+|\n");

        Set<String> set1 = new HashSet<>(Arrays.asList(s1Tokens));
        Set<String> set2 = new HashSet<>(Arrays.asList(s2Tokens));

        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);

        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);

        return intersection.isEmpty() ? 0.0 : (double) intersection.size() / union.size();
    }

    private static boolean nullOrEmpty(String s1, String s2) {
        return s1 == null || s2 == null || s1.isEmpty() || s2.isEmpty();
    }
}
