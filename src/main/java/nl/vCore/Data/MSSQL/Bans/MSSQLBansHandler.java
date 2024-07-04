package nl.vCore.Data.MSSQL.Bans;

import nl.vCore.Config.ConfigHandler;
import nl.vCore.Dto.Ban;
import nl.vCore.Dto.User;
import nl.vCore.Main;
import nl.vCore.Utils.DtoShaper;
import nl.vCore.Utils.MessageUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class MSSQLBansHandler {

    private final Main main;
    MessageUtils msgUtils;
    private final ConfigHandler cfg;
    private final String url;
    private final String USERNAME;
    private final String PASSWORD;

    public MSSQLBansHandler(Main main) {
        this.main = main;
        this.cfg = new ConfigHandler(main);
        this.url = buildJdbcUrl();
        this.USERNAME = cfg.getConfig().getString("MySQL.username");
        this.PASSWORD = cfg.getConfig().getString("MySQL.password");
        this.msgUtils = new MessageUtils(main);
    }

    public void createBanTableIfNotExists() throws SQLException {
        Connection connection = DriverManager.getConnection(url, USERNAME, PASSWORD);
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

    private String buildJdbcUrl() {
        String ip = cfg.getConfig().getString("MSSQL.ip");
        int port = cfg.getConfig().getInt("MSSQL.port");
        String dbName = cfg.getConfig().getString("MSSQL.database_name");
        return "jdbc:sqlserver://" + ip + ":" + port + "/" + dbName;
    }

    public void createBan(Ban ban) {
        String insertQuery = "INSERT INTO bans(banner, banned, reason, date) VALUES (?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(url, USERNAME, PASSWORD);
             PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
            insertStatement.setString(0, ban.getBannedUser().getId().toString());
            insertStatement.setString(1, ban.getBanner().getId().toString());
            insertStatement.setString(2, ban.getReason());
            insertStatement.setString(3, ban.getDate());
            insertStatement.executeUpdate();
        } catch (SQLException e) {
            msgUtils.severe(e.getMessage());
        }
    }

    public List<Ban> getAllBans() {
        List<Ban> bList = new ArrayList<>();
        String selectAllQuery = "SELECT * FROM bans";
        try (Connection connection = DriverManager.getConnection(url, USERNAME, PASSWORD);
             PreparedStatement selectAllStatement = connection.prepareStatement(selectAllQuery);
             ResultSet rs = selectAllStatement.executeQuery()) {

            while (rs.next()) {
                Ban b = new Ban();
                b.setId(rs.getInt("id"));
                b.setBannedUser(DtoShaper.userShaper(Objects.requireNonNull(main.getServer().getPlayer(UUID.fromString(rs.getString("banned"))))));
                b.setBanner(DtoShaper.userShaper(Objects.requireNonNull(main.getServer().getPlayer(UUID.fromString(rs.getString("banner"))))));
                b.setReason(rs.getString("reason"));
                b.setDate(rs.getString("date"));
                bList.add(b);
            }
        } catch (SQLException e) {
            msgUtils.severe(e.getMessage());
        }
        return bList;
    }

    public List<Ban> getAllBansFromUser(User user) {
        List<Ban> ubList = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(url, USERNAME, PASSWORD)) {
            String selectQuery = "SELECT * FROM bans WHERE banned = ?";
            try (PreparedStatement stmt = conn.prepareStatement(selectQuery)) {
                stmt.setString(0, user.getId().toString());
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Ban b = new Ban();
                        b.setId(rs.getInt("id"));
                        b.setBannedUser(DtoShaper.userShaper(Objects.requireNonNull(main.getServer().getPlayer(UUID.fromString(rs.getString("banned"))))));
                        b.setBanner(DtoShaper.userShaper(Objects.requireNonNull(main.getServer().getPlayer(UUID.fromString(rs.getString("banner"))))));
                        b.setReason(rs.getString("reason"));
                        b.setDate(rs.getString("date"));
                        ubList.add(b);
                    }
                }
            }
        } catch (SQLException e) {
            msgUtils.severe(e.getMessage());
        }
        return ubList;
    }

    public void updateBan(Ban ban) {
        String updateQuery = "UPDATE bans SET reason = ? WHERE banner = ? AND id = ?";
        try (Connection conn = DriverManager.getConnection(url, USERNAME, PASSWORD);
             PreparedStatement updateStatement = conn.prepareStatement(updateQuery)) {
            updateStatement.setString(0, ban.getReason());
            updateStatement.setString(1, ban.getBannedUser().getId().toString());
            updateStatement.setInt(2, ban.getId());
            updateStatement.executeUpdate();
        } catch (SQLException e) {
            msgUtils.severe(e.getMessage());
        }
    }

    public void deleteBan(Ban ban) {
        String deleteQuery = "DELETE FROM bans WHERE banned = ? AND id = ?";
        try (Connection conn = DriverManager.getConnection(url, USERNAME, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(deleteQuery)) {
            stmt.setString(0, ban.getBannedUser().getId().toString());
            stmt.setInt(1, ban.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            msgUtils.severe(e.getMessage());
        }
    }

    public Ban read(User u, int id) {
        String query = "SELECT * From bans WHERE id = ? AND banned = ?";
        try (Connection conn = DriverManager.getConnection(url, USERNAME, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(0, id);
            stmt.setString(1, u.getId().toString());
            try (ResultSet rs = stmt.executeQuery(query)) {
                Ban b = new Ban();
                b.setId(rs.getInt("id"));
                b.setBanner(DtoShaper.userShaper(Objects.requireNonNull(main.getServer().getPlayer(UUID.fromString(rs.getString("banner"))))));
                b.setBannedUser(DtoShaper.userShaper(Objects.requireNonNull(main.getServer().getPlayer(UUID.fromString(rs.getString("banned"))))));
                b.setReason(rs.getString("reason"));
                b.setDate(rs.getString("date"));
                return b;
            }
        } catch (SQLException e) {
            msgUtils.severe(e.getMessage());
        }
        return null;
    }
}
