package de.tum.ls4.locators.comparisons;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class MapComparisons {
    public static double intersectValueCompare(Map<String, String> aMap, Map<String, String> bMap) {
        if (aMap == null || bMap == null || aMap.isEmpty() || bMap.isEmpty()) {
            return 0.0;
        }

        int maxSize = Math.max(aMap.size(), bMap.size());

        Set<String> aKeys = new HashSet<>(aMap.keySet());
        Set<String> bKeys = bMap.keySet();

        aKeys.retainAll(bKeys);

        int intersect = 0;

        for (String key : aKeys) {
            if (Objects.equals(aMap.get(key), bMap.get(key))) {
                intersect++;
            }
        }

        return (double) intersect / (double) maxSize;
    }

    public static double intersectKeyCompare(Map<String, String> aMap, Map<String, String> bMap) {
        if (aMap == null || bMap == null || aMap.isEmpty() || bMap.isEmpty()) {
            return 0.0;
        }

        Set<String> aKeys = aMap.keySet();
        Set<String> bKeys = bMap.keySet();

        Set<String> union = new HashSet<>(aKeys);
        union.addAll(bKeys);

        Set<String> intersect = new HashSet<>(aKeys);
        intersect.retainAll(bKeys);

        return intersect.isEmpty() ? 0.0 : (double) intersect.size() / (double) union.size();
    }
}
