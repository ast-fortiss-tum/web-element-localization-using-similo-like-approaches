package de.tum.ls4.utils;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.openqa.selenium.By;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class Utils {
    private static String gitBranch = null;
    private static final Config config = ConfigFactory.load();

    public static String getGitBranch() {
        if (gitBranch == null) {
            gitBranch = getGitBranchFromSystem();
        }
        return gitBranch;
    }

    private static String getGitBranchFromSystem() {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(new String[]{"git", "rev-parse", "--abbrev-ref", "HEAD"});
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            StringBuilder output = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                output.append(line);
            }

            process.waitFor();
            return output.toString();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
    }

    public static By asLocator(String locator, String identifier) {
        return switch (locator) {
            case "xpath" -> By.xpath(identifier);
            case "css" -> By.cssSelector(identifier);
            case "id" -> By.id(identifier);
            case "name" -> By.name(identifier);
            case "class" -> By.className(identifier);
            case "tag" -> By.tagName(identifier);
            case "link" -> By.linkText(identifier);
            case "partialLink" -> By.partialLinkText(identifier);
            default -> throw new RuntimeException("Unknown locator: " + locator);
        };
    }

    public static String getByName(By by) {
        if (by instanceof By.ByName) {
            return "name";
        } else if (by instanceof By.ByCssSelector) {
            return "css";
        } else if (by instanceof By.ByClassName) {
            return "class";
        } else if (by instanceof By.ByLinkText) {
            return "link";
        } else if (by instanceof By.ByPartialLinkText) {
            return "partialLink";
        } else if (by instanceof By.ByTagName) {
            return "tag";
        } else if (by instanceof By.ByXPath) {
            return "xpath";
        } else {
            throw new IllegalArgumentException("Unknown By type");
        }
    }

    public static boolean getConfig(String key, boolean defaultValue) {
        return getConfig(key, Config::getBoolean, defaultValue);
    }

    public static double getConfig(String key, double defaultValue) {
        return getConfig(key, Config::getDouble, defaultValue);
    }

    public static <T> T getConfig(String key, BiFunction<Config, String, T> extract, T defaultValue) {
        if (config.hasPath(key)) {
            return extract.apply(config, key);
        } else {
            return defaultValue;
        }
    }

    public static Map<String, Object> checkedCastToMap(Object object) {
        if (object instanceof Map) {
            return checkedCastToMap(object);
        } else {
            throw new RuntimeException("Could not cast object to Map<String, Object>");
        }
    }

    public static List<Object> checkedCastToList(Object object) {
        if (object instanceof List) {
            return checkedCastToList(object);
        } else {
            throw new RuntimeException("Could not cast object to List<Object>");
        }
    }
}
