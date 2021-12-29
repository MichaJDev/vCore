package vCore.Data.Builders.Users;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import vCore.Main;
import vCore.Data.Builders.Users.Interface.IUserBuilder;
import vCore.Dto.Types.Users.UserEditType;
import vCore.Dto.User.User;
import vCore.Dto.User.Interface.IUser;

public class UserBuilder implements IUserBuilder {

	Main main = Main.getInstance();

	public UserBuilder(Main _main) {
		main = _main;
	}

	@Override
	public List<UUID> getAllUserUUIDs() {
		List<UUID> users = new ArrayList<UUID>();
		for (File dir : getUsersFolder().listFiles()) {
			main.getLogger().info(dir.getName());
			users.add(UUID.fromString(dir.getName()));
		}
		return null;
	}

	@Override
	public Boolean userExist(UUID uuid) {
		File file = new File(getUserFolder(uuid), "player.yml");
		if (!file.exists())
			return false;
		return true;

	}

	@Override
	public void Startup() {
		createUsersFolder();
	}

	@Override
	public void Add(UUID uuid, String key, String value) {
		FileConfiguration cfg = getUserCfg(uuid);
		cfg.addDefault(key, value);
		saveUserFile(uuid);
		main.getLogger().info("Key: " + key + " with Value: " + value + " added to cfg of " + getUserName(uuid));
	}

	@Override
	public IUser get(UUID uuid) {
		IUser user = new User();
		FileConfiguration cfg = getUserCfg(uuid);
		user.setUUID(cfg.getString("UUID"));
		user.setName(cfg.getString("Name"));
		user.setBanned(cfg.getBoolean("Banned"));
		user.setBanner(cfg.getString("Banner"));
		user.setBanReason(cfg.getString("BanReason"));
		user.setWarns(cfg.getInt("Warns"));
		return user;
	}

	@Override
	public void create(Player p) {
		createUserFile(p.getUniqueId());
	}

	@Override
	public void edit(UUID uuid, UserEditType edit, String node) {
		FileConfiguration cfg = getUserCfg(uuid);
		switch (edit) {
		case NAME:
			cfg.set("Name", node);
			saveUserFile(uuid);
			break;
		case BANNED:
			cfg.set("Banned", Boolean.parseBoolean(node));
			saveUserFile(uuid);
			break;
		case BANNER:
			cfg.set("Banner", node);
			saveUserFile(uuid);
			break;
		case BANREASON:
			cfg.set("BanReason", node);
			saveUserFile(uuid);
			break;
		case IP:
			cfg.set("Ip", node);
			saveUserFile(uuid);
			break;
		case WARNS:
			cfg.set("Warns", Integer.parseInt(node));
			saveUserFile(uuid);
			break;
		default:
			main.getLogger().severe("WRONG EDITTYPE PLEASE CHECK CODE");
			break;

		}

	}

	@Override
	public void delete(UUID uuid) {
		File file = getUserFile(uuid);
		File dir = getUserFile(uuid);
		try {
			file.delete();
			dir.delete();
			main.getLogger().info("Userfolder and files deleted for: " + getUserName(uuid));
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	/*
	 * Folder Management
	 */

	// Check if user folder exists
	@Override
	public Boolean userFolderExist() {
		File dir = getUsersFolder();
		if (!dir.exists())
			return false;
		return true;
	}

	public IUser getUserByName(String name) {
		IUser user = new User();
		main.getLogger().info("Trying to get user through name: " + name);
		for (File i : getUsersFolder().listFiles()) {
			FileConfiguration cfg = getUserCfg(UUID.fromString(i.getName()));
			if (cfg.getString("Name") == name) {
				main.getLogger().info("User Found by name: " + name);
				user.setUUID(cfg.getString("UUID"));
				user.setName(cfg.getString("Name"));
				user.setBanned(cfg.getBoolean("Banned"));
				user.setBanner(cfg.getString("Banner"));
				user.setBanReason(cfg.getString("BanReason"));
				user.setWarns(cfg.getInt("Warns"));
			} else {
				main.getLogger().info("User could not be found by name: " + name);
			}
		}
		return user;
	}

	// Create Main Users Folder
	private void createUsersFolder() {
		File dir = new File(main.getDataFolder() + File.separator + "Users");
		if (!dir.exists()) {
			dir.mkdirs();
			main.getLogger().info("UserFolder Created.");
		}
	}

	// Get Main Users Folder
	private File getUsersFolder() {
		File dir = new File(main.getDataFolder() + File.separator + "Users");
		if (!dir.exists())
			createUsersFolder();
		return dir;
	}

	private void createUserFolder(UUID uuid) {
		File file = new File(getUsersFolder() + File.separator + uuid.toString());
		if (!file.exists()) {
			if (file.mkdirs()) {
				main.getLogger().info("Folder for " + main.getServer().getPlayer(uuid).getName() + " created.");
			}
		}
	}

	public File getUserFolder(UUID uuid) {
		File dir = new File(getUsersFolder() + File.separator + uuid.toString());
		if (!dir.exists())
			return null;
		return dir;
	}

	private void createUserFile(UUID uuid) {
		createUserFolder(uuid);
		File file = new File(getUserFolder(uuid), "player.yml");
		if (!file.exists()) {
			try {
				file.createNewFile();
				Player p = main.getServer().getPlayer(uuid);
				FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
				cfg.addDefault("UUID", p.getUniqueId().toString());
				cfg.addDefault("Name", p.getName());
				cfg.addDefault("Banned", false);
				cfg.addDefault("Banner", "");
				cfg.addDefault("BanReason", "");
				cfg.addDefault("Warns", 0);
				cfg.options().copyDefaults(true);
				try {
					cfg.save(getUserFile(uuid));
				} catch (IOException e) {
					e.printStackTrace();
				}
				main.getLogger().info("UserFile for" + main.getServer().getPlayer(uuid).getName() + " created.");
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	public IUser createUserObject(Player p) {
		IUser user = new User();
		user.setName(p.getName());
		user.setIP(p.getAddress().getHostName());
		user.setBanned(false);
		user.setBanner("");
		user.setBanReason("");
		user.setUUID(p.getUniqueId().toString());
		user.setWarns(0);
		return user;
	}

	public void saveUserFile(UUID uuid) {
		FileConfiguration cfg = getUserCfg(uuid);
		try {
			cfg.save(getUserFile(uuid));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private File getUserFile(UUID uuid) {
		File file = new File(getUserFolder(uuid), "player.yml");
		if (!file.exists())
			return null;
		return file;
	}

	private FileConfiguration getUserCfg(UUID uuid) {
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(getUserFile(uuid));
		return cfg;
	}

	private String getUserName(UUID uuid) {
		return main.getServer().getPlayer(uuid).getName();
	}

}
