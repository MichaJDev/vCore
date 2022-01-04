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

	public void edit(IUser user, UserEditType edit, String node);

	public void delete(IUser user);

	public Boolean userExist(IUser user);

	public Boolean userFolderExist();

	public void Startup();

	public void Add(IUser user, String key, String value);

	public List<UUID> getAllUserUUIDs();

	public void saveUserFile(IUser user);

	public IUser createUserObject(Player p);

	public File getUserFolder(IUser user);

	public FileConfiguration getUserCfg(IUser user);

	public File getUserFile(IUser user);
}
