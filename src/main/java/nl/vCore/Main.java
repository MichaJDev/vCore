package nl.vCore;

import nl.vCore.Config.ConfigHandler;
import nl.vCore.Data.Factories.MSSQL.MSSQLUserFactory;
import nl.vCore.Data.GlobalDatabasingHandler;
import nl.vCore.Listeners.UserListener;
import nl.vCore.Utils.MessageUtils;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private static Main main;
    public static Main getInstance(){
        return main;
    }
    private final MessageUtils msgUtils  = new MessageUtils(this);

    @Override
    public void onEnable() {
        msgUtils.log("Getting Commands...");
        getCommands();
        msgUtils.log("Getting Listeners...");
        getListeners();
        msgUtils.log("Checking config file...");
        ConfigHandler cfgHandler = new ConfigHandler(this);

        if(!cfgHandler.doesConfigFileExist()){
            cfgHandler.createNewConfigFile();
        }
        if(cfgHandler.isFirstTime()){
            GlobalDatabasingHandler.triggerFirstTime();
        }
        msgUtils.warn("Comparing Server Offline Player Logging to User Database...");


    }

    @Override
    public void onDisable() {
        msgUtils.warn("Comparing MinecraftServer Local OfflinePlayers to User Database before shutdown...");
        if(!MSSQLUserFactory.compareDB()){
            msgUtils.warn("DB Not the same as Local OfflinePlayers array... Updating...");
            MSSQLUserFactory.elevateDB();
        }
    }

    private void getCommands(){

    }
    private void getListeners(){
        getServer().getPluginManager().registerEvents(new UserListener(), this);
    }


}
