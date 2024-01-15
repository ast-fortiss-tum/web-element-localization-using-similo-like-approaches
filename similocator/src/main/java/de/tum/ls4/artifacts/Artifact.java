package de.tum.ls4.artifacts;

/**
 * Interface representing an artifact, for Similo either an element or element with overlap.
 * This interface provides methods to get the JSON and XPath representations of an artifact.
 */
public interface Artifact {
    
    /**
     * Returns the JSON representation of the artifact.
     *
     * @return a String containing the JSON representation of the artifact.
     */
    String json();

    /**
     * Returns the absolute XPath to the artifact.
     *
     * @return a String containing the XPath representation of the artifact.
     */
    String xpath();
}
