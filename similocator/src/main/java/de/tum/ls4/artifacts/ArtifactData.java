package de.tum.ls4.artifacts;

import org.openqa.selenium.By;

/**
 * Record representing a concrete artifact used in the {@link de.tum.ls4.artifacts.ArtifactData} classes, e.g.
 * {@link SimpleSimiloStore}. Additionally, it contains the average score between the artifact and its
 * localizations, which can be used to determine if a located artifact is a match or not.
 *
 * @param <A>          the type of the artifact.
 * @param artifact     the actual artifact.
 * @param optimalBy    the optimal {@link By} to locate the artifact.
 * @param elementId    the id of the element in the database.
 * @param averageScore the average score of the comparisons for this artifact.
 * @param numberOfLocalizations how often this artifact was localized.
 */
public record ArtifactData<A extends Artifact>(A artifact, By optimalBy,
                                               int elementId, double averageScore, int numberOfLocalizations) {
}
