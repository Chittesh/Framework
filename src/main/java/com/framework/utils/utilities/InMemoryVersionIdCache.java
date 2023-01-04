package com.framework.utils.utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

public class InMemoryVersionIdCache implements VersionIdCache {

	private Logger logger = LogManager.getLogger(this.getClass());

	static {
		try (Connection connection = getConnection()) {
			connection
				.createStatement()
				.executeUpdate(
					"CREATE TABLE version_one_cache (test_id VARCHAR(20) NOT NULL, version_one_id VARCHAR(20) NOT NULL, PRIMARY KEY (test_id));");

		} catch (SQLException e) {
			LoggerFactory.getLogger(InMemoryVersionIdCache.class).error(e, () -> "Could not create cache table.");
		}
	}

	@Override
	public void put(String id, String versionOneId) {
		try (Connection connection = getConnection();
			 PreparedStatement statement = connection.prepareStatement("INSERT INTO version_one_cache (test_id, version_one_id) VALUES (?, ?)")) {

			statement.setString(1, id);
			statement.setString(2, versionOneId);

			statement.executeUpdate();

		} catch(SQLIntegrityConstraintViolationException e) { 
			logger.info("Ignored duplicate insert for id [{}]", versionOneId);
		} catch (SQLException e) {
			logger.error("Failed to insert into cache. ", e);
		}

	}

	@Override
	public Optional<String> getVersionOneId(String id) {
		try (Connection connection = getConnection();
			 PreparedStatement statement = connection.prepareStatement("SELECT version_one_id FROM version_one_cache WHERE test_id = ? ")) {

			statement.setString(1, id);
			ResultSet resultSet = statement.executeQuery();

			return resultSet.next() ? Optional.ofNullable(resultSet.getString(1)) : Optional.empty();

		} catch (SQLException e) {
			logger.error("Failed to retrieve from cache. ", e);
			return Optional.empty();
		}
	}

	private static Connection getConnection() throws SQLException {
		return DriverManager.getConnection("jdbc:hsqldb:mem:mymemdb", "SA", "");
	}

}
