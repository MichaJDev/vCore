package nl.vCore.Data.MySql.Warns;

import nl.vCore.Config.ConfigHandler;
import nl.vCore.Dto.User;
import nl.vCore.Dto.Warn;
import nl.vCore.Main;
import nl.vCore.Utils.DtoShaper;
import nl.vCore.Utils.MessageUtils;

import javax.xml.transform.Result;
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
            stmt.setString(0, w.getId().toString());
            stmt.setString(1, w.getWarner().getId().toString());
            stmt.setString(2, w.getWarned().getId().toString());
            stmt.setString(3, w.getReason());
            stmt.setString(4, w.getDate());
        }catch (SQLException e){
            msgUtils.severe(e.getMessage());
        }
    }

    public Warn read(UUID id){
        String sql = "SELECT * FROM warns WHERE id = ?;";
        try(Connection conn = DriverManager.getConnection(sql);
            PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(0,id.toString());
            try(ResultSet rs = stmt.executeQuery()){
                Warn w = new Warn();
                w.setId(id);
                w.setWarned(DtoShaper.userShaper(Objects.requireNonNull(Main.getInstance().getServer().getPlayer(UUID.fromString(rs.getString("warned"))))));
                w.setWarner(DtoShaper.userShaper(Objects.requireNonNull(Main.getInstance().getServer().getPlayer(UUID.fromString(rs.getString("warner"))))));
                w.setReason(rs.getString("reason"));
                w.setDate(rs.getString("date"));
                return w;
            }
        }catch(SQLException e){
            msgUtils.severe(e.getMessage());
        }
        return null;
    }

    public void update(Warn w){
        String sql = "UPDATE SET reason = ? WHERE id = ?;";
        try(Connection conn = DriverManager.getConnection(url, user, password);
            PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(0, w.getReason());
            stmt.setString(1, w.getId().toString());
            stmt.executeUpdate();
        }catch (SQLException e){
            msgUtils.severe(e.getMessage());
        }
    }

    public void delete(Warn w){
        String sql = "DELETE FROM warns WHERE id = ?;";
        try(Connection conn = DriverManager.getConnection(url, user, password);
            PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(0, w.getId().toString());
        }catch (SQLException e){
            msgUtils.severe(e.getMessage());
        }
    }

    public List<Warn> getAll(){
        List<Warn> warns = new ArrayList<>();
        String sql = "SELECT * FROM warns;";
        try(Connection conn = DriverManager.getConnection(url, user, password);
            PreparedStatement stmt = conn.prepareStatement(sql)){
            try(ResultSet rs = stmt.executeQuery()){
                while(rs.next()){
                    Warn w = new Warn();
                    w.setId(UUID.fromString("id"));
                    w.setWarned(DtoShaper.userShaper(Objects.requireNonNull(Main.getInstance().getServer().getPlayer(UUID.fromString(rs.getString("warned"))))));
                    w.setWarner(DtoShaper.userShaper(Objects.requireNonNull(Main.getInstance().getServer().getPlayer(UUID.fromString(rs.getString("warner"))))));
                    w.setReason(rs.getString("reason"));
                    w.setDate(rs.getString("date"));
                    warns.add(w);
                }
            }
        }catch(SQLException e){
            msgUtils.severe(e.getMessage());
        }
        return warns;
    }

    public List<Warn> getWarnsFromUser(User u){
        List<Warn> warns = new ArrayList<>();
        String sql = "SELECT * FROM warns WHERE warned = ?;";
        try(Connection conn = DriverManager.getConnection(url, user, password);
         PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(0, u.getId().toString());
            try(ResultSet rs = stmt.executeQuery()){
                while(rs.next()){
                    Warn w = new Warn();
                    w.setId(UUID.fromString("id"));
                    w.setWarned(DtoShaper.userShaper(Objects.requireNonNull(Main.getInstance().getServer().getPlayer(UUID.fromString(rs.getString("warned"))))));
                    w.setWarner(DtoShaper.userShaper(Objects.requireNonNull(Main.getInstance().getServer().getPlayer(UUID.fromString(rs.getString("warner"))))));
                    w.setReason(rs.getString("reason"));
                    w.setDate(rs.getString("date"));
                    warns.add(w);
                }
            }
        }catch(SQLException e){
            msgUtils.severe(e.getMessage());
        }
        return warns;
    }

    public void createWarnsTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS warns (" +
                "id VARCHAR(255) PRIMARY KEY, " +
                "warner VARCHAR(255), " +
                "warmed VARCHAR(255), " +
                "reason VARCHAR(255), )" +
                "date VARCHAR(255);";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            msgUtils.severe(e.getMessage());
        }
    }
}
