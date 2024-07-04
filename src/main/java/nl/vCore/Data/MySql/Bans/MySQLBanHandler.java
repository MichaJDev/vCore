package nl.vCore.Data.MySql.Bans;

import nl.vCore.Config.ConfigHandler;
import nl.vCore.Dto.Ban;
import nl.vCore.Main;
import nl.vCore.Utils.DtoShaper;
import nl.vCore.Utils.MessageUtils;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.UUID;

public class MySQLBanHandler {
    private final ConfigHandler configHandler;
    private final String url;
    private final String user;
    private final String password;
    private final MessageUtils msgUtils;

    public MySQLBanHandler(Main main) {
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

    public void create(Ban ban) {
        String sql = "INSERT (bannedUser, banner, reason, date) VALUES ( ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(0, ban.getBannedUser().getId().toString());
            stmt.setString(1, ban.getBanner().getId().toString());
            stmt.setString(2, ban.getReason());
            stmt.setString(3, ban.getDate());
            stmt.executeUpdate();
        } catch (SQLException e) {
            msgUtils.severe(e.getMessage());
        }
    }

    public Ban read(int id) {
        String sql = "SELECT * FROM bans WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Ban b = new Ban();
                b.setId(rs.getInt("id"));
                b.setBannedUser(DtoShaper.userShaper((Player) Main.getInstance().getServer().getOfflinePlayer(UUID.fromString(rs.getString("bannedUserId")))));
                b.setBanner(DtoShaper.userShaper((Player) Main.getInstance().getServer().getOfflinePlayer(UUID.fromString(rs.getString("bannerId")))));
                b.setReason(rs.getString("reason"));
                b.setDate(rs.getString("date"));
                return b;
            }
        } catch (SQLException e) {
            msgUtils.severe(e.getMessage());
        }
        return null;
    }

    public void update(Ban b) {
        String sql = "UPDATE bans SET reason = ? AND date = ? WHERE id = ? ";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(0, b.getReason());
            stmt.setString(1, b.getDate());
            stmt.setInt(2, b.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            msgUtils.severe(e.getMessage());
        }
    }

    public void delete(Ban b) {
        String sql = "DELETE FROM bans WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(0, b.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            msgUtils.severe(e.getMessage());
        }
    }

    public void createBansTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS bans (" +
                "id INT PRIMARY KEY AUTO_INCREMENT, " +
                "bannedUser VARCHAR(255), " +
                "banner VARCHAR(255), " +
                "reason VARCHAR(255), " +
                "date VARCHAR(10));";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            msgUtils.severe(e.getMessage());
        }
    }

}
