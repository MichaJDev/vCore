package nl.vCore.Data.MSSQL.Users;

import nl.vCore.Config.ConfigHandler;
import nl.vCore.Dto.User;
import nl.vCore.Main;
import nl.vCore.Utils.MessageUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MSSQLUsersHandler
{
    private Main main = Main.getInstance();
    MessageUtils msgUtils = new MessageUtils(main);
    private ConfigHandler cfg;
    private final String JDBC_URL = "jdbc:sqlserver://" + cfg.getConfig().getString("MSSQL.ip") + "\\" + cfg.getConfig().getString("MSSQL.database_name") + ":" + cfg.getConfig().getString("MSSQL.port");
    private final String USERNAME = cfg.getConfig().getString("MSSQL.username");
    private final String PASSWORD = cfg.getConfig().getString("MSSQL.password");
    public MSSQLUsersHandler(Main _main){
        main = _main;
    }

    public void createUsersTableIfNotExists() throws SQLException {
        Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        String tableName = "users";
        String createTableSQL = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                "id VARCHAR(50) PRIMARY KEY," +
                "user_name VARCHAR(50)," +
                "display_name VARCHAR(50)," +
                "ip VARCHAR(50)" +
                "banned BIT" +
                "warn_times INT" +
                ")";

        try (Statement statement = connection.createStatement()) {
            statement.execute(createTableSQL);
            msgUtils.log("Table '" + tableName + "' created (if not already exists)...");
        }
    }
    public void create(User player) {
        String insertQuery = "INSERT INTO users (id, user_name, display_name, ip, banned, warn_times) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
            insertStatement.setString(1, player.getId().toString());
            insertStatement.setString(2, player.getName());
            insertStatement.setString(3, player.getDisplayName());
            insertStatement.setString(4, player.getIP());
            insertStatement.setBoolean(5, player.isBanned());
            insertStatement.setInt(6, player.getWarnTimes());
            insertStatement.executeUpdate();
            msgUtils.log("User record inserted successfully!");
        } catch (SQLException e) {
            msgUtils.severe(e.getMessage());
        }
    }
    public User read(String playerId) {
        String selectQuery = "SELECT * FROM users WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement selectStatement = connection.prepareStatement(selectQuery)) {
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
        } catch (SQLException e) {
            msgUtils.severe(e.getMessage());
        }
        msgUtils.warn("Player not found!");
        return null; // Player not found
    }

    public void update(User player) {
        String updateQuery = "UPDATE users SET user_name = ?, display_name = ?, ip = ?, banned = ?, warn_times = ? WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
            updateStatement.setString(0, player.getId().toString());
            updateStatement.setString(1, player.getName());
            updateStatement.setString(2, player.getDisplayName());
            updateStatement.setString(3, player.getIP());
            updateStatement.setBoolean(4, player.isBanned());
            updateStatement.setInt(5, player.getWarnTimes());
            updateStatement.executeUpdate();
            msgUtils.log("User record updated successfully!");
        } catch (SQLException e) {
            msgUtils.severe(e.getMessage());
        }
    }
    public void delete(User u) {

    }
    public boolean checkIfUserExists(User u) {
        String query = "SELECT COUNT(*) FROM users WHERE uuid = ?";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, u.getId().toString());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            msgUtils.severe(e.getMessage());
        }
        return false;
    }
    public List<User> getAll() {
        List<User> players = new ArrayList<>();
        String selectAllQuery = "SELECT * FROM users";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement selectAllStatement = connection.prepareStatement(selectAllQuery);
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
        } catch (SQLException e) {
            msgUtils.severe(e.getMessage());
        }
        return players;
    }
}
