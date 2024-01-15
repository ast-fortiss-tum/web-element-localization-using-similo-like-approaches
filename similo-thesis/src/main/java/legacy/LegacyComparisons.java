package legacy;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Contains the comparison functions used in the original Similo paper.
 */
public class LegacyComparisons {
    private static final ConcurrentHashMap<Tuple<String, String>, Integer> computeDistanceMap = new ConcurrentHashMap<>();

    public static String object2String(Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof String s) {
            return s.trim().replaceAll(";", "&59&").replaceAll("\\n", "").trim();
        } else if (o instanceof Integer i) {
            return i.toString();
        }
        if (o instanceof Double d) {
            int i = d.intValue();
            return "" + i;
        } else if (o instanceof Long l) {
            return l.toString();
        }
        return null;
    }

    public static int string2Int(String text) {
        try {
            return Integer.parseInt(text);
        } catch (Exception e) {
            return 0;
        }
    }

    public static boolean nullableCompare(String a, String b) {
        if (a == null && b == null) return true;
        else if (a == null) return false;
        else if (b == null) return false;
        else return a.equals(b);
    }

    public static double compareEqualProcessed(String t1, String t2) {
        if (t1 != null && t2 != null && !t1.isEmpty() && !t2.isEmpty()) {
            return compareEqual(t1, t2, 1);
        } else {
            return 0;
        }
    }

    public static int compareEqual(String t1, String t2, int maxScore) {
        if (t1 != null && t2 != null) {
            if (t1.equalsIgnoreCase(t2)) {
                return maxScore;
            }
        }
        return 0;
    }

    public static int computeDistance(String s1, String s2) {
        int[] costs = new int[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0) {
                    costs[j] = j;
                } else {
                    if (j > 0) {
                        int newValue = costs[j - 1];
                        if (s1.charAt(i - 1) != s2.charAt(j - 1)) {
                            newValue = Math.min(Math.min(newValue, lastValue), costs[j]) + 1;
                        }
                        costs[j - 1] = lastValue;
                        lastValue = newValue;
                    }
                }
            }
            if (i > 0) {
                costs[s2.length()] = lastValue;
            }
        }
        return costs[s2.length()];
    }

    public static int compareDistance(String t1, String t2, int maxScore) {

        Tuple<String, String> tuple = new Tuple<>(t1, t2);
        Integer result = computeDistanceMap.get(tuple);
        if (result != null) {
            return result;
        }

        String s1 = t1.toLowerCase();
        String s2 = t2.toLowerCase();

        if (s1.equals(s2)) {
            return maxScore;
        }

        // Make sure s1 is longer (or equal)
        if (s1.length() < s2.length()) {
            String swap = s1;
            s1 = s2;
            s2 = swap;
        }

        int editDistance;
        int bigLen = s1.length();
        editDistance = computeDistance(s1, s2);
        int finalScore;
        if (bigLen == 0) {
            finalScore = maxScore;
        } else {
            finalScore = (bigLen - editDistance) * maxScore / bigLen;
        }

        computeDistanceMap.put(tuple, finalScore);

        return finalScore;
    }

    public static int compareIntegerDistance(String t1, String t2, int maxScore) {
        int value1 = string2Int(t1);
        int value2 = string2Int(t2);
        return compareIntegerDistance(value1, value2, maxScore);
    }

    public static int compareIntegerDistance(int value1, int value2, int maxScore) {
        int distance = Math.abs(value1 - value2);
        int max = Math.max(value1, value2);
        if (max == 0) {
            return maxScore;
        }
        return (max - distance) * maxScore / max;
    }

    public static int compareNeighborText(String text1, String text2, int maxScore) {
        String[] words1 = text1.split("\\s+");
        String[] words2 = text2.split("\\s+");

        int existsCount = 0;
        int wordCount = Math.max(text1.length() - words1.length + 1, text2.length() - words2.length + 1);
        for (String word1 : words1) {
            if (containsWord(word1, words2)) {
                existsCount += word1.length();
            }
        }
        return Math.min((existsCount * maxScore) / wordCount, 100);
    }

    public static boolean containsWord(String containsWord, String[] words) {
        for (String word : words) {
            if (containsWord.length() < word.length() && (word.startsWith(containsWord) || word.endsWith(containsWord))) {
                return true;
            } else if (word.length() < containsWord.length() && (containsWord.startsWith(word) || containsWord.endsWith(word))) {
                return true;
            } else if (containsWord.equals(word)) {
                return true;
            }
        }
        return false;
    }

    static class Tuple<L, R> {
        private final L t1;
        private final R t2;

        public Tuple(L t1, R t2) {
            this.t1 = t1;
            this.t2 = t2;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Tuple t) {
                return t.t1.equals(t1) && t.t2.equals(t2);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return t1.hashCode() + t2.hashCode();
        }
    }
}
