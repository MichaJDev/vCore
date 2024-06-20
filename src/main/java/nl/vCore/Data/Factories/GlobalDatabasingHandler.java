package nl.vCore.Data.Factories;

import nl.vCore.Config.ConfigHandler;
import nl.vCore.Data.Factories.MSSQL.*;
import nl.vCore.Main;
import nl.vCore.Utils.MessageUtils;

public class GlobalDatabasingHandler {

    public static void triggerFirstTime() {
        ConfigHandler cfg = new ConfigHandler(Main.getInstance());
        MessageUtils msgUtils = new MessageUtils(Main.getInstance());
        msgUtils.log("First time starting up vCore, Initialising startup sequence...");
        if (cfg.isYml() && !cfg.isMSSQL() && !cfg.isMYSSQL()) {
            msgUtils.log("YML Enabled, triggering YML Startup Sequence...");
            GlobalDatabasingHandler.triggerYmlFirstTimeTask();
        } else if (cfg.isMSSQL() && !cfg.isYml() && !cfg.isMYSSQL()) {
            msgUtils.log("MSSQL Enabled, triggering MSSQL Startup Sequence...");
            GlobalDatabasingHandler.triggerMSSQLFirstTimeTask();
        } else if (cfg.isMYSSQL() && !cfg.isMSSQL() && !cfg.isMYSSQL()) {
            msgUtils.log("MYSQL Enabled, triggering MYSQL Startup Sequence...");
            GlobalDatabasingHandler.triggerMYSQLFirstTimeTask();
        } else {
            msgUtils.warn("More than 1 Database type is selected as true...");
            msgUtils.warn("Please make sure only 1 type database is selected in the config.yml...");
            msgUtils.warn("Disabling plugin to not corrupt anything...");
            Main.getInstance().getServer().getPluginManager().disablePlugin(Main.getInstance());
        }


    }

    private static void triggerYmlFirstTimeTask() {

    }

    private static void triggerMSSQLFirstTimeTask() {
        MSSQLUserFactory.createTable();
        MSSQLBanFactory.createTable();
        MSSQLHomesFactory.createTable();
        MSSQLWarpsFactory.createTable();
        MSSQLWarnsFactory.createTable();
    }

    private static void triggerMYSQLFirstTimeTask() {
    }
}
