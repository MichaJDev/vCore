package nl.vCore.Data.MSSQL.Bans;

import nl.vCore.Config.ConfigHandler;
import nl.vCore.Dto.Ban;
import nl.vCore.Dto.User;
import nl.vCore.Main;
import nl.vCore.Utils.MessageUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MSSQLBansHandler {

    private Main main = Main.getInstance();

    MessageUtils msgUtils = new MessageUtils(main);
    private ConfigHandler cfg;
    private final String JDBC_URL = "jdbc:sqlserver://" + cfg.getConfig().getString("MSSQL.ip") + "\\" + cfg.getConfig().getString("MSSQL.database_name") + ":" + cfg.getConfig().getString("MSSQL.port");
    private final String USERNAME = cfg.getConfig().getString("MSSQL.username");
    private final String PASSWORD = cfg.getConfig().getString("MSSQL.password");
    public MSSQLBansHandler (Main _main){
        main = _main;
    }
    public void createBanTableIfNotExists() throws SQLException {
        Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
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

    public void createBan(Ban ban) {

    }

    public List<Ban> getAllBans() {
        List<Ban> bList = new ArrayList<Ban>();

        return bList;
    }

    public List<Ban> getAllBansFromUser(User user) {
        List<Ban> ubList = new ArrayList<Ban>();

        return ubList;
    }

    public void updateBan(Ban ban) {

    }

    public void deleteBan(Ban ban) {

    }

}
