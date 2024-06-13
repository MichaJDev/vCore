package nl.vCore.Data.MSSQL;

import nl.vCore.Config.ConfigHandler;
import nl.vCore.Dto.Ban;
import nl.vCore.Dto.User;
import nl.vCore.Main;
import nl.vCore.Utils.MessageUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MSSQLHandler {
    private Main main = Main.getInstance();
    MessageUtils msgUtils = new MessageUtils(main);
    private ConfigHandler cfg;
    private final String JDBC_URL = "jdbc:sqlserver://" + cfg.getConfig().getString("MSSQL.ip") + "\\" + cfg.getConfig().getString("MSSQL.database_name") + ":" + cfg.getConfig().getString("MSSQL.port");
    private final String USERNAME = cfg.getConfig().getString("MSSQL.username");
    private final String PASSWORD = cfg.getConfig().getString("MSSQL.password");
    public MSSQLHandler(Main _main) {
        main = _main;
        cfg = new ConfigHandler(main);
    }

    /**
     * Creates a player table if it does not already exist in the database.
     *
     * @throws SQLException If there is an error executing the SQL statement.
     */
    public void createPlayerTableIfNotExists() throws SQLException {
        Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        String tableName = "players";
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

    /**
     * Creates a bans table if it does not already exist in the database.
     *
     * @throws SQLException If there is an error executing the SQL statement.
     */
    public void createBanTableIfNotExists() throws SQLException {
        Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        String tableName = "bans";
        String createTableSQL = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                "id INT PRIMARY KEY IDENTITY(1,1)," +
                "banner VARCHAR(50), " +
                "banned VARCHAR(50), " +
                "reason text," +
                "date VARCHAR(50)" +
                ");";
        try (Statement statement = connection.createStatement()) {
            statement.execute(createTableSQL);
        }
        msgUtils.log("Table '" + tableName + "' created (if not already exists)...");
    }
    /**
     * Creates a homes table if it does not already exist in the database.
     *
     * @throws SQLException If there is an error executing the SQL statement.
     */
    public void createHomesTableIfNotExists() throws SQLException {
        Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        String tableName = "homes";
        String createTableSQL = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                "id INT PRIMARY KEY IDENTITY(1,1)," +
                "owner VARCHAR(50)," +
                "name VARCHAR(50)," +
                "x INT," +
                "y INT," +
                "z INT," +
                "world VARCHAR(50)" +
                ");";
        try (Statement statement = connection.createStatement()){
            statement.execute(createTableSQL);
            msgUtils.log("Table '" + tableName + "' created (if not already exists)...");
        }
    }
    /**
     * Creates a warns table if it does not already exist in the database.
     *
     * @throws SQLException If there is an error executing the SQL statement.
     */
    public void createWarnsTableIfNotExists() throws SQLException {
        Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        String tableName = "warns";
        String createTableSQL = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                "id INT PRIMARY KEY IDENTITY(1,1)," +
                "warner VARCHAR(50)," +
                "warned VARCHAR(50)," +
                "reason text," +
                "date VARCHAR(50)" +
                ");";
        try (Statement statement = connection.createStatement()){
            statement.execute(createTableSQL);
            msgUtils.log("Table '" + tableName + "' created (if not already exists)...");
        }
    }
    /**
     * Creates a homes table if it does not already exist in the database.
     *
     * @throws SQLException If there is an error executing the SQL statement.
     */
    public void createWarpsTableIfNotExists() throws SQLException {
        Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        String tableName = "warps";
        String createTableSQL = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                "id INT PRIMARY KEY IDENTITY(1,1)," +
                "creator VARCHAR(50)," +
                "name VARCHAR(50)," +
                "x INT," +
                "y INT," +
                "z INT," +
                "world VARCHAR(50)" +
                ");";
        try (Statement statement = connection.createStatement()){
            statement.execute(createTableSQL);
            msgUtils.log("Table '" + tableName + "' created (if not already exists)...");
        }
    }

    /**
     * Inserts a player record into the database.
     *
     * @param player The user object representing the player.
     */
    public void createPlayer(User player) {
        String insertQuery = "INSERT INTO player (id, user_name, display_name, ip, banned, warn_times) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
            insertStatement.setString(1, player.getId().toString());
            insertStatement.setString(2, player.getName());
            insertStatement.setString(3, player.getDisplayName());
            insertStatement.setString(4, player.getIP());
            insertStatement.setBoolean(5, player.isBanned());
            insertStatement.setInt(6, player.getWarnTimes());
            insertStatement.executeUpdate();
            msgUtils.log("Player record inserted successfully!");
        } catch (SQLException e) {
            msgUtils.severe(e.getMessage());
        }
    }

    /**
     * Retrieves a player record from the database based on the player ID.
     *
     * @param playerId The unique identifier of the player.
     * @return The user object representing the player, or null if the player is not found.
     */
    public User readPlayer(String playerId) {
        String selectQuery = "SELECT * FROM player WHERE id = ?";
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

    /**
     * Updates an existing player record in the database.
     *
     * @param player The user object representing the player with updated information.
     */
    public void updatePlayer(User player) {
        String updateQuery = "UPDATE player SET user_name = ?, display_name = ?, ip = ?, banned = ?, warn_times = ? WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
            updateStatement.setString(0, player.getId().toString());
            updateStatement.setString(1, player.getName());
            updateStatement.setString(2, player.getDisplayName());
            updateStatement.setString(3, player.getIP());
            updateStatement.setBoolean(4, player.isBanned());
            updateStatement.setInt(5, player.getWarnTimes());
            updateStatement.executeUpdate();
            msgUtils.log("Player record updated successfully!");
        } catch (SQLException e) {
            msgUtils.severe(e.getMessage());
        }
    }

    public boolean checkIfUserExists(User u) {
        String query = "SELECT COUNT(*) FROM player WHERE uuid = ?";
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

    /**
     * Retrieves a list of all player records from the database.
     *
     * @return A list of user objects representing all players.
     */
    public List<User> getAllPlayers() {
        List<User> players = new ArrayList<>();
        String selectAllQuery = "SELECT * FROM player";
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

    public void deletePlayer(User u) {

    }

    public void createBan(Ban ban) {

    }

    public List<Ban> getAllBans() {
        List<Ban> bList = new ArrayList<Ban>();

        return bList;
    }

    public List<Ban> getAllBansFromUser(User user) {
        List<Ban> ubList = new ArrayList<Ban>();

        return ubList;
    }

    public void updateBan(Ban ban) {

    }

    public void deleteBan(Ban ban) {

    }

}
