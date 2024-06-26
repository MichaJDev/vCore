package nl.vCore.Data.MySql;

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
        this. msgUtils = new MessageUtils(main);
        buildJdbcUrl();
    }

    private String buildJdbcUrl() {
        String ip = configHandler.getConfig().getString("MySQL.ip");
        int port = configHandler.getConfig().getInt("MySQL.port");
        String dbName = configHandler.getConfig().getString("MySQL.database_name");
        return "jdbc:mysql://" + ip + ":" + port + "/" + dbName;
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
}
