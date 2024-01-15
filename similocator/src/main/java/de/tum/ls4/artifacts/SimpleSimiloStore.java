package de.tum.ls4.artifacts;

import org.openqa.selenium.By;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import static de.tum.ls4.utils.Utils.asLocator;
import static de.tum.ls4.utils.Utils.getGitBranch;

public class SimpleSimiloStore extends SimiloStore<Element> {
    protected static final String INSERT_ELEMENT_META = """
            INSERT INTO similo_elements(
                git_branch,
                url,
                original_identifier,
                original_by_type,
                active_identifier,
                active_by_type,
                average_similo_score,
                localizations
            ) VALUES (?, ?, ?, ?, ?, ?, ?, 1)
            """;
    protected static final String INSERT_ATTRIBUTES = """
            INSERT INTO similo_attributes(
                element_id, evolution_id, name, value
            ) VALUES (?, ?, ?, ?)
            """;
    protected static final String INSERT_CSS = """
            INSERT INTO similo_css(
                element_id, evolution_id, name, value
            ) VALUES (?, ?, ?, ?)
            """;
    protected static final String GET_ELEMENT_ID = """
            SELECT element_id FROM similo_elements
            WHERE git_branch = ?
            AND url = ?
            AND original_identifier = ?
            """;
    protected static final String GET_EVOLUTION_ID = """
            SELECT evolution_id FROM similo_evolution
            WHERE element_id = ?
            ORDER BY evolution_id DESC
            LIMIT 1
            """;
    protected static final String GET_OPTIMAL_LOCATOR = """
            SELECT active_identifier, active_by_type FROM similo_elements
            WHERE git_branch = ?
            AND url = ?
            AND original_identifier = ?
            """;
    private static final String CREATE_EVOLUTION_TABLE = """
            CREATE TABLE IF NOT EXISTS similo_evolution(
                element_id INTEGER,
                evolution_id INTEGER,
                tag VARCHAR(255),
                inner_text TEXT,
                node_text TEXT,
                children_length INTEGER,
                xpath_chrome VARCHAR(255),
                xpath_selenium VARCHAR(255),
                id_xpath VARCHAR(255),
                css_selector TEXT,
                x INTEGER,
                y INTEGER,
                width INTEGER,
                height INTEGER,
                CONSTRAINT UNIQUE (element_id, evolution_id),
                FOREIGN KEY(element_id) REFERENCES similo_elements(element_id)
            );""";
    /**
     * Queries.
     */
    private static final String INSERT_ELEMENT_EVOLUTION = """
            INSERT INTO similo_evolution(
                element_id,
                evolution_id,
                tag,
                inner_text,
                node_text,
                children_length,
                xpath_chrome,
                xpath_selenium,
                id_xpath,
                css_selector,
                x,
                y,
                width,
                height
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
    private static final String SELECT_NEWEST_ELEMENT = """
            SELECT *
            FROM similo_elements elements
            LEFT JOIN similo.similo_evolution evolutions ON elements.element_id = evolutions.element_id
            WHERE
                git_branch = ?
                AND url = ?
                AND original_identifier = ?
            ORDER BY evolution_id DESC
            LIMIT 1
            """;
    private static final String SELECT_LOCATORS = """
            SELECT elements.*, evolutions.xpath_chrome, evolutions.id_xpath, attributes.value AS id
            FROM similo_elements elements
            RIGHT JOIN similo.similo_evolution evolutions ON
                elements.element_id = evolutions.element_id
            LEFT JOIN similo.similo_attributes attributes ON
                evolutions.element_id = attributes.element_id
                AND evolutions.evolution_id = attributes.evolution_id
                AND attributes.name = 'id'
            WHERE
                git_branch = ?
                AND url = ?
                AND original_identifier = ?
            ORDER BY evolutions.evolution_id DESC
            """;
    private static final String UPDATE_OPTIMAL_LOCATOR = """
            UPDATE similo_elements
            SET active_identifier = ?, active_by_type = ?
            WHERE git_branch = ?
            AND url = ?
            AND original_identifier = ?
            """;
    private static final String UPDATE_AVERAGE_SCORE = """
            UPDATE similo_elements
            SET average_similo_score = ?, localizations = localizations + 1
            WHERE git_branch = ?
            AND url = ?
            AND original_identifier = ?
            """;
    private static final String SELECT_ATTRIBUTES = """
            SELECT * FROM similo_attributes
            WHERE element_id = (
                SELECT element_id FROM similo_elements
                WHERE git_branch = ?
                AND url = ?
                AND original_identifier = ?
            ) AND evolution_id = (
                SELECT evolution_id FROM similo_evolution
                WHERE element_id = (
                    SELECT element_id FROM similo_elements
                    WHERE git_branch = ?
                    AND url = ?
                    AND original_identifier = ?
                )
                ORDER BY evolution_id DESC
                LIMIT 1
            )
            """;
    private static final String SELECT_CSS = """
            SELECT * FROM similo_css
            WHERE element_id = (
                SELECT element_id FROM similo_elements
                WHERE git_branch = ?
                AND url = ?
                AND original_identifier = ?
            ) AND evolution_id = (
                SELECT evolution_id FROM similo_evolution
                WHERE element_id = (
                    SELECT element_id FROM similo_elements
                    WHERE git_branch = ?
                    AND url = ?
                    AND original_identifier = ?
                )
                ORDER BY evolution_id DESC
                LIMIT 1
            )
            """;
    private static final String DELETE_ELEMENT = """
            DELETE FROM similo_elements
            WHERE git_branch = ?
            AND url = ?
            AND original_identifier = ?;
            """;
    private static final String DELETE_ELEMENT_EVOLUTION = """
            DELETE FROM similo_evolution
            WHERE element_id = (
                SELECT element_id FROM similo_elements
                WHERE git_branch = ?
                AND url = ?
                AND original_identifier = ?
            );
            """;
    private static final String DELETE_ATTRIBUTES = """
            DELETE FROM similo_attributes
            WHERE element_id = (
                SELECT element_id FROM similo_elements
                WHERE git_branch = ?
                AND url = ?
                AND original_identifier = ?
            );
            """;
    private static final String DELETE_CSS = """
            DELETE FROM similo_css
            WHERE element_id = (
                SELECT element_id FROM similo_elements
                WHERE git_branch = ?
                AND url = ?
                AND original_identifier = ?
            );
            """;

    public SimpleSimiloStore() {
        super("similo", List.of(CREATE_ELEMENTS_TABLE, CREATE_EVOLUTION_TABLE,
                CREATE_ATTRIBUTES_TABLE, CREATE_CSS_TABLE));
    }

    private Map<String, String> queryAttributes(String url, String identifier, String selectAttributes) {
        try {
            PreparedStatement preparedAttributes = connection.prepareStatement(selectAttributes);
            preparedAttributes.setString(1, getGitBranch());
            preparedAttributes.setString(2, url);
            preparedAttributes.setString(3, identifier);
            preparedAttributes.setString(4, getGitBranch());
            preparedAttributes.setString(5, url);
            preparedAttributes.setString(6, identifier);
            ResultSet attributesResultSet = preparedAttributes.executeQuery();

            Map<String, String> attributes = new HashMap<>();
            while (attributesResultSet.next()) {
                attributes.put(
                        attributesResultSet.getString("name"),
                        attributesResultSet.getString("value")
                );
            }
            return attributes;
        } catch (SQLException e) {
            // TODO
            throw new RuntimeException(e);
        }
    }

    private void elementInQuery(PreparedStatement prepared, Element element, int elementId, int evolutionId) {
        try {
            prepared.setInt(1, elementId);
            prepared.setInt(2, evolutionId);
            prepared.setString(3, element.tag);
            prepared.setString(4, element.innerText);
            prepared.setString(5, element.nodeText);
            prepared.setInt(6, element.childrenLength);
            prepared.setString(7, element.xpath.chrome);
            prepared.setString(8, element.xpath.selenium);
            prepared.setString(9, element.xpath.id);
            prepared.setString(10, element.cssSelector);
            prepared.setInt(11, element.x);
            prepared.setInt(12, element.y);
            prepared.setInt(13, element.width);
            prepared.setInt(14, element.height);
        } catch (SQLException e) {
            // TODO
            throw new RuntimeException(e);
        }
    }

    private int getStreak(List<String> locators) {
        if (locators.isEmpty() || Objects.equals(locators.get(0), "") || locators.get(0) == null) {
            return -1;
        }

        int streak = 0;
        String active = locators.get(0);
        for (String locator : locators) {
            if (active.equals(locator)) {
                streak++;
            } else {
                break;
            }
        }
        return streak;
    }

    @Override
    public void addArtifact(String url, String identifier, String identifiedBy, Element artifact,
                            double initialScore) {
        try {
            // Add the element meta data
            PreparedStatement preparedMeta = connection.prepareStatement(INSERT_ELEMENT_META,
                    Statement.RETURN_GENERATED_KEYS);
            preparedMeta.setString(1, getGitBranch());
            preparedMeta.setString(2, url);
            preparedMeta.setString(3, identifier);
            preparedMeta.setString(4, identifiedBy);
            preparedMeta.setString(5, identifier);
            preparedMeta.setString(6, identifiedBy);
            preparedMeta.setDouble(7, initialScore);
            preparedMeta.execute();

            // Get the element id
            ResultSet generatedKeys = preparedMeta.getGeneratedKeys();
            generatedKeys.next();
            int elementId = generatedKeys.getInt(1);

            // Add the element itself as the first evolution
            PreparedStatement preparedElement = connection.prepareStatement(INSERT_ELEMENT_EVOLUTION,
                    Statement.RETURN_GENERATED_KEYS);
            elementInQuery(preparedElement, artifact, elementId, 0);
            preparedElement.execute();

            // Insert the attributes
            PreparedStatement preparedAttributes = connection.prepareStatement(INSERT_ATTRIBUTES);
            for (var entry : artifact.attributes.entrySet()) {
                preparedAttributes.setInt(1, elementId);
                preparedAttributes.setInt(2, 0);
                preparedAttributes.setString(3, entry.getKey());
                preparedAttributes.setString(4, entry.getValue());
                preparedAttributes.addBatch();
            }
            preparedAttributes.executeBatch();

            // Insert the css styles
            PreparedStatement preparedCss = connection.prepareStatement(INSERT_CSS);
            for (var entry : artifact.cssStyles.entrySet()) {
                preparedCss.setInt(1, elementId);
                preparedCss.setInt(2, 0);
                preparedCss.setString(3, entry.getKey());
                preparedCss.setString(4, entry.getValue());
                preparedCss.addBatch();
            }
            preparedCss.executeBatch();
        } catch (SQLException e) {
            // TODO
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeArtifact(String url, String identifier) {
        try {
            // Delete the elements attributes
            PreparedStatement preparedAttributes = connection.prepareStatement(DELETE_ATTRIBUTES);
            preparedAttributes.setString(1, getGitBranch());
            preparedAttributes.setString(2, url);
            preparedAttributes.setString(3, identifier);
            preparedAttributes.execute();

            // Delete the elements css styles
            PreparedStatement preparedCss = connection.prepareStatement(DELETE_CSS);
            preparedCss.setString(1, getGitBranch());
            preparedCss.setString(2, url);
            preparedCss.setString(3, identifier);
            preparedCss.execute();

            // Delete evolutions of the element
            PreparedStatement preparedEvolution = connection.prepareStatement(DELETE_ELEMENT_EVOLUTION);
            preparedEvolution.setString(1, getGitBranch());
            preparedEvolution.setString(2, url);
            preparedEvolution.setString(3, identifier);
            preparedEvolution.execute();

            // Delete the element itself
            PreparedStatement preparedElement = connection.prepareStatement(DELETE_ELEMENT);
            preparedElement.setString(1, getGitBranch());
            preparedElement.setString(2, url);
            preparedElement.setString(3, identifier);
            preparedElement.execute();
        } catch (SQLException e) {
            // TODO
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addArtifactEvolution(String url, String identifier, Element element) {
        try {
            // Get the element id
            PreparedStatement preparedElementId = connection.prepareStatement(GET_ELEMENT_ID);
            preparedElementId.setString(1, getGitBranch());
            preparedElementId.setString(2, url);
            preparedElementId.setString(3, identifier);
            ResultSet elementIdResultSet = preparedElementId.executeQuery();
            elementIdResultSet.next();
            int elementId = elementIdResultSet.getInt(1);

            // Get the evolution id
            PreparedStatement preparedEvolutionId = connection.prepareStatement(GET_EVOLUTION_ID);
            preparedEvolutionId.setInt(1, elementId);
            ResultSet evolutionIdResultSet = preparedEvolutionId.executeQuery();
            evolutionIdResultSet.next();
            int evolutionId = evolutionIdResultSet.getInt(1);

            // Add the element itself as the first evolution
            PreparedStatement preparedElement = connection.prepareStatement(INSERT_ELEMENT_EVOLUTION,
                    Statement.RETURN_GENERATED_KEYS);
            elementInQuery(preparedElement, element, elementId, evolutionId + 1);
            preparedElement.execute();

            // Insert the attributes
            PreparedStatement preparedAttributes = connection.prepareStatement(INSERT_ATTRIBUTES);
            for (var entry : element.attributes.entrySet()) {
                preparedAttributes.setInt(1, elementId);
                preparedAttributes.setInt(2, evolutionId + 1);
                preparedAttributes.setString(3, entry.getKey());
                preparedAttributes.setString(4, entry.getValue());
                preparedAttributes.addBatch();
            }
            preparedAttributes.executeBatch();

            // Insert the css styles
            PreparedStatement preparedCss = connection.prepareStatement(INSERT_CSS);
            for (var entry : element.cssStyles.entrySet()) {
                preparedCss.setInt(1, elementId);
                preparedCss.setInt(2, evolutionId + 1);
                preparedCss.setString(3, entry.getKey());
                preparedCss.setString(4, entry.getValue());
                preparedCss.addBatch();
            }
            preparedCss.executeBatch();
        } catch (SQLException e) {
            // TODO
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateOptimalLocator(String url, String identifier) {
        try {
            PreparedStatement preparedLocators = connection.prepareStatement(SELECT_LOCATORS);
            preparedLocators.setString(1, getGitBranch());
            preparedLocators.setString(2, url);
            preparedLocators.setString(3, identifier);
            ResultSet locatorsResultSet = preparedLocators.executeQuery();
            if (!locatorsResultSet.next()) {
                return;
            }

            String activeByType = locatorsResultSet.getString("active_by_type");
            String activeIdentifier = locatorsResultSet.getString("active_identifier");
            List<String> xPaths = new ArrayList<>();
            List<String> idXPaths = new ArrayList<>();
            List<String> ids = new ArrayList<>();

            do {
                xPaths.add(locatorsResultSet.getString("xpath_chrome"));
                idXPaths.add(locatorsResultSet.getString("id_xpath"));
                ids.add(locatorsResultSet.getString("id"));
            } while (locatorsResultSet.next());

            int xPathStreak = getStreak(xPaths);
            int idXPathStreak = getStreak(idXPaths);
            int idStreak = getStreak(ids);

            String optimalLocator;
            String optimalByType;

            if (idStreak >= idXPathStreak && idStreak >= xPathStreak && idStreak > 0) {
                optimalLocator = ids.get(0);
                optimalByType = "id";
            } else if (idXPathStreak >= xPathStreak && idXPathStreak > 0) {
                optimalLocator = idXPaths.get(0);
                optimalByType = "xpath";
            } else if (xPathStreak > 0) {
                optimalLocator = xPaths.get(0);
                optimalByType = "xpath";
            } else {
                optimalLocator = activeIdentifier;
                optimalByType = activeByType;
            }

            PreparedStatement preparedUpdate = connection.prepareStatement(UPDATE_OPTIMAL_LOCATOR);
            preparedUpdate.setString(1, optimalLocator);
            preparedUpdate.setString(2, optimalByType);
            preparedUpdate.setString(3, getGitBranch());
            preparedUpdate.setString(4, url);
            preparedUpdate.setString(5, identifier);
            preparedUpdate.execute();
        } catch (SQLException e) {
            // TODO
            throw new RuntimeException(e);
        }
    }

    @Override
    public By getOptimalLocator(String url, String identifier) {
        try {
            PreparedStatement preparedOptimalLocator = connection.prepareStatement(GET_OPTIMAL_LOCATOR);
            preparedOptimalLocator.setString(1, getGitBranch());
            preparedOptimalLocator.setString(2, url);
            preparedOptimalLocator.setString(3, identifier);
            ResultSet resultSet = preparedOptimalLocator.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            return asLocator(
                    resultSet.getString("active_by_type"),
                    resultSet.getString("active_identifier")
            );
        } catch (SQLException e) {
            // TODO
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateAverageScore(String url, String identifier, double score) {
        try {
            PreparedStatement preparedUpdate = connection.prepareStatement(UPDATE_AVERAGE_SCORE);
            preparedUpdate.setDouble(1, score);
            preparedUpdate.setString(2, getGitBranch());
            preparedUpdate.setString(3, url);
            preparedUpdate.setString(4, identifier);
            preparedUpdate.execute();
        } catch (SQLException e) {
            // TODO
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArtifactData<Element> getArtifact(String url, String identifier, boolean considerBranch) {
        try {
            // TODO consider branch

            // Get the attributes and css styles
            Map<String, String> cssStyles = queryAttributes(url, identifier, SELECT_CSS);
            Map<String, String> attributes = queryAttributes(url, identifier, SELECT_ATTRIBUTES);

            PreparedStatement preparedSelect = connection.prepareStatement(SELECT_NEWEST_ELEMENT);
            preparedSelect.setString(1, getGitBranch());
            preparedSelect.setString(2, url);
            preparedSelect.setString(3, identifier);
            ResultSet resultSet = preparedSelect.executeQuery();
            if (!resultSet.next()) {
                return null;
            }

            Element element = new Element(
                    resultSet.getString("tag"),
                    resultSet.getString("node_text"),
                    resultSet.getString("inner_text"),
                    resultSet.getInt("children_length"),
                    new Element.Xpath(
                            resultSet.getString("xpath_chrome"),
                            resultSet.getString("xpath_selenium"),
                            resultSet.getString("id_xpath")
                    ),
                    resultSet.getString("css_selector"),
                    resultSet.getInt("x"),
                    resultSet.getInt("y"),
                    resultSet.getInt("width"),
                    resultSet.getInt("height"),
                    attributes,
                    cssStyles
            );

            return new ArtifactData<>(
                    element,
                    getOptimalLocator(url, identifier),
                    resultSet.getInt("element_id"),
                    resultSet.getDouble("average_similo_score"),
                    resultSet.getInt("localizations")
            );
        } catch (SQLException e) {
            throw new RuntimeException("Error while retrieving artifact", e);
        }
    }
}
