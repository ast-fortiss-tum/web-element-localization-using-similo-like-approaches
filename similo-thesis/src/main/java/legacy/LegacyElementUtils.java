package legacy;

import elements.Element;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Utility functions used to construct the properties of an element in the original Similo paper.
 */
public class LegacyElementUtils {
    public static boolean isValidText(String text) {
        if (text == null) {
            return false;
        }
        String trimmedText = text.trim();
        if (trimmedText.length() < 3 || trimmedText.length() > 50) {
            // Too short or too long
            return false;
        }
        if (trimmedText.indexOf('\n') >= 0) {
            // Contains newline
            return false;
        }
        // Contains tab
        return trimmedText.indexOf('\t') < 0;
    }

    public static boolean isButton(String tag, String type, String className) {
        if (tag == null) {
            return false;
        }
        if (tag.equalsIgnoreCase("a") && className != null && className.contains("btn")) {
            return true;
        }
        if (tag.equalsIgnoreCase("button")) {
            return true;
        }
        return tag.equalsIgnoreCase("input") && ("button".equalsIgnoreCase(type) || "submit".equalsIgnoreCase(type) || "reset".equalsIgnoreCase(type));
    }

    public static Element addNeighborTexts(Element locator, List<Element> availableLocators) {
        if (locator.getLocationArea() == null) {
            return locator;
        }
        Rectangle r = locator.getLocationArea();
        if (r.height > 100 || r.width > 600) {
            return locator;
        }
        Rectangle largerRectangle = new Rectangle(r.x - 50, r.y - 50, r.width + 100, r.height + 100);

        List<Element> neighbors = new ArrayList<>();
        for (Element available : availableLocators) {
            if (locator != available && available.getLocationArea() != null) {
                Rectangle rect = available.getLocationArea();
                if (rect.getHeight() <= 100 && largerRectangle.intersects(rect)) {
                    neighbors.add(available);
                }
            }
        }

        List<String> words = new ArrayList<>();
        Properties wordHash = new Properties();
        for (Element neighbor : neighbors) {
            String visibleText = neighbor.getVisibleText().getOrElse(() -> null);
            if (visibleText != null) {
                String[] visibleWords = visibleText.split("\\s+");
                for (String visibleWord : visibleWords) {
                    String visibleWordLower = visibleWord.toLowerCase();
                    if (!wordHash.containsKey(visibleWordLower)) {
                        wordHash.put(visibleWordLower, true);
                        words.add(visibleWordLower);
                    }
                }
            }
        }

        StringBuilder wordString = new StringBuilder();
        for (String word : words) {
            if (!wordString.isEmpty()) {
                wordString.append(" ");
            }
            wordString.append(word);
        }

        if (!wordString.isEmpty()) {
            String text = wordString.toString();
            return locator.addNeighborTexts(text);
        } else {
            return locator;
        }
    }
}
