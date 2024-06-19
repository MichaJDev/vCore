package nl.vCore.Data.MSSQL.Warns;

import nl.vCore.Config.ConfigHandler;
import nl.vCore.Dto.User;
import nl.vCore.Dto.Warn;
import nl.vCore.Main;
import nl.vCore.Utils.DtoShaper;
import nl.vCore.Utils.MessageUtils;

import java.sql.*;
import java.util.List;
import java.util.UUID;

public class MSSQLWarnsHandler {
    private Main main = Main.getInstance();
    MessageUtils msgUtils = new MessageUtils(main);
    private ConfigHandler cfg;
    private final String JDBC_URL = "jdbc:sqlserver://" + cfg.getConfig().getString("MSSQL.ip") + "\\" + cfg.getConfig().getString("MSSQL.database_name") + ":" + cfg.getConfig().getString("MSSQL.port");
    private final String USERNAME = cfg.getConfig().getString("MSSQL.username");
    private final String PASSWORD = cfg.getConfig().getString("MSSQL.password");

    public MSSQLWarnsHandler(Main _main) {
        main = _main;
    }

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
        try (Statement statement = connection.createStatement()) {
            statement.execute(createTableSQL);
            msgUtils.log("Table '" + tableName + "' created (if not already exists)...");
        }
    }

    public void create(Warn warn) {
        String insertQuery = "INSERT INTO (warner, warned,reason,date) VALUES (?,?,?,?)";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
            stmt.setString(0, warn.getWarner().getId().toString());
            stmt.setString(1, warn.getWarned().getId().toString());
            stmt.setString(2, warn.getReason());
            stmt.setString(3, warn.getDate());
            stmt.executeUpdate();
        } catch (SQLException e) {
            msgUtils.severe(e.getMessage());
        }
    }

    public Warn read(Warn warn) {
        String query = "SELECT * FROM warns WHERE id = ? AND warned = ?";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)
        ) {
            stmt.setInt(0, warn.getId());
            stmt.setString(1, warn.getWarned().getId().toString());
            try (ResultSet rs = stmt.executeQuery()) {
                Warn w = new Warn();
                w.setId(rs.getInt("id"));
                w.setWarner(DtoShaper.userShaper(main.getServer().getPlayer(UUID.fromString(rs.getString("warner")))));
                w.setWarned(DtoShaper.userShaper(main.getServer().getPlayer(UUID.fromString(rs.getString("warned")))));
                w.setReason(rs.getString("reason"));
                w.setDate(rs.getString("date"));
                return w;
            }
        } catch (SQLException e) {
            msgUtils.severe(e.getMessage());
        }

        return null;
    }

    public void update(Warn warn) {
        String query = "UPDATE warns SET reason = ? WHERE id = ? and warned = ?";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)
        ) {
            stmt.setString(0, warn.getReason());
            stmt.setInt(1, warn.getId());
            stmt.setString(2, warn.getWarned().getId().toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            msgUtils.severe(e.getMessage());
        }
    }

    public void delete(Warn wan) {

    }

    public List<Warn> getAll(User u) {

    }

    public List<Warn> getWarningsFromUser(User u) {

    }
}
