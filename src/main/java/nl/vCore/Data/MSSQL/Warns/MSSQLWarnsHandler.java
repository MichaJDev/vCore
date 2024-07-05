package nl.vCore.Data.MSSQL.Warns;

import nl.vCore.Config.ConfigHandler;
import nl.vCore.Dto.User;
import nl.vCore.Dto.Warn;
import nl.vCore.Main;
import nl.vCore.Utils.DtoShaper;
import nl.vCore.Utils.MessageUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

;
public class MSSQLWarnsHandler {
    private final Main main;
    MessageUtils msgUtils;
    private ConfigHandler cfg;

    public MSSQLWarnsHandler(Main _main) {
        main = _main;
        cfg = new ConfigHandler(main);
        msgUtils = new MessageUtils(main);
    }

    private final String JDBC_URL = "jdbc:sqlserver://" + cfg.getConfig().getString("MSSQL.ip") + "\\" + cfg.getConfig().getString("MSSQL.database_name") + ":" + cfg.getConfig().getString("MSSQL.port");
    private final String USERNAME = cfg.getConfig().getString("MSSQL.username");
    private final String PASSWORD = cfg.getConfig().getString("MSSQL.password");

    public void createWarnsTableIfNotExists() throws SQLException {
        Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        String tableName = "warns";
        String createTableSQL = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                "id VARCHAR(255) PRIMARY KEY," +
                "warner VARCHAR(50)," +
                "warned VARCHAR(50)," +
                "reason text," +
                "date VARCHAR(50)" +
                ");";
        try (Statement statement = connection.createStatement()) {
            statement.execute(createTableSQL);
        }
    }

    public void create(Warn warn) {
        String insertQuery = "INSERT INTO (id, warner, warned,reason,date) VALUES (?,?,?,?,?)";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
            stmt.setString(0, warn.getId().toString());
            stmt.setString(1, warn.getWarner().getId().toString());
            stmt.setString(2, warn.getWarned().getId().toString());
            stmt.setString(3, warn.getReason());
            stmt.setString(4, warn.getDate());
            stmt.executeUpdate();
        } catch (SQLException e) {
            msgUtils.severe(e.getMessage());
        }
    }

    public Warn read(Warn warn) {
        String query = "SELECT * FROM warns WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)
        ) {
            stmt.setString(0, warn.getId().toString());
            try (ResultSet rs = stmt.executeQuery()) {
                Warn w = new Warn();
                w.setId(UUID.fromString(rs.getString("id")));
                w.setWarner(DtoShaper.userShaper(Objects.requireNonNull(main.getServer().getPlayer(UUID.fromString(rs.getString("warner"))))));
                w.setWarned(DtoShaper.userShaper(Objects.requireNonNull(main.getServer().getPlayer(UUID.fromString(rs.getString("warned"))))));
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
        String query = "UPDATE warns SET reason = ? WHERE id = ? AND warned = ?";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)
        ) {
            stmt.setString(0, warn.getReason());
            stmt.setString(1, warn.getId().toString());
            stmt.setString(2, warn.getWarned().getId().toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            msgUtils.severe(e.getMessage());
        }
    }

    public void delete(Warn w) {
        String query = "DELETE FROM warns WHERE id = ? AND warned = ?";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)
        ) {
            stmt.setString(0, w.getId().toString());
            stmt.setString(1, w.getWarned().getId().toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            msgUtils.severe(e.getMessage());
        }
    }

    public List<Warn> getAll() {
        List<Warn> warns = new ArrayList<>();
        String query = "SELECT * FROM warns";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)
        ) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Warn w = new Warn();
                    w.setId(UUID.fromString(rs.getString("id")));
                    w.setWarner(DtoShaper.userShaper(Objects.requireNonNull(main.getServer().getPlayer(UUID.fromString(rs.getString("warner"))))));
                    w.setWarned(DtoShaper.userShaper(Objects.requireNonNull(main.getServer().getPlayer(UUID.fromString(rs.getString("warned"))))));
                    w.setReason(rs.getString("reason"));
                    w.setDate(rs.getString("date"));
                    warns.add(w);
                }
            }
        } catch (SQLException e) {
            msgUtils.severe(e.getMessage());
        }
        return warns;
    }

    public List<Warn> getWarningsFromUser(User u) {
        List<Warn> warns = new ArrayList<>();
        String query = "SELECT * FROM warns WHERE warned = ?";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)
        ) {
            stmt.setString(0, u.getId().toString());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Warn w = new Warn();
                    w.setId(UUID.fromString(rs.getString("id")));
                    w.setWarner(DtoShaper.userShaper(Objects.requireNonNull(main.getServer().getPlayer(UUID.fromString(rs.getString("warner"))))));
                    w.setWarned(DtoShaper.userShaper(Objects.requireNonNull(main.getServer().getPlayer(UUID.fromString(rs.getString("warned"))))));
                    w.setReason(rs.getString("reason"));
                    w.setDate(rs.getString("date"));
                    warns.add(w);
                }
            }
        } catch (SQLException e) {
            msgUtils.severe(e.getMessage());
        }
        return warns;
    }
}
