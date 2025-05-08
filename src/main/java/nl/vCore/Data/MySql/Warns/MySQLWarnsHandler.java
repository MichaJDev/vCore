package nl.vCore.Data.MySql.Warns;

import nl.vCore.Config.ConfigHandler;
import nl.vCore.Dto.User;
import nl.vCore.Dto.Warn;
import nl.vCore.Main;
import nl.vCore.Utils.DtoShaper;
import nl.vCore.Utils.MessageUtils;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;
import java.util.Objects;
import java.util.UUID;

public class MySQLWarnsHandler {

    private final ConfigHandler configHandler;
    private final String url;
    private final String user;
    private final String password;
    private final MessageUtils msgUtils;

    public MySQLWarnsHandler(Main main) {
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

    public void create(Warn w){
        String sql = "INSERT INTO warns (id, warner, warned, reason, date) VALUES (?, ?, ?, ?, ?);";
        try(Connection conn = DriverManager.getConnection(url, user, password);
            PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, w.getId().toString());
            stmt.setString(2, w.getWarner().getId().toString());
            stmt.setString(3, w.getWarned().getId().toString());
            stmt.setString(4, w.getReason());
            stmt.setString(5, w.getDate());
            stmt.executeUpdate();
            msgUtils.log("Warning created successfully!");
        }catch (SQLException e){
            msgUtils.severe("Failed to create warning: " + e.getMessage());
        }
    }

