package nl.vCore.Config;

import java.io.File;
import java.io.IOException;

import nl.vCore.Main;
import nl.vCore.Utils.MessageUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * Handles all configuration-related operations
 * 
 * @version 1.1.0
 */
public class ConfigHandler {

    private final Main main;
    private FileConfiguration config;
    private final MessageUtils msgUtils;
    private File configFile;
    
    public ConfigHandler(Main _main) {
        main = _main;
        msgUtils = new MessageUtils(main);
        configFile = new File(main.getDataFolder(), "config.yml");
    }

    /**
     * Checks if the config file exists
     * 
     * @return true if the config file exists, false otherwise
     */
    public boolean doesConfigFileExist() {
        msgUtils.log("Checking if config file exists...");
        if (configFile.exists()) {
            msgUtils.log("Config file exists...");
            return true;
        }
        msgUtils.log("Config file does not exist...");
        return false;
    }
    
    /**
     * Checks if this is the first time the plugin is running
     * 
     * @return true if this is the first time, false otherwise
     */
    public boolean isFirstTime(){
        FileConfiguration cfg = getConfig();
        return cfg != null && cfg.getBoolean("FirstTime");
    }
    
    /**
     * Checks if YML database is enabled
     * 
     * @return true if YML is enabled, false otherwise
     */
    public boolean isYml(){
        FileConfiguration cfg = getConfig();
        return cfg != null && cfg.getBoolean("Database.yml");
    }
    
    /**
     * Checks if MSSQL database is enabled
     * 
     * @return true if MSSQL is enabled, false otherwise
     */
    public boolean isMSSQL(){
        FileConfiguration cfg = getConfig();
        return cfg != null && cfg.getBoolean("Database.mssql");
    }

    /**
     * Checks if MySQL database is enabled
     * 
     * @return true if MySQL is enabled, false otherwise
     */
    public boolean isMySQL(){
        FileConfiguration cfg = getConfig();
        return cfg != null && cfg.getBoolean("Database.mysql");
    }
    
    /**
     * Creates a new config file with default values
     */
    public void createNewConfigFile(){
        if(!configFile.exists()){
            try{
                // Create the plugin directory if it doesn't exist
                if (!main.getDataFolder().exists() && !main.getDataFolder().mkdirs()) {
                    msgUtils.severe("Failed to create plugin directory");
                    return;
                }
                
                msgUtils.log("Trying to create new config...");
                if (!configFile.createNewFile()) {
                    msgUtils.severe("Failed to create config file");
                    return;
                }
                
                msgUtils.log("Filling new config with default values...");
                fillConfigFile(configFile);
                msgUtils.log("Config created successfully...");
            } catch(IOException e) {
                msgUtils.severe("Config file creation failed: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Gets the config file
     * 
     * @return the config file or null if it doesn't exist
     */
    private File getConfigFile(){
        if(doesConfigFileExist()){
            return configFile;
        }
        return null;
    }
    
    /**
     * Fills the config file with default values
     * 
     * @param file the config file to fill
     */
    private void fillConfigFile(File file){
        if(file == null || !file.exists()){
            msgUtils.severe("Cannot fill null or non-existent config file");
            return;
        }

        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        cfg.options().header("vCore Configuration File v1.1.0\nEnsure only one database type is set to true");
        
        // Database settings
        // Comments will be added in future versions with Paper API
        cfg.addDefault("FirstTime", true);
        cfg.addDefault("Database.yml", true);
        cfg.addDefault("Database.mysql", false);
        cfg.addDefault("Database.mssql", false);

        // MySQL settings
        // Comments will be added in future versions with Paper API
        cfg.addDefault("MySQL.ip", "127.0.0.1");
        cfg.addDefault("MySQL.port", 3306);
        cfg.addDefault("MySQL.database_name", "mydb");
        cfg.addDefault("MySQL.username", "myuser");
        cfg.addDefault("MySQL.password", "mypassword");

        // MSSQL settings
        // Comments will be added in future versions with Paper API
        cfg.addDefault("MSSQL.ip", "192.168.1.100");
        cfg.addDefault("MSSQL.port", 1433);
        cfg.addDefault("MSSQL.database_name", "mssqldb");
        cfg.addDefault("MSSQL.username", "mssqluser");
        cfg.addDefault("MSSQL.password", "mssqlpassword");

        // Save the config
        cfg.options().copyDefaults(true);
        try{
            cfg.save(file);
        } catch(IOException e){
            msgUtils.severe("Failed to save config: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Gets the configuration
     * 
     * @return the configuration or null if the file doesn't exist
     */
    public FileConfiguration getConfig(){
        if (config == null) {
            File file = getConfigFile();
            if (file != null) {
                config = YamlConfiguration.loadConfiguration(file);
            }
        }
        return config;
    }
    
    /**
     * Reload the configuration from disk
     */
    public void reloadConfig() {
        if (configFile == null) {
            configFile = new File(main.getDataFolder(), "config.yml");
        }
        
        if (configFile.exists()) {
            config = YamlConfiguration.loadConfiguration(configFile);
            msgUtils.log("Config reloaded successfully");
        } else {
            msgUtils.warn("Config file does not exist, cannot reload");
        }
    }

    /**
     * Sets whether this is the first time the plugin is running
     * 
     * @param value true if this is the first time, false otherwise
     */
    public void setFirstTime(boolean value) {
        File file = getConfigFile();
        if (file != null) {
            FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
            cfg.set("FirstTime", value);
            try {
                cfg.save(file);
                msgUtils.log("FirstTime value updated to: " + value);
            } catch (IOException e) {
                msgUtils.severe("Failed to update FirstTime: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            msgUtils.severe("Cannot set FirstTime: config file doesn't exist");
        }
    }
    
    /**
     * Updates a specific configuration value and saves the config
     * 
     * @param path the path to the value
     * @param value the new value
     * @return true if successful, false otherwise
     */
    public boolean updateConfigValue(String path, Object value) {
        if (getConfig() == null) {
            return false;
        }
        
        getConfig().set(path, value);
        try {
            getConfig().save(configFile);
            return true;
        } catch (IOException e) {
            msgUtils.severe("Failed to save config update: " + e.getMessage());
            return false;
        }
    }
}
