package vCore.Data.Builders.Users.Interface;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import vCore.Dto.Types.Users.UserEditType;
import vCore.Dto.User.Interface.IUser;

public interface IUserBuilder {

	public IUser get(UUID uuid);

	public void create(Player p);

	public void edit(UUID uuid, UserEditType edit, String node);

	public void delete(UUID uuid);

	public Boolean userExist(UUID uuid);

	public Boolean userFolderExist();

	public void Startup();

	public void Add(UUID uuid, String key, String value);

	public List<UUID> getAllUserUUIDs();

	public void saveUserFile(UUID uuid);

	public IUser createUserObject(Player p);

	public File getUserFolder(UUID uuid);

	public FileConfiguration getUserCfg(UUID uuid);

	public File getUserFile(UUID uuid);
}
