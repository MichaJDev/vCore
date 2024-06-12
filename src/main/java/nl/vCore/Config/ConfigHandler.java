package nl.vCore.Config;

import java.io.File;
import java.io.IOException;

import nl.vCore.Main;
import nl.vCore.Utils.MessageUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigHandler {

    private Main main = Main.getInstance(); //
    private FileConfiguration config;
    private MessageUtils msgUtils;
    public ConfigHandler(Main _main) {
        main = _main;
        msgUtils = new MessageUtils(main);
    }

    public boolean doesConfigFileExist() {
        msgUtils.log("Checking if config file exists...");
        File file = new File(main.getDataFolder(), "config.yml");
        if (file.exists()) {
            msgUtils.log("Config file exists...");
            return true;
        }
        return false;
    }
    public boolean isFirstTime(){
        FileConfiguration cfg = getConfig();
        return cfg.getBoolean("FirstTime");
    }
    public boolean isYml(){
        FileConfiguration cfg = getConfig();
        return cfg.getBoolean("Database.yml");
    }
    public boolean isMSSQL(){
        FileConfiguration cfg = getConfig();
        return cfg.getBoolean("Database.mssql");
    }

    public boolean isMYSSQL(){
        FileConfiguration cfg = getConfig();
        return cfg.getBoolean("Database.mysql");
    }
    public void createNewConfigFile(){
        File file = new File(main.getDataFolder(), "config.yml");
        if(!file.exists()){
            try{
                msgUtils.log("Trying to create new config...");
                file.createNewFile();
                msgUtils.log("Filling new config...");
                fillConfigFile(file);
                msgUtils.log("Success...");
            }catch(IOException e){
                msgUtils.log("Config file creation failedERROR MESSAGE BELOW...");
                msgUtils.severe(e.getMessage());
            }
        }

    }
    private File getConfigFile(){
        if(doesConfigFileExist()){
            return new File(main.getDataFolder(), "config.yml");
        }
        return null;
    }
    private void fillConfigFile(File file){
        if(file != null){

            FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
            cfg.addDefault("FirstTime", true);
            cfg.addDefault("Database.yml", true);
            cfg.addDefault("Database.mysql", false);
            cfg.addDefault("Database.mssql", false);

            // Set MySQL connection details
            cfg.addDefault("MySQL.ip", "127.0.0.1");
            cfg.addDefault("MySQL.port", 3306);
            cfg.addDefault("MySQL.database_name", "mydb");
            cfg.addDefault("MySQL.username", "myuser");
            cfg.addDefault("MySQL.password", "mypassword");

            // Set MSSQL connection details
            cfg.addDefault("MSSQL.ip", "192.168.1.100");
            cfg.addDefault("MSSQL.port", 1433);
            cfg.addDefault("MSSQL.database_name", "mssqldb");
            cfg.addDefault("MSSQL.username", "mssqluser");
            cfg.addDefault("MSSQL.password", "mssqlpassword");

            // Save the config
            cfg.options().copyDefaults(true);
            try{
                cfg.save(file);
            }catch(IOException e){
                msgUtils.severe(e.getMessage());
            }
        }else{
            msgUtils.severe("[DEBUG] Config file was null...");
        }


    }

    public FileConfiguration getConfig(){
        if(getConfigFile() != null) {
            return YamlConfiguration.loadConfiguration(getConfigFile());
        }
        return null;
    }
}
