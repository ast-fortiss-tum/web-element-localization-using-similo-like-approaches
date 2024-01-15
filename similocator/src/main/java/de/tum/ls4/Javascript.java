package de.tum.ls4;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Javascript {
    private final List<String> scripts = new ArrayList<>();

    public static Javascript create() {
        return new Javascript();
    }

    public Javascript addScript(String script) {
        scripts.add(script);
        return this;
    }

    public Javascript addFile(String path) {

        URL totalPath = getClass().getResource("/javascript/" + path);
        if (totalPath == null) {
            throw new RuntimeException("File not found: " + path);
        }

        try {
            scripts.add(Files.readString(Path.of(totalPath.getPath())));
        } catch (IOException e) {
            // TODO
            throw new RuntimeException(e);
        }

        return this;
    }

    public String getScript() {
        return String.join("\n", scripts);
    }

    public Object executeOn(SearchContext context, Object... args) {
        return executeOn((JavascriptExecutor) context, args);
    }

    public Object executeOn(JavascriptExecutor js, Object... args) {
        return js.executeScript(getScript(), args);
    }

    public Object executeOn(WebDriver driver, Object... args) {
        return executeOn((JavascriptExecutor) driver, args);
    }
}
