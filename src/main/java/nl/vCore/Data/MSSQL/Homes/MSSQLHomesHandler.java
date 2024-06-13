package nl.vCore.Data.MSSQL.Homes;

import nl.vCore.Config.ConfigHandler;
import nl.vCore.Main;
import nl.vCore.Utils.MessageUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MSSQLHomesHandler {
    private Main main = Main.getInstance();
    MessageUtils msgUtils = new MessageUtils(main);
    private ConfigHandler cfg;
    private final String JDBC_URL = "jdbc:sqlserver://" + cfg.getConfig().getString("MSSQL.ip") + "\\" + cfg.getConfig().getString("MSSQL.database_name") + ":" + cfg.getConfig().getString("MSSQL.port");
    private final String USERNAME = cfg.getConfig().getString("MSSQL.username");
    private final String PASSWORD = cfg.getConfig().getString("MSSQL.password");

    public MSSQLHomesHandler(Main _main){
        main = _main;
    }
    public void createHomesTableIfNotExists() throws SQLException {
        Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        String tableName = "homes";
        String createTableSQL = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                "id INT PRIMARY KEY IDENTITY(1,1)," +
                "owner VARCHAR(50)," +
                "name VARCHAR(50)," +
                "x INT," +
                "y INT," +
                "z INT," +
                "world VARCHAR(50)" +
                ");";
        try (Statement statement = connection.createStatement()){
            statement.execute(createTableSQL);
            msgUtils.log("Table '" + tableName + "' created (if not already exists)...");
        }
    }
}
