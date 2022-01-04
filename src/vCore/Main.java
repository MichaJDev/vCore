package vCore;

import java.io.IOException;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import vCore.Commands.Info.InfoCmd;
import vCore.Config.ConfigBuilder;
import vCore.Config.Interface.IConfigBuilder;
import vCore.Data.Builders.Users.UserBuilder;
import vCore.Data.Builders.Users.Interface.IUserBuilder;
import vCore.Data.SQL.Database;
import vCore.Data.SQL.Interface.IDatabase;
import vCore.Dto.User.Interface.IUser;
import vCore.Listeners.Login.LoginListener;

public class Main extends JavaPlugin {

	private static Main main;
	private IUserBuilder _user;
	private IConfigBuilder _cfg;
	private IDatabase _db;

	private String host, port, database, username, password;

	public static Main getInstance() {
		return main;
	}

	@Override
	public void onEnable() {
		this.saveDefaultConfig();
		_user = new UserBuilder(this);
		_cfg = new ConfigBuilder(this);

		getLogger().info("Loading commands.......");
		getCommands();

		getLogger().info("Checking directories.......");
		if (!_user.userFolderExist()) {
			getLogger().info("User Folder doesnt exist......");
			getLogger().info("Starting setup.......");
			_user.Startup();
		}
		getLogger().info("Checking Config......");
		if (!_cfg.configExists()) {
			getLogger().info("Creating Config.......");
			_cfg.createConfig();
		}

		host = getConfig().getString("SQL.host");
		port = getConfig().getString("SQL.port");
		database = getConfig().getString("SQL.database");
		username = getConfig().getString("SQL.username");
		password = getConfig().getString("SQL.password");
		_db = new Database();
		getLogger().info("Checking if using MySQL.......");

		if (getConfig().getBoolean("useMYSQL")) {
			_db.setConnectionInfo(host, port, database, username, password);
			_db.setupDB();
		}
		getLogger().info("Loading Listeners........");
		getListeners();

	}

	@Override
	public void onDisable() {
		saveConfig();
		if (_user.getAllUserUUIDs() != null) {
			getLogger().info("Saving all UserFiles");
			for (UUID u : _user.getAllUserUUIDs()) {
				IUser user = _user.get(u);
				FileConfiguration cfg = _user.getUserCfg(user);
				getLogger().info("File for: " + user.getName() + " getting saved");
				try {
					cfg.save(_user.getUserFile(user));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if (getConfig().getBoolean("useMYSQL")) {

		}
	}

	private void getCommands() {
		this.getCommand("info").setExecutor(new InfoCmd(this, _user));
	}

	private void getListeners() {
		getServer().getPluginManager().registerEvents(new LoginListener(this, _user, _db), this);
		getLogger().info("Listeners Loaded.......");
	}
}
