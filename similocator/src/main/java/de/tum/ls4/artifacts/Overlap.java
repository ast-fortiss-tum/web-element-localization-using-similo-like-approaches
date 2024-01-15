package de.tum.ls4.artifacts;

import java.util.ArrayList;
import java.util.List;

public record Overlap(Element target, List<Element> overlap) implements Artifact {
    @Override
    public String json() {
        return null;
    }

    @Override
    public String xpath() {
        return target.xpathChrome();
    }

    public List<Element> allElements() {
        List<Element> allElements = new ArrayList<>();
        allElements.add(target);
        allElements.addAll(overlap);
        return allElements;
    }
}
