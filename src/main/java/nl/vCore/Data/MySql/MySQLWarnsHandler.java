package nl.vCore.Data.MySql;

import nl.vCore.Config.ConfigHandler;
import nl.vCore.Dto.User;
import nl.vCore.Dto.Warn;
import nl.vCore.Main;
import nl.vCore.Utils.MessageUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
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

    }

    public Warn read(User u, UUID id){
        Warn w = new Warn();

        return w;
    }

    public void update(Warn w){

    }

    public void delete(Warn w){

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
