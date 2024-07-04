package nl.vCore.Data.MySql.Users;

import nl.vCore.Config.ConfigHandler;
import nl.vCore.Dto.User;
import nl.vCore.Main;
import nl.vCore.Utils.MessageUtils;

import java.sql.*;
import java.util.UUID;

public class MySQLUserHandler {
    private final ConfigHandler configHandler;
    private final String url;
    private final String user;
    private final String password;

    private final MessageUtils msgUtils;

    public MySQLUserHandler(Main main) {
        this.configHandler = new ConfigHandler(main);
        this.url = buildJdbcUrl();
        this.user = configHandler.getConfig().getString("MySQL.username");
        this.password = configHandler.getConfig().getString("MySQL.password");
        this.msgUtils = new MessageUtils(main);
    }

    private String buildJdbcUrl() {
        String ip = configHandler.getConfig().getString("MySQL.ip");
        int port = configHandler.getConfig().getInt("MySQL.port");
        String dbName = configHandler.getConfig().getString("MySQL.database_name");
        return "jdbc:mysql://" + ip + ":" + port + "/" + dbName;
    }

    public void create(User u) {
        String sql = "INSERT INTO users (id, name, displayName, IP, banned, warnTimes) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, u.getId().toString());
            stmt.setString(2, u.getName());
            stmt.setString(3, u.getDisplayName());
            stmt.setString(4, u.getIP());
            stmt.setBoolean(5, u.isBanned());
            stmt.setInt(6, u.getWarnTimes());
            stmt.executeUpdate();
        } catch (SQLException e) {
            msgUtils.severe(e.getMessage());
        }
    }

    public User read(UUID id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id.toString());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                User u = new User();
                u.setId(UUID.fromString(rs.getString("id")));
                u.setName(rs.getString("name"));
                u.setDisplayName(rs.getString("displayName"));
                u.setIP(rs.getString("IP"));
                u.setBanned(rs.getBoolean("banned"));
                u.setWarnTimes(rs.getInt("warnTimes"));
                return u;
            }
        } catch (SQLException e) {
            msgUtils.severe(e.getMessage());
        }
        return null;
    }

    public void update(User u) {
        String sql = "UPDATE users SET name = ?, displayName = ?, IP = ?, banned = ?, warnTimes = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, u.getName());
            stmt.setString(2, u.getDisplayName());
            stmt.setString(3, u.getIP());
            stmt.setBoolean(4, u.isBanned());
            stmt.setInt(5, u.getWarnTimes());
            stmt.setString(6, u.getId().toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            msgUtils.severe(e.getMessage());
        }
    }

    public void delete(UUID id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id.toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            msgUtils.severe(e.getMessage());
        }
    }

    public void createUserTableIfNotExist() {
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                "id VARCHAR(36) PRIMARY KEY, " +
                "name VARCHAR(255), " +
                "displayName VARCHAR(255), " +
                "IP VARCHAR(255), " +
                "banned BOOLEAN, " +
                "warnTimes INT)";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            msgUtils.severe(e.getMessage());
        }
    }

}
