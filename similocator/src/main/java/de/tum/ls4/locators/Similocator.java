package de.tum.ls4.locators;

import org.openqa.selenium.By;

public class Similocator {
    /**
     * Creates a new instance of the Similocator utilizing the basic Similo algorithm to wrap the given By instance.
     *
     * @param by the By instance to be wrapped.
     * @return a wrapped By instance.
     */
    public static BySimpleSimilo similo(By by) {
        return new BySimpleSimilo(by);
    }

    /**
     * Creates a new instance of the Similocator utilizing the VonSimilo algorithm to wrap the given By instance.
     *
     * @param by the By instance to be wrapped.
     * @return a wrapped By instance.
     */
    public static ByVonSimilo von(By by) {
        return new ByVonSimilo(by);
    }

    /**
     * Creates a new instance of the Similocator utilizing the CombinedSimilo algorithm to wrap the given By instance.
     *
     * @param by the By instance to be wrapped.
     * @return a wrapped By instance.
     */
    public static ByCombinedSimilo combined(By by) {
        return new ByCombinedSimilo(by);
    }
}
