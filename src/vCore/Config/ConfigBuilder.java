package vCore.Config;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;

import vCore.Main;
import vCore.Config.Interface.IConfigBuilder;
import vCore.Dto.Types.Configs.ConfigEditType;

public class ConfigBuilder implements IConfigBuilder {

	Main main = Main.getInstance();

	public ConfigBuilder(Main _main) {
		main = _main;
	}

	@Override
	public void createConfig() {
		main.saveDefaultConfig();
		FileConfiguration cfg = main.getConfig();
		cfg.addDefault("useMYSQL", false);
		cfg.addDefault("SQL.host", "localhost");
		cfg.addDefault("SQL.port", 3306);
		cfg.addDefault("SQL.database", "VCOREDB");
		cfg.addDefault("SQL.username", "root");
		cfg.addDefault("SQL.password", "");
		cfg.options().copyDefaults(true);
		main.saveConfig();
	}

	@Override
	public void editConfig(ConfigEditType type, String node) {
		FileConfiguration cfg = main.getConfig();
		switch (type) {
		case MYSQL:
			if (!cfg.getBoolean("Mysql"))
				cfg.set("Mysql", true);
			break;
		case FLAT:
			if (cfg.getBoolean("Mysql"))
				cfg.set("Mysql", false);
			break;
		default:
			break;
		}

	}

	@Override
	public Boolean configExists() {
		File file = new File(main.getDataFolder(), "config.yml");
		if (!file.exists())
			return false;
		return true;
	}

	@Override
	public FileConfiguration getConfig() {
		return main.getConfig();
	}

	@Override
	public File getMainFolder() {
		return main.getDataFolder();
	}

}
