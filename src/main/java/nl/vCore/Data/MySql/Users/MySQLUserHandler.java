package nl.vCore.Data.MySql.Users;

import nl.vCore.Config.ConfigHandler;
import nl.vCore.Data.DatabaseConnectionManager;
import nl.vCore.Dto.User;
import nl.vCore.Main;
import nl.vCore.Utils.MessageUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MySQLUserHandler {
    private final Main main;
    private final DatabaseConnectionManager dbManager;
    private final MessageUtils msgUtils;

    public MySQLUserHandler(Main main) {
        this.main = main;
        this.dbManager = DatabaseConnectionManager.getInstance(main);
        this.msgUtils = new MessageUtils(main);
    }

    public void create(User u) {
        Connection conn = null;
        String sql = "INSERT INTO users (id, name, displayName, IP, banned, warnTimes) VALUES (?, ?, ?, ?, ?, ?)";
        
        try {
            conn = dbManager.getMysqlConnection();
            if (conn == null) {
                msgUtils.severe("Could not establish database connection");
                return;
            }
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, u.getId().toString());
                stmt.setString(2, u.getName());
                stmt.setString(3, u.getDisplayName());
                stmt.setString(4, u.getIP());
                stmt.setBoolean(5, u.isBanned());
                stmt.setInt(6, u.getWarnTimes());
                stmt.executeUpdate();
                msgUtils.log("User created successfully: " + u.getName());
            }
        } catch (SQLException e) {
            msgUtils.severe("Error creating user: " + e.getMessage());
        } finally {
            dbManager.closeConnection(conn);
        }
    }

    public User read(UUID id) {
        Connection conn = null;
        String sql = "SELECT * FROM users WHERE id = ?";
        
        try {
            conn = dbManager.getMysqlConnection();
            if (conn == null) {
                msgUtils.severe("Could not establish database connection");
                return null;
            }
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, id.toString());
                try (ResultSet rs = stmt.executeQuery()) {
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
                }
            }
        } catch (SQLException e) {
            msgUtils.severe("Error reading user: " + e.getMessage());
        } finally {
            dbManager.closeConnection(conn);
        }
        return null;
    }

    public void update(User u) {
        Connection conn = null;
        String sql = "UPDATE users SET name = ?, displayName = ?, IP = ?, banned = ?, warnTimes = ? WHERE id = ?";
        
        try {
            conn = dbManager.getMysqlConnection();
            if (conn == null) {
                msgUtils.severe("Could not establish database connection");
                return;
            }
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, u.getName());
                stmt.setString(2, u.getDisplayName());
                stmt.setString(3, u.getIP());
                stmt.setBoolean(4, u.isBanned());
                stmt.setInt(5, u.getWarnTimes());
                stmt.setString(6, u.getId().toString());
                
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    msgUtils.log("User updated successfully: " + u.getName());
                } else {
                    msgUtils.warn("No user found with ID: " + u.getId());
                }
            }
        } catch (SQLException e) {
            msgUtils.severe("Error updating user: " + e.getMessage());
        } finally {
            dbManager.closeConnection(conn);
        }
    }

    public void delete(UUID id) {
        Connection conn = null;
        String sql = "DELETE FROM users WHERE id = ?";
        
        try {
            conn = dbManager.getMysqlConnection();
            if (conn == null) {
                msgUtils.severe("Could not establish database connection");
                return;
            }
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, id.toString());
                
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    msgUtils.log("User deleted successfully: " + id);
                } else {
                    msgUtils.warn("No user found with ID: " + id);
                }
            }
        } catch (SQLException e) {
            msgUtils.severe("Error deleting user: " + e.getMessage());
        } finally {
            dbManager.closeConnection(conn);
        }
    }

    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        Connection conn = null;
        String sql = "SELECT * FROM users";
        
        try {
            conn = dbManager.getMysqlConnection();
            if (conn == null) {
                msgUtils.severe("Could not establish database connection");
                return users;
            }
            
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                while(rs.next()) {
                    User u = new User();
                    u.setId(UUID.fromString(rs.getString("id")));
                    u.setName(rs.getString("name"));
                    u.setDisplayName(rs.getString("displayName"));
                    u.setIP(rs.getString("IP"));
                    u.setWarnTimes(rs.getInt("warnTimes"));
                    u.setBanned(rs.getBoolean("banned"));
                    users.add(u);
                }
            }
        } catch (SQLException e) {
            msgUtils.severe("Error getting all users: " + e.getMessage());
        } finally {
            dbManager.closeConnection(conn);
        }
        return users;
    }

    public void createUserTableIfNotExist() {
        Connection conn = null;
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                "id VARCHAR(36) PRIMARY KEY, " +
                "name VARCHAR(255), " +
                "displayName VARCHAR(255), " +
                "IP VARCHAR(255), " +
                "banned BOOLEAN, " +
                "warnTimes INT)";
        
        try {
            conn = dbManager.getMysqlConnection();
            if (conn == null) {
                msgUtils.severe("Could not establish database connection");
                return;
            }
            
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(sql);
                msgUtils.log("Users table created or already exists");
            }
        } catch (SQLException e) {
            msgUtils.severe("Error creating users table: " + e.getMessage());
        } finally {
            dbManager.closeConnection(conn);
        }
    }
    
    public boolean checkIfUserExists(User u) {
        Connection conn = null;
        String sql = "SELECT COUNT(*) FROM users WHERE id = ?";
        
        try {
            conn = dbManager.getMysqlConnection();
            if (conn == null) {
                msgUtils.severe("Could not establish database connection");
                return false;
            }
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, u.getId().toString());
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt(1) > 0;
                    }
                }
            }
        } catch (SQLException e) {
            msgUtils.severe("Error checking if user exists: " + e.getMessage());
        } finally {
            dbManager.closeConnection(conn);
        }
        return false;
    }
}