    public Warn read(UUID id){
        String sql = "SELECT * FROM warns WHERE id = ?;";
        try(Connection conn = DriverManager.getConnection(url, user, password);
            PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, id.toString());
            try(ResultSet rs = stmt.executeQuery()){
                if (rs.next()) {
                    Warn w = new Warn();
                    w.setId(id);
                    
                    // Use the safer method for getting users from UUID strings
                    String warnedId = rs.getString("warned");
                    String warnerId = rs.getString("warner");
                    
                    w.setWarned(DtoShaper.userShaperFromUUID(warnedId));
                    w.setWarner(DtoShaper.userShaperFromUUID(warnerId));
                    w.setReason(rs.getString("reason"));
                    w.setDate(rs.getString("date"));
                    return w;
                }
            }
        }catch(SQLException e){
            msgUtils.severe("Failed to read warning: " + e.getMessage());
        }
        return null;
    }

    public void update(Warn w){
        String sql = "UPDATE warns SET reason = ? WHERE id = ?;";
        try(Connection conn = DriverManager.getConnection(url, user, password);
            PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, w.getReason());
            stmt.setString(2, w.getId().toString());
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                msgUtils.log("Warning updated successfully!");
            } else {
                msgUtils.warn("No warning found with ID: " + w.getId());
            }
        }catch (SQLException e){
            msgUtils.severe("Failed to update warning: " + e.getMessage());
        }
    }

    public void delete(Warn w){
        String sql = "DELETE FROM warns WHERE id = ?;";
        try(Connection conn = DriverManager.getConnection(url, user, password);
            PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, w.getId().toString());
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                msgUtils.log("Warning deleted successfully!");
            } else {
                msgUtils.warn("No warning found with ID: " + w.getId());
            }
        }catch (SQLException e){
            msgUtils.severe("Failed to delete warning: " + e.getMessage());
        }
    }

    public List<Warn> getAll(){
        List<Warn> warns = new ArrayList<>();
        String sql = "SELECT * FROM warns;";
        try(Connection conn = DriverManager.getConnection(url, user, password);
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()){
            while(rs.next()){
                try {
                    Warn w = new Warn();
                    w.setId(UUID.fromString(rs.getString("id")));
                    
                    // Use the safer method for getting users from UUID strings
                    String warnedId = rs.getString("warned");
                    String warnerId = rs.getString("warner");
                    
                    w.setWarned(DtoShaper.userShaperFromUUID(warnedId));
                    w.setWarner(DtoShaper.userShaperFromUUID(warnerId));
                    w.setReason(rs.getString("reason"));
                    w.setDate(rs.getString("date"));
                    warns.add(w);
                } catch (IllegalArgumentException e) {
                    msgUtils.severe("Invalid UUID in warns table: " + e.getMessage());
                }
            }
        }catch(SQLException e){
            msgUtils.severe("Failed to fetch all warnings: " + e.getMessage());
        }
        return warns;
    }

    public List<Warn> getWarnsFromUser(User u){
        List<Warn> warns = new ArrayList<>();
        String sql = "SELECT * FROM warns WHERE warned = ?;";
        try(Connection conn = DriverManager.getConnection(url, user, password);
         PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, u.getId().toString());
            try(ResultSet rs = stmt.executeQuery()){
                while(rs.next()){
                    try {
                        Warn w = new Warn();
                        w.setId(UUID.fromString(rs.getString("id")));
                        
                        // Use the safer method for getting users from UUID strings
                        String warnedId = rs.getString("warned");
                        String warnerId = rs.getString("warner");
                        
                        w.setWarned(DtoShaper.userShaperFromUUID(warnedId));
                        w.setWarner(DtoShaper.userShaperFromUUID(warnerId));
                        w.setReason(rs.getString("reason"));
                        w.setDate(rs.getString("date"));
                        warns.add(w);
                    } catch (IllegalArgumentException e) {
                        msgUtils.severe("Invalid UUID in warns table: " + e.getMessage());
                    }
                }
            }
        }catch(SQLException e){
            msgUtils.severe("Failed to fetch warnings for user: " + e.getMessage());
        }
        return warns;
    }

    public void createWarnsTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS warns (" +
                "id VARCHAR(255) PRIMARY KEY, " +
                "warner VARCHAR(255), " +
                "warned VARCHAR(255), " +
                "reason VARCHAR(255), " +
                "date VARCHAR(255));";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            msgUtils.log("Warns table created successfully!");
        } catch (SQLException e) {
            msgUtils.severe("Failed to create warns table: " + e.getMessage());
        }
    }

    /**
     * Gets all warns for a specific player
     *
     * @param playerUuid The UUID of the player
     * @return List of warns for the player
     */
    public List<Warn> getWarnsForPlayer(UUID playerUuid) {
        List<Warn> warns = new ArrayList<>();
        String sql = "SELECT * FROM warns WHERE warned = ?;";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, playerUuid.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    try {
                        Warn w = new Warn();
                        w.setId(UUID.fromString(rs.getString("id")));
                        
                        // Use the safer method for getting users from UUID strings
                        String warnedId = rs.getString("warned");
                        String warnerId = rs.getString("warner");
                        
                        w.setWarned(DtoShaper.userShaperFromUUID(warnedId));
                        w.setWarner(DtoShaper.userShaperFromUUID(warnerId));
                        w.setReason(rs.getString("reason"));
                        w.setDate(rs.getString("date"));
                        warns.add(w);
                    } catch (IllegalArgumentException e) {
                        msgUtils.severe("Invalid UUID in warns table: " + e.getMessage());
                    }
                }
            }
        } catch (SQLException e) {
            msgUtils.severe("Failed to fetch warnings for player: " + e.getMessage());
        }
        return warns;
    }
    
    /**
     * Gets all warns from the database
     *
     * @return List of all warns
     */
    public List<Warn> getAllWarns() {
        return getAll(); // Reuse existing method
    }
    
    /**
     * Checks if a warn exists in the database
     *
     * @param warn The warn to check
     * @return true if the warn exists, false otherwise
     */
    public boolean doesWarnExist(Warn warn) {
        String sql = "SELECT COUNT(*) FROM warns WHERE id = ?;";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, warn.getId().toString());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            msgUtils.severe("Failed to check if warn exists: " + e.getMessage());
        }
        return false;
    }
}
