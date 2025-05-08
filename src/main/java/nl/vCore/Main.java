package nl.vCore;

import nl.vCore.Config.ConfigHandler;
import nl.vCore.Data.DatabaseConnectionManager;
import nl.vCore.Data.Factories.MSSQL.MSSQLUserFactory;
import nl.vCore.Data.GlobalDatabasingHandler;
import nl.vCore.Listeners.UserListener;
import nl.vCore.Utils.MessageUtils;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main plugin class for vCore
 * 
 * @version 1.1.0
 * @author vCore Team
 */
public final class Main extends JavaPlugin {

    private static Main instance;
    private MessageUtils msgUtils;
    private ConfigHandler configHandler;
    private DatabaseConnectionManager dbManager;
    
    // Plugin version information
    private static final String PLUGIN_VERSION = "1.1.0";
    private static final String REQUIRED_SERVER_VERSION = "1.16.5+";

    @Override
    public void onEnable() {
        // Set the instance first
        instance = this;
        
        // Initialize utilities
        msgUtils = new MessageUtils(this);
        msgUtils.log("vCore v" + PLUGIN_VERSION + " is starting up...");
        msgUtils.log("Recommended server version: " + REQUIRED_SERVER_VERSION);
        
        // Setup config
        msgUtils.log("Initializing configuration...");
        configHandler = new ConfigHandler(this);

        if(!configHandler.doesConfigFileExist()){
            msgUtils.log("No config found, creating default configuration...");
            configHandler.createNewConfigFile();
        }
        
        // Initialize database connection manager
        msgUtils.log("Initializing database connection manager...");
        dbManager = DatabaseConnectionManager.getInstance(this);
        
        // Register commands and listeners
        msgUtils.log("Registering commands...");
        registerCommands();
        
        msgUtils.log("Registering listeners...");
        registerListeners();
        
        // Handle first-time setup
        if(configHandler.isFirstTime()){
            msgUtils.log("First time setup detected...");
            try {
                GlobalDatabasingHandler.triggerFirstTime();
                
                // Set FirstTime to false after initial setup
                configHandler.setFirstTime(false);
                msgUtils.log("First time setup completed successfully!");
            } catch (Exception e) {
                msgUtils.severe("Error during first time setup: " + e.getMessage());
                e.printStackTrace();
                return;
            }
        }
        
        // Compare database with offline players and update if needed
        try {
            msgUtils.log("Comparing server offline player data to user database...");
            if (configHandler.isMSSQL()) {
                if (!MSSQLUserFactory.compareDB()) {
                    msgUtils.warn("Database not in sync with player data. Updating database...");
                    MSSQLUserFactory.elevateDB();
                    msgUtils.log("Database updated successfully!");
                } else {
                    msgUtils.log("Database is in sync with player data!");
                }
            } else if (configHandler.isMySQL()) {
                // Similar logic for MySQL could be implemented here in a future update
                msgUtils.log("MySQL database check not implemented yet - will be added in a future update.");
            }
        } catch (Exception e) {
            msgUtils.severe("Error comparing database: " + e.getMessage());
            e.printStackTrace();
        }
        
        msgUtils.log("vCore v" + PLUGIN_VERSION + " has been enabled successfully!");
    }

    @Override
    public void onDisable() {
        try {
            msgUtils.log("Ensuring database is up-to-date before shutdown...");
            
            if (configHandler.isMSSQL() && !MSSQLUserFactory.compareDB()) {
                msgUtils.warn("Synchronizing local player data with database...");
                MSSQLUserFactory.elevateDB();
                msgUtils.log("Database synchronized successfully!");
            } else if (configHandler.isMySQL()) {
                // Similar logic for MySQL could be implemented here in a future update
                msgUtils.log("MySQL database update on shutdown not implemented yet - will be added in a future update.");
            }
        } catch (Exception e) {
            msgUtils.severe("Error updating database on shutdown: " + e.getMessage());
            e.printStackTrace();
        }
        
        msgUtils.log("vCore v" + PLUGIN_VERSION + " has been disabled.");
    }

    private void registerCommands(){
        // Commands will be registered here
        msgUtils.log("No commands registered yet - will be implemented in a future update");
    }
    
    private void registerListeners(){
        getServer().getPluginManager().registerEvents(new UserListener(), this);
        msgUtils.log("UserListener registered successfully");
    }

    /**
     * Get the plugin instance
     * 
     * @return The plugin instance
     */
    public static Main getInstance(){
        return instance;
    }
    
    /**
     * Get the plugin version
     * 
     * @return The current version of the plugin
     */
    public static String getPluginVersion() {
        return PLUGIN_VERSION;
    }
}
