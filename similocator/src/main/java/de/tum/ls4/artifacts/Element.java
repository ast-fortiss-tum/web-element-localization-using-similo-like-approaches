package de.tum.ls4.artifacts;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static de.tum.ls4.utils.Utils.checkedCastToMap;

public class Element implements Artifact {
    // Basic Properties
    public final String tag;
    public final String innerText;
    public final String nodeText;
    public final int childrenLength;
    // Identification Properties
    public final Xpath xpath;
    public final String cssSelector;
    // Position & Size
    public final int x;
    public final int y;
    public final int width;
    public final int height;
    // Attributes
    public final Map<String, String> attributes;
    public final Map<String, String> cssStyles;
    public String neighborText;

    public Element(
            String tag,
            String nodeText,
            String innerText,
            int childrenLength,
            Xpath xpath,
            String cssSelector,
            int x, int y,
            int width, int height,
            Map<String, String> attributes,
            Map<String, String> cssStyles
    ) {
        this.tag = tag;
        this.innerText = innerText;
        this.nodeText = nodeText;
        this.childrenLength = childrenLength;
        this.xpath = xpath;
        this.cssSelector = cssSelector;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.attributes = attributes;
        this.cssStyles = cssStyles;
    }

    public Element(Map<String, Object> map) {
        this.tag = (String) map.get("tag");
        this.innerText = (String) map.get("innerText");
        this.nodeText = (String) map.get("nodeText");
        this.childrenLength = (int) (long) map.get("childrenLength");
        this.xpath = new Xpath(
                (String) checkedCastToMap(map.get("xpath")).get("chrome"),
                (String) checkedCastToMap(map.get("xpath")).get("selenium"),
                (String) checkedCastToMap(map.get("xpath")).get("id")
        );
        this.cssSelector = (String) map.get("cssSelector");
        this.x = (int) (long) map.get("x");
        this.y = (int) (long) map.get("y");
        this.width = (int) (long) map.get("width");
        this.height = (int) (long) map.get("height");
        this.attributes = safeCastToMap(map.get("attributes"));
        this.cssStyles = safeCastToMap(map.get("cssStyles"));
    }

    private static Map<String, String> safeCastToMap(Object obj) {
        if (obj instanceof Map<?, ?> rawMap) {
            Map<String, String> stringMap = new HashMap<>();
            for (Map.Entry<?, ?> entry : rawMap.entrySet()) {
                if (!(entry.getKey() instanceof String) || !(entry.getValue() instanceof String)) {
                    throw new IllegalArgumentException("Map contains non-String key/value");
                }
                stringMap.put((String) entry.getKey(), (String) entry.getValue());
            }
            return stringMap;
        } else {
            throw new IllegalArgumentException("Object is not a Map<String, String>");
            // Or return a default value, e.g., Collections.emptyMap();
        }
    }

    private StringBuilder appendMapJson(StringBuilder sb, Map<String, String> map) {
        sb.append("{");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            sb.append("\"")
                    .append(escape(entry.getKey()))
                    .append("\":\"")
                    .append(escape(entry.getValue()))
                    .append("\",");
        }
        if (!map.isEmpty()) {
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append("}");
        return sb;
    }

    private String escape(String s) {
        if (s == null) return null;
        return s.replace("\"", "\\\"");
    }

    public static class Xpath {
        public final String chrome;
        public final String selenium;
        public final String id;

        public Xpath(String chrome, String selenium, String id) {
            this.chrome = chrome;
            this.selenium = selenium;
            this.id = id;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (!(o instanceof Xpath xpath)) {
                return false;
            }

            return Objects.equals(chrome, xpath.chrome) &&
                    Objects.equals(selenium, xpath.selenium) &&
                    Objects.equals(id, xpath.id);
        }
    }

    public int area() {
        return width * height;
    }

    public int shape() {
        return height == 0 ? 0 : (width * 100) / height;
    }

    public String typeName() {
        return attributes.get("type");
    }

    public String className() {
        return attributes.get("class");
    }

    public String value() {
        return attributes.get("value");
    }

    public String placeholder() {
        return attributes.get("placeholder");
    }

    public Map<String, String> attributes() {
        return attributes;
    }

    public String tag() {
        return tag;
    }

    public String name() {
        return attributes.get("name");
    }

    public String id() {
        return attributes.get("id");
    }

    public String href() {
        return attributes.get("href");
    }

    public String alt() {
        return attributes.get("alt");
    }

    public String ariaLabel() {
        return attributes.get("aria-label");
    }

    public String xpathChrome() {
        return xpath.chrome;
    }

    public String xpathId() {
        return xpath.id;
    }

    public int[] location() {
        return new int[]{x, y};
    }

    public int[] dimensions() {
        return new int[]{width, height};
    }

    public String neighborText() {
        return neighborText;
    }

    public boolean isButton() {
        if (tag == null) return false;
        if ("a".equals(tag) && Objects.requireNonNullElse(className(), "").contains("btn")) return true;
        if ("button".equals(tag)) return true;
        return "input".equals(tag) && ("button".equals(typeName()) ||
                "submit".equals(typeName()) || "reset".equals(typeName()));
    }

    public String visibleText() {
        return Stream.of(innerText, value(), placeholder())
                .filter(Objects::nonNull)
                .filter(s -> !s.trim().isEmpty())
                .findFirst().orElse(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Element element)) {
            return false;
        }

        return childrenLength == element.childrenLength &&
                x == element.x && y == element.y &&
                width == element.width && height == element.height &&
                Objects.equals(tag, element.tag) &&
                Objects.equals(innerText, element.innerText) &&
                Objects.equals(nodeText, element.nodeText) &&
                Objects.equals(xpath, element.xpath) &&
                Objects.equals(cssSelector, element.cssSelector) &&
                Objects.equals(attributes, element.attributes) &&
                Objects.equals(cssStyles, element.cssStyles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tag, nodeText, innerText, childrenLength, xpath,
                cssSelector, x, y, width, height, attributes, cssStyles);
    }

    public String json() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"tag\":\"").append(escape(tag)).append("\",");
        sb.append("\"innerText\":\"").append(escape(innerText)).append("\",");
        sb.append("\"nodeText\":\"").append(escape(nodeText)).append("\",");
        sb.append("\"xpath\":{");
        sb.append("\"chrome\":\"").append(escape(xpath.chrome)).append("\",");
        sb.append("\"selenium\":\"").append(escape(xpath.selenium)).append("\",");
        sb.append("\"id\":\"").append(escape(xpath.id)).append("\"");
        sb.append("},");
        sb.append("\"cssSelector\":\"").append(escape(cssSelector)).append("\",");
        sb.append("\"x\":").append(x).append(",");
        sb.append("\"y\":").append(y).append(",");
        sb.append("\"width\":").append(width).append(",");
        sb.append("\"height\":").append(height).append(",");
        sb.append("\"attributes\":");
        appendMapJson(sb, attributes).append(",");
        sb.append("\"cssStyles\":");
        appendMapJson(sb, cssStyles);
        sb.append("}");

        return sb.toString();
    }

    @Override
    public String xpath() {
        return xpathChrome();
    }
}
