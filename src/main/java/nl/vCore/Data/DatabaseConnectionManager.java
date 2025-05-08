package nl.vCore.Data;

import nl.vCore.Config.ConfigHandler;
import nl.vCore.Main;
import nl.vCore.Utils.MessageUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Central manager for database connections to improve connection handling and reuse
 */
public class DatabaseConnectionManager {
    private static DatabaseConnectionManager instance;
    private final ConfigHandler configHandler;
    private final MessageUtils msgUtils;
    
    // Connection details
    private String mssqlJdbcUrl;
    private String mssqlUsername;
    private String mssqlPassword;
    
    private String mysqlJdbcUrl;
    private String mysqlUsername;
    private String mysqlPassword;
    
    private DatabaseConnectionManager(Main main) {
        this.configHandler = new ConfigHandler(main);
        this.msgUtils = new MessageUtils(main);
        initConnectionParameters();
    }
    
    public static synchronized DatabaseConnectionManager getInstance(Main main) {
        if (instance == null) {
            instance = new DatabaseConnectionManager(main);
        }
        return instance;
    }
    
    private void initConnectionParameters() {
        // MSSQL connection parameters
        String mssqlServer = configHandler.getConfig().getString("MSSQL.ip");
        String mssqlDatabase = configHandler.getConfig().getString("MSSQL.database_name");
        int mssqlPort = configHandler.getConfig().getInt("MSSQL.port");
        
        mssqlJdbcUrl = "jdbc:sqlserver://" + mssqlServer + ":" + mssqlPort + 
                       ";databaseName=" + mssqlDatabase + ";encrypt=false";
        mssqlUsername = configHandler.getConfig().getString("MSSQL.username");
        mssqlPassword = configHandler.getConfig().getString("MSSQL.password");
        
        // MySQL connection parameters
        String mysqlServer = configHandler.getConfig().getString("MySQL.ip");
        String mysqlDatabase = configHandler.getConfig().getString("MySQL.database_name");
        int mysqlPort = configHandler.getConfig().getInt("MySQL.port");
        
        mysqlJdbcUrl = "jdbc:mysql://" + mysqlServer + ":" + mysqlPort + "/" + 
                       mysqlDatabase + "?useSSL=false&allowPublicKeyRetrieval=true";
        mysqlUsername = configHandler.getConfig().getString("MySQL.username");
        mysqlPassword = configHandler.getConfig().getString("MySQL.password");
    }
    
    /**
     * Get a connection to the MSSQL database
     *
     * @return A connection to the database or null if there was an error
     */
    public Connection getMssqlConnection() {
        try {
            // Load the JDBC driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return DriverManager.getConnection(mssqlJdbcUrl, mssqlUsername, mssqlPassword);
        } catch (ClassNotFoundException e) {
            msgUtils.severe("MSSQL JDBC Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            msgUtils.severe("Failed to connect to MSSQL database: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Get a connection to the MySQL database
     *
     * @return A connection to the database or null if there was an error
     */
    public Connection getMysqlConnection() {
        try {
            // Load the JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(mysqlJdbcUrl, mysqlUsername, mysqlPassword);
        } catch (ClassNotFoundException e) {
            msgUtils.severe("MySQL JDBC Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            msgUtils.severe("Failed to connect to MySQL database: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Close a connection safely
     *
     * @param connection The connection to close
     */
    public void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                msgUtils.severe("Error closing database connection: " + e.getMessage());
            }
        }
    }
} 