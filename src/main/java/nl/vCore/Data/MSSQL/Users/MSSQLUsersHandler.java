package nl.vCore.Data.MSSQL.Users;

import nl.vCore.Config.ConfigHandler;
import nl.vCore.Data.DatabaseConnectionManager;
import nl.vCore.Dto.User;
import nl.vCore.Main;
import nl.vCore.Utils.MessageUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MSSQLUsersHandler
{
    private final Main main;
    private final MessageUtils msgUtils;
    private final DatabaseConnectionManager dbManager;
    
    public MSSQLUsersHandler(Main _main) {
        main = _main;
        msgUtils = new MessageUtils(main);
        dbManager = DatabaseConnectionManager.getInstance(main);
    }

    public void createUsersTableIfNotExists() throws SQLException {
        Connection connection = null;
        try {
            connection = dbManager.getMssqlConnection();
            if (connection == null) {
                throw new SQLException("Could not establish database connection");
            }
            
            String tableName = "users";
            String createTableSQL = "IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'users') " +
                    "BEGIN " +
                    "CREATE TABLE " + tableName + " (" +
                    "id VARCHAR(50) PRIMARY KEY," +
                    "user_name VARCHAR(50)," +
                    "display_name VARCHAR(50)," +
                    "ip VARCHAR(50)," +
                    "banned BIT," +
                    "warn_times INT" +
                    ") " +
                    "END";

            try (Statement statement = connection.createStatement()) {
                statement.execute(createTableSQL);
                msgUtils.log("Table '" + tableName + "' created (if not already exists)...");
            }
        } catch (SQLException e) {
            msgUtils.severe("Failed to create users table: " + e.getMessage());
            throw e;
        } finally {
            dbManager.closeConnection(connection);
        }
    }
    
    public void create(User player) {
        Connection connection = null;
        String insertQuery = "INSERT INTO users (id, user_name, display_name, ip, banned, warn_times) VALUES (?, ?, ?, ?, ?, ?)";
        
        try {
            connection = dbManager.getMssqlConnection();
            if (connection == null) {
                msgUtils.severe("Could not establish database connection");
                return;
            }
            
            try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                insertStatement.setString(1, player.getId().toString());
                insertStatement.setString(2, player.getName());
                insertStatement.setString(3, player.getDisplayName());
                insertStatement.setString(4, player.getIP());
                insertStatement.setBoolean(5, player.isBanned());
                insertStatement.setInt(6, player.getWarnTimes());
                insertStatement.executeUpdate();
                msgUtils.log("User record inserted successfully!");
            }
        } catch (SQLException e) {
            msgUtils.severe("Failed to create user: " + e.getMessage());
        } finally {
            dbManager.closeConnection(connection);
        }
    }
    
    public User read(String playerId) {
        Connection connection = null;
        String selectQuery = "SELECT * FROM users WHERE id = ?";
        
        try {
            connection = dbManager.getMssqlConnection();
            if (connection == null) {
                msgUtils.severe("Could not establish database connection");
                return null;
            }
            
            try (PreparedStatement selectStatement = connection.prepareStatement(selectQuery)) {
                selectStatement.setString(1, playerId);
                try (ResultSet resultSet = selectStatement.executeQuery()) {
                    if (resultSet.next()) {
                        User user = new User();
                        user.setId(UUID.fromString(resultSet.getString("id")));
                        user.setName(resultSet.getString("user_name"));
                        user.setDisplayName(resultSet.getString("display_name"));
                        user.setIP(resultSet.getString("ip"));
                        user.setBanned(resultSet.getBoolean("banned"));
                        user.setWarnTimes(resultSet.getInt("warn_times"));
                        return user;
                    }
                }
            }
        } catch (SQLException e) {
            msgUtils.severe("Failed to read user: " + e.getMessage());
        } finally {
            dbManager.closeConnection(connection);
        }
        
        msgUtils.warn("Player not found!");
        return null; // Player not found
    }

    public void update(User player) {
        Connection connection = null;
        String updateQuery = "UPDATE users SET user_name = ?, display_name = ?, ip = ?, banned = ?, warn_times = ? WHERE id = ?";
        
        try {
            connection = dbManager.getMssqlConnection();
            if (connection == null) {
                msgUtils.severe("Could not establish database connection");
                return;
            }
            
            try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                updateStatement.setString(1, player.getName());
                updateStatement.setString(2, player.getDisplayName());
                updateStatement.setString(3, player.getIP());
                updateStatement.setBoolean(4, player.isBanned());
                updateStatement.setInt(5, player.getWarnTimes());
                updateStatement.setString(6, player.getId().toString());
                int rowsUpdated = updateStatement.executeUpdate();
                if (rowsUpdated > 0) {
                    msgUtils.log("User record updated successfully!");
                } else {
                    msgUtils.warn("No user found to update with ID: " + player.getId());
                }
            }
        } catch (SQLException e) {
            msgUtils.severe("Failed to update user: " + e.getMessage());
        } finally {
            dbManager.closeConnection(connection);
        }
    }
    
    public void delete(User u) {
        Connection connection = null;
        String deleteQuery = "DELETE FROM users WHERE id = ?";
        
        try {
            connection = dbManager.getMssqlConnection();
            if (connection == null) {
                msgUtils.severe("Could not establish database connection");
                return;
            }
            
            try (PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {
                deleteStatement.setString(1, u.getId().toString());
                int rowsDeleted = deleteStatement.executeUpdate();
                if (rowsDeleted > 0) {
                    msgUtils.log("User deleted successfully!");
                } else {
                    msgUtils.warn("No user found to delete with ID: " + u.getId());
                }
            }
        } catch (SQLException e) {
            msgUtils.severe("Failed to delete user: " + e.getMessage());
        } finally {
            dbManager.closeConnection(connection);
        }
    }
    
    public boolean checkIfUserExists(User u) {
        Connection connection = null;
        String query = "SELECT COUNT(*) FROM users WHERE id = ?";
        
        try {
            connection = dbManager.getMssqlConnection();
            if (connection == null) {
                msgUtils.severe("Could not establish database connection");
                return false;
            }
            
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, u.getId().toString());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt(1) > 0;
                    }
                }
            }
        } catch (SQLException e) {
            msgUtils.severe("Failed to check if user exists: " + e.getMessage());
        } finally {
            dbManager.closeConnection(connection);
        }
        return false;
    }
    
    public List<User> getAll() {
        List<User> players = new ArrayList<>();
        Connection connection = null;
        String selectAllQuery = "SELECT * FROM users";
        
        try {
            connection = dbManager.getMssqlConnection();
            if (connection == null) {
                msgUtils.severe("Could not establish database connection");
                return players;
            }
            
            try (PreparedStatement selectAllStatement = connection.prepareStatement(selectAllQuery);
                 ResultSet resultSet = selectAllStatement.executeQuery()) {
                while (resultSet.next()) {
                    User player = new User();
                    player.setId(UUID.fromString(resultSet.getString("id")));
                    player.setName(resultSet.getString("user_name"));
                    player.setDisplayName(resultSet.getString("display_name"));
                    player.setIP(resultSet.getString("ip"));
                    player.setBanned(resultSet.getBoolean("banned"));
                    player.setWarnTimes(resultSet.getInt("warn_times"));
                    players.add(player);
                }
            }
        } catch (SQLException e) {
            msgUtils.severe("Failed to get all users: " + e.getMessage());
        } finally {
            dbManager.closeConnection(connection);
        }
        return players;
    }
}
