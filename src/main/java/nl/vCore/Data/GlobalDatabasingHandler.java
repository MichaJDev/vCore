package nl.vCore.Data;

import nl.vCore.Config.ConfigHandler;
import nl.vCore.Data.Factories.MSSQL.*;
import nl.vCore.Data.Factories.MySQL.MySQLBansFactory;
import nl.vCore.Data.Factories.MySQL.MySQLHomesFactory;
import nl.vCore.Data.Factories.MySQL.MySQLUserFactory;
import nl.vCore.Data.Factories.MySQL.MySQLWarnsFactory_new;
import nl.vCore.Data.Factories.MySQL.MySQLWarpsFactory;
import nl.vCore.Main;
import nl.vCore.Utils.MessageUtils;

/**
 * Handles database initialization and first-time setup
 * 
 * @version 1.1.0
 */
public class GlobalDatabasingHandler {

    /**
     * Triggers the first-time setup process based on the selected database type
     */
    public static void triggerFirstTime() {
        ConfigHandler cfg = new ConfigHandler(Main.getInstance());
        MessageUtils msgUtils = new MessageUtils(Main.getInstance());
        msgUtils.log("First time starting up vCore, initializing database setup...");
        
        // Check which database type is enabled
        boolean ymlEnabled = cfg.isYml();
        boolean mssqlEnabled = cfg.isMSSQL();
        boolean mysqlEnabled = cfg.isMySQL();
        
        // Count how many database types are enabled
        int enabledDatabases = 0;
        if (ymlEnabled) enabledDatabases++;
        if (mssqlEnabled) enabledDatabases++;
        if (mysqlEnabled) enabledDatabases++;
        
        // Ensure only one database type is enabled
        if (enabledDatabases != 1) {
            msgUtils.severe("Invalid database configuration detected:");
            msgUtils.severe("YML enabled: " + ymlEnabled);
            msgUtils.severe("MSSQL enabled: " + mssqlEnabled);
            msgUtils.severe("MySQL enabled: " + mysqlEnabled);
            msgUtils.severe("Please ensure exactly ONE database type is enabled in config.yml");
            msgUtils.severe("Disabling plugin to prevent data corruption...");
            Main.getInstance().getServer().getPluginManager().disablePlugin(Main.getInstance());
            return;
        }
        
        // Proceed with appropriate database setup
        if (ymlEnabled) {
            msgUtils.log("YML database enabled, triggering YML startup sequence...");
            triggerYmlFirstTimeTask();
        } else if (mssqlEnabled) {
            msgUtils.log("MSSQL database enabled, triggering MSSQL startup sequence...");
            triggerMSSQLFirstTimeTask();
        } else if (mysqlEnabled) {
            msgUtils.log("MySQL database enabled, triggering MySQL startup sequence...");
            triggerMySQLFirstTimeTask();
        }
    }

    /**
     * Sets up YML database (placeholder for future implementation)
     */
    private static void triggerYmlFirstTimeTask() {
        // YML database implementation will be added in future updates
        MessageUtils msgUtils = new MessageUtils(Main.getInstance());
        msgUtils.log("YAML database support is planned for future releases.");
    }

    /**
     * Sets up MSSQL database tables
     */
    private static void triggerMSSQLFirstTimeTask() {
        MessageUtils msgUtils = new MessageUtils(Main.getInstance());
        msgUtils.log("Creating MSSQL tables...");
        
        try {
            // Create each table with error handling
            try {
                msgUtils.log("Creating users table...");
                MSSQLUserFactory.createTable();
            } catch (Exception e) {
                msgUtils.severe("Failed to create users table: " + e.getMessage());
            }
            
            try {
                msgUtils.log("Creating bans table...");
                MSSQLBanFactory.createTable();
            } catch (Exception e) {
                msgUtils.severe("Failed to create bans table: " + e.getMessage());
            }
            
            try {
                msgUtils.log("Creating homes table...");
                MSSQLHomesFactory.createTable();
            } catch (Exception e) {
                msgUtils.severe("Failed to create homes table: " + e.getMessage());
            }
            
            try {
                msgUtils.log("Creating warps table...");
                MSSQLWarpsFactory.createTable();
            } catch (Exception e) {
                msgUtils.severe("Failed to create warps table: " + e.getMessage());
            }
            
            try {
                msgUtils.log("Creating warns table...");
                MSSQLWarnsFactory.createTable();
            } catch (Exception e) {
                msgUtils.severe("Failed to create warns table: " + e.getMessage());
            }
            
            msgUtils.log("MSSQL tables creation process completed!");
        } catch (Exception e) {
            msgUtils.severe("Unexpected error during MSSQL tables creation: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Sets up MySQL database tables
     */
    private static void triggerMySQLFirstTimeTask() {
        MessageUtils msgUtils = new MessageUtils(Main.getInstance());
        msgUtils.log("Creating MySQL tables...");
        
        try {
            // Create each table with error handling
            try {
                msgUtils.log("Creating users table...");
                MySQLUserFactory.createTable();
            } catch (Exception e) {
                msgUtils.severe("Failed to create users table: " + e.getMessage());
            }
            
            try {
                msgUtils.log("Creating bans table...");
                MySQLBansFactory.createTable();
            } catch (Exception e) {
                msgUtils.severe("Failed to create bans table: " + e.getMessage());
            }
            
            try {
                msgUtils.log("Creating homes table...");
                MySQLHomesFactory.createTable();
            } catch (Exception e) {
                msgUtils.severe("Failed to create homes table: " + e.getMessage());
            }
            
            try {
                msgUtils.log("Creating warps table...");
                MySQLWarpsFactory.createTable();
            } catch (Exception e) {
                msgUtils.severe("Failed to create warps table: " + e.getMessage());
            }
            
            try {
                msgUtils.log("Creating warns table...");
                MySQLWarnsFactory_new.createTable();
            } catch (Exception e) {
                msgUtils.severe("Failed to create warns table: " + e.getMessage());
            }
            
            msgUtils.log("MySQL tables creation process completed!");
        } catch (Exception e) {
            msgUtils.severe("Unexpected error during MySQL tables creation: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
