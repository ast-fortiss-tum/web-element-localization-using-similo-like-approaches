package de.tum.ls4.artifacts;

import org.openqa.selenium.By;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class SimiloStore<A extends Artifact> extends ArtifactStore<A> {
    protected static final String CREATE_ELEMENTS_TABLE = """
            CREATE TABLE IF NOT EXISTS similo_elements(
                element_id INTEGER PRIMARY KEY AUTO_INCREMENT,
                git_branch VARCHAR(255) DEFAULT null,
                url VARCHAR(255) NOT NULL,
                original_identifier VARCHAR(255) NOT NULL,
                original_by_type VARCHAR(20),
                active_identifier VARCHAR(255) NOT NULL,
                active_by_type VARCHAR(20),
                localizations INTEGER DEFAULT 0,
                average_similo_score DOUBLE DEFAULT 0,
                last_update TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                CONSTRAINT UNIQUE (git_branch, url, original_identifier)
            );""";
    protected static final String CREATE_ATTRIBUTES_TABLE = """
            CREATE TABLE IF NOT EXISTS similo_attributes(
                id INTEGER PRIMARY KEY AUTO_INCREMENT,
                element_id INTEGER,
                evolution_id INTEGER,
                name VARCHAR(255),
                value TEXT,
                FOREIGN KEY(element_id, evolution_id) REFERENCES similo_evolution(element_id, evolution_id)
            );""";
    protected static final String CREATE_CSS_TABLE = """
            CREATE TABLE IF NOT EXISTS similo_css(
                id INTEGER PRIMARY KEY AUTO_INCREMENT,
                element_id INTEGER,
                evolution_id INTEGER,
                name VARCHAR(255),
                value TEXT,
                FOREIGN KEY(element_id, evolution_id) REFERENCES similo_evolution(element_id, evolution_id)
            );""";

    public SimiloStore(String databaseName, List<String> tableCreateStatements) {
        super(databaseName);
        setupDatabase(tableCreateStatements);
    }

    private void setupDatabase(List<String> tableCreateStatements) {
        try {
            Statement statement = connection.createStatement();

            for (String tableCreateStatement : tableCreateStatements) {
                statement.execute(tableCreateStatement);
            }
        } catch (SQLException e) {
            // TODO
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addArtifact(String url, String identifier, String identifiedBy, A artifact, double initialScore) {

    }

    @Override
    public void removeArtifact(String url, String identifier) {

    }

    @Override
    public void addArtifactEvolution(String url, String identifier, A artifact) {

    }

    @Override
    public void updateOptimalLocator(String url, String identifier) {

    }

    @Override
    public By getOptimalLocator(String url, String identifier) {
        return null;
    }

    @Override
    public void updateAverageScore(String url, String identifier, double score) {

    }

    @Override
    public ArtifactData<A> getArtifact(String url, String identifier, boolean considerBranch) {
        return null;
    }
}
