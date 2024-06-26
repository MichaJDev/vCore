package nl.vCore.Data.MSSQL.Warps;

import nl.vCore.Config.ConfigHandler;
import nl.vCore.Dto.Warp;
import nl.vCore.Main;
import nl.vCore.Utils.DtoShaper;
import nl.vCore.Utils.MessageUtils;
import org.bukkit.Location;

import java.sql.*;
import java.util.Objects;
import java.util.UUID;

public class MSSQLWarpsHandler {

    private final Main main;
    MessageUtils msgUtils;
    private ConfigHandler cfg;
    private final String JDBC_URL = "jdbc:sqlserver://" + cfg.getConfig().getString("MSSQL.ip") + "\\" + cfg.getConfig().getString("MSSQL.database_name") + ":" + cfg.getConfig().getString("MSSQL.port");
    private final String USERNAME = cfg.getConfig().getString("MSSQL.username");
    private final String PASSWORD = cfg.getConfig().getString("MSSQL.password");


    public MSSQLWarpsHandler(Main _main) {
        main = _main;
        msgUtils = new MessageUtils(main);
        cfg = new ConfigHandler(main);
    }

    public void createWarpsTableIfNotExists() throws SQLException {
        Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        String tableName = "warps";
        String createTableSQL = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                "uuid varchar(255) PRIMARY KEY," +
                "creator VARCHAR(50)," +
                "name VARCHAR(50)," +
                "x INT," +
                "y INT," +
                "z INT," +
                "world VARCHAR(50)" +
                ");";
        try (Statement statement = connection.createStatement()) {
            statement.execute(createTableSQL);
            msgUtils.log("Table '" + tableName + "' created (if not already exists)...");
        }
    }

    public void create(Warp w) {
        String query = "INSERT INTO warps (uuid, creator, name, x, y, z, world) VALUES (? , ? , ? , ? , ? , ? , ? , ? )";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query);
        ) {
            stmt.setString(0, w.getId().toString());
            stmt.setString(1, w.getCreator().getId().toString());
            stmt.setString(2, w.getName());
            stmt.setInt(3, w.getLocation().getBlockX());
            stmt.setInt(4, w.getLocation().getBlockY());
            stmt.setInt(5, w.getLocation().getBlockZ());
            stmt.setString(6, w.getLocation().getWorld().getName());
            stmt.executeUpdate();
        } catch (SQLException e) {
            msgUtils.severe(e.getMessage());
        }
    }

    public Warp read(String name) {
        String query = "SELECT * FROM warps WHERE name = ?";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)
        ) {
            stmt.setString(0, name);
            try (ResultSet rs = stmt.executeQuery()) {
                Warp w = new Warp();
                Location loc = new Location(main.getServer().getWorld(rs.getString("world")), rs.getInt("x"), rs.getInt("y"), rs.getInt("z"));
                w.setId(rs.getString("id"));
                w.setCreator(DtoShaper.userShaper(Objects.requireNonNull(main.getServer().getPlayer(UUID.fromString(rs.getString("creator"))))));
                w.setLocation(loc);
                w.setName(rs.getString("name"));
                return w;
            }
        } catch (SQLException e) {
            msgUtils.severe(e.getMessage());
        }
        return null;
    }

    public void update(Warp w) {
        String query = "UPDATE SET x = ?, y = ?, z = ?, world = ? WHERE uuid = ?";
        try(Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
            PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setInt(0, w.getLocation().getBlockX());
            stmt.setInt(1, w.getLocation().getBlockY());
            stmt.setInt(2, w.getLocation().getBlockZ());
            stmt.setString(3, w.getLocation().getWorld().getName());
            stmt.setString(4, w.getId().toString());
            stmt.executeUpdate();
        }catch(SQLException e){
            msgUtils.severe(e.getMessage());
        }
    }

    public void delete(Warp w) {
        String query = "DELETE FROM warps WHERE uuid = ?";
        try(Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
            PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setString(0, w.getId().toString());
            stmt.executeUpdate();
        }catch (SQLException e){
            msgUtils.severe(e.getMessage());
        }
    }

}
