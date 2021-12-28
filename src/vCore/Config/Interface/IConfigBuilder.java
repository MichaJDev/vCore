package vCore.Config.Interface;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;

import vCore.Dto.Types.Configs.ConfigEditType;

public interface IConfigBuilder {

	public void createConfig();

	public void editConfig(ConfigEditType type, String node);

	public Boolean configExists();

	public FileConfiguration getConfig();
	
	public File getMainFolder();
}
