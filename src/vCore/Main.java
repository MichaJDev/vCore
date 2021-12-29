package vCore;

import java.util.UUID;

import org.bukkit.plugin.java.JavaPlugin;

import vCore.Commands.Info.InfoCmd;
import vCore.Config.ConfigBuilder;
import vCore.Config.Interface.IConfigBuilder;
import vCore.Data.Builders.Users.UserBuilder;
import vCore.Data.Builders.Users.Interface.IUserBuilder;
import vCore.Dto.User.Interface.IUser;
import vCore.Listeners.Login.LoginListener;
import vCore.Sql.Database;
import vCore.Sql.interfaces.IDataBase;

public class Main extends JavaPlugin {

	private static Main main;
	private IUserBuilder _user;
	private IConfigBuilder _cfg;
	private IDataBase _db;

	public static Main getInstance() {
		return main;
	}

	@Override
	public void onEnable() {
		_user = new UserBuilder(main);
		_cfg = new ConfigBuilder(main);
		_db = new Database(main, _cfg);
		getLogger().info("Loading commands.......");
		getCommands();
		getLogger().info("Loading Listeners........");
		getListeners();
		getLogger().info("Checking directories.......");
		if (!_user.userFolderExist()) {
			_user.Startup();
		}
		getLogger().info("Creating Config");
		if (!_cfg.configExists()) {
			_cfg.createConfig();
		} else {
			if (_cfg.getConfig().getBoolean("useMYSQL")) {
				getLogger().info("Connecting to  database");
				_db.connect();
				if (_db.isConnected() && !_db.exist("Users"))
					_db.createFirstSetup();
			}
		}
	}

	@Override
	public void onDisable() {
		saveConfig();
		getLogger().info("Saving all UserFiles");
		if (_user.getAllUserUUIDs() != null) {
			for (UUID u : _user.getAllUserUUIDs()) {
				IUser user = _user.get(u);
				getLogger().info("File for: " + user.getName() + " getting saved");
				_user.saveUserFile(u);
			}

		}
		getLogger().info("Disconnecting database");
		_db.disconnect();
	}

	private void getCommands() {
		this.getCommand("info").setExecutor(new InfoCmd(this, _user));
	}

	private void getListeners() {
		getServer().getPluginManager().registerEvents(new LoginListener(this, _user, _db), this);
		getLogger().info("Listeners Loaded.......");
	}
}
