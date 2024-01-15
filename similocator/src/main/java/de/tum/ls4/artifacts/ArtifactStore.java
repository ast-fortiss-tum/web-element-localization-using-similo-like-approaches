package de.tum.ls4.artifacts;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.openqa.selenium.By;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Abstract class representing a storage for artifacts.
 * This class provides the basic operations for managing artifacts and their locators.
 *
 * @param <A> the type of artifacts this store can handle.
 */
public abstract class ArtifactStore<A extends Artifact> {

    private static final Config config = ConfigFactory.load();
    private static final String DATABASE_HOST = config.getString("artifact_database.host");
    private static final String DATABASE_USER = config.getString("artifact_database.user");
    private static final String DATABASE_PASSWORD = config.getString("artifact_database.password");
    protected Connection connection;

    public ArtifactStore(String databaseName) {
        try {
            this.connection = DriverManager.getConnection(
                    String.format("jdbc:mysql://%s/%s", DATABASE_HOST, databaseName),
                    DATABASE_USER,
                    DATABASE_PASSWORD
            );
        } catch (SQLException e) {
            throw new RuntimeException("Could not connect to database.");
        }
    }

    /**
     * Adds an artifact to the store.
     *
     * @param url          the URL where the artifact is located.
     * @param identifier   the unique identifier of the artifact.
     * @param identifiedBy the identifier by which the artifact was identified.
     * @param artifact     the artifact to be added.
     */
    public abstract void addArtifact(String url, String identifier, String identifiedBy, A artifact,
                                     double initialScore);

    public void addArtifact(String url, String identifier, String identifiedBy, A artifact) {
        addArtifact(url, identifier, identifiedBy, artifact, 0.0);
    }

    /**
     * Removes an artifact from the store.
     *
     * @param url        the URL where the artifact is located.
     * @param identifier the unique identifier of the artifact.
     */
    public abstract void removeArtifact(String url, String identifier);

    /**
     * Adds an evolution of an artifact to the store.
     *
     * @param url        the URL where the artifact is located.
     * @param identifier the unique identifier of the artifact.
     * @param artifact   the new evolution of the artifact.
     */
    public abstract void addArtifactEvolution(String url, String identifier, A artifact);

    /**
     * Updates the optimal locator of an artifact in the store.
     *
     * @param url        the URL where the artifact is located.
     * @param identifier the unique identifier of the artifact.
     */
    public abstract void updateOptimalLocator(String url, String identifier);

    /**
     * Retrieves the optimal locator of an artifact from the store.
     *
     * @param url        the URL where the artifact is located.
     * @param identifier the unique identifier of the artifact.
     * @return the optimal locator of the artifact.
     */
    public abstract By getOptimalLocator(String url, String identifier);

    public abstract void updateAverageScore(String url, String identifier, double score);

    /**
     * Retrieves an artifact from the store.
     *
     * @param url        the URL where the artifact is located.
     * @param identifier the unique identifier of the artifact.
     * @return the artifact.
     */
    public abstract ArtifactData<A> getArtifact(String url, String identifier, boolean considerBranch);

    public ArtifactData<A> getArtifact(String url, String identifier) {
        return getArtifact(url, identifier, true);
    }
}
