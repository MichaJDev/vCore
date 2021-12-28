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

public class Main extends JavaPlugin {

	private static Main main;
	private IUserBuilder _user;
	private IConfigBuilder _cfg;

	public static Main getInstance() {
		return main;
	}

	@Override
	public void onEnable() {
		_user = new UserBuilder(this);
		_cfg = new ConfigBuilder(this);
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
				Database.connect();
				if (Database.isConnected() && !Database.exist("Users"))
					Database.createFirstSetup();
			}
		}
	}

	@Override
	public void onDisable() {
		saveConfig();
		getLogger().info("Saving all UserFiles");
		for (UUID u : _user.getAllUserUUIDs()) {
			IUser user = _user.get(u);
			getLogger().info("File for: " + user.getName() + " getting saved");
			_user.saveUserFile(u);
		}
		getLogger().info("Disconnecting database");
	}

	private void getCommands() {
		this.getCommand("info").setExecutor(new InfoCmd(this, _user));
	}

	private void getListeners() {
		getServer().getPluginManager().registerEvents(new LoginListener(this, _user), this);
		getLogger().info("Listeners Loaded.......");
	}
}
