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

	/**
	 * Returns list of all UUID's so you an retrieve all lookups
	 * 
	 * @return users List of all User UUID's
	 */

	@Override
	public List<UUID> getAllUserUUIDs() {
		List<UUID> users = new ArrayList<UUID>();
		for (File dir : getUsersFolder().listFiles()) {
			users.add(UUID.fromString(dir.getName()));
		}
		return users;
	}

	/**
	 * Returns a boolean if a users exists
	 * 
	 * @param uuid Unique user id of a player
	 * @return boolean true or false if exists
	 */
	@Override
	public Boolean userExist(IUser user) {
		File file = new File(getUserFolder(user), "player.yml");
		if (!file.exists())
			return false;
		return true;

	}

	/**
	 * Sets up all dictionaries up used to store flatfile users
	 * 
	 */

	@Override
	public void Startup() {
		main.getLogger().info("Creating \\Users\\.......");
		createUsersFolder();
	}

	/**
	 * Adds a default node to the user flatfile
	 * 
	 * @param user IUser object filled with uuid
	 */
	@Override
	public void Add(IUser user, String key, String value) {
		FileConfiguration cfg = getUserCfg(user);
		cfg.addDefault(key, value);
		saveUserFile(user);
		main.getLogger().info("Key: " + key + " with Value: " + value + " added to cfg of " + getUserName(user));
	}

	/**
	 * Retrieves an IUser object filled with user data from a uuid
	 * 
	 * @param uuid Unique User ID of the user
	 * @return user IUser object filled with user data
	 */
	@Override
	public IUser get(UUID uuid) {
		IUser user = new User();
		user.setUUID(uuid.toString());
		FileConfiguration cfg = getUserCfg(user);
		user.setUUID(cfg.getString("UUID"));
		user.setName(cfg.getString("Name"));
		user.setBanned(cfg.getBoolean("Banned"));
		user.setBanner(cfg.getString("Banner"));
		user.setBanReason(cfg.getString("BanReason"));
		user.setWarns(cfg.getInt("Warns"));
		return user;
	}

	/**
	 * Creates a player file
	 * 
	 * @param user IUser object filled with player info
	 */

	@Override
	public void create(Player p) {
		IUser user = new User();
		user.setUUID(p.getUniqueId().toString());
		createUserFile(user);
	}
	
	/**
	 * Edit one of the values of the user file nodes
	 * 
	 * @param user IUser object filled with users UUID
	 * @param edit UserEditType NAME/BANNED/BANNER/BANREASON/IP/WARNS
	 * @param node value of edit for related EditType
	 */

	@Override
	public void edit(IUser user, UserEditType edit, String node) {
		FileConfiguration cfg = getUserCfg(user);
		switch (edit) {
		case NAME:
			cfg.set("Name", node);
			saveUserFile(user);
			break;
		case BANNED:
			cfg.set("Banned", Boolean.parseBoolean(node));
			saveUserFile(user);
			break;
		case BANNER:
			cfg.set("Banner", node);
			saveUserFile(user);
			break;
		case BANREASON:
			cfg.set("BanReason", node);
			saveUserFile(user);
			break;
		case IP:
			cfg.set("Ip", node);
			saveUserFile(user);
			break;
		case WARNS:
			cfg.set("Warns", Integer.parseInt(node));
			saveUserFile(user);
			break;
		default:
			main.getLogger().severe("WRONG EDITTYPE PLEASE CHECK CODE");
			break;

		}

	}

	@Override
	public void delete(IUser user) {
		File file = getUserFile(user);
		File dir = getUserFile(user);
		try {
			file.delete();
			dir.delete();
			main.getLogger().info("Userfolder and files deleted for: " + getUserName(user));
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	/**
	 * Returns a true or false if folder for users is created
	 * 
	 * @return boolean returns true or false
	 */
	@Override
	public Boolean userFolderExist() {
		File dir = getUsersFolder();
		if (!dir.exists())
			return false;
		return true;
	}

	/**
	 * Returns user object with lookup of name
	 * 
	 * @param name name of player 
	 * @return user IUser object filled with user data
	 */
	public IUser getUserByName(String name) {
		IUser user = new User();
		main.getLogger().info("Trying to get user through name: " + name);
		for (File i : getUsersFolder().listFiles()) {
			user.setUUID(i.getName());
			FileConfiguration cfg = getUserCfg(user);
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

	private void createUsersFolder() {
		File dir = new File(main.getDataFolder() + File.separator + "Users");
		if (!dir.exists()) {
			dir.mkdirs();
			main.getLogger().info("UserFolder Created.");
		}
	}

	private File getUsersFolder() {
		File dir = new File(main.getDataFolder() + File.separator + "Users");
		if (!dir.exists())
			createUsersFolder();
		return dir;
	}

	private void createUserFolder(IUser user) {
		File file = new File(getUsersFolder() + File.separator + user.getUUID().toString());
		if (!file.exists()) {
			if (file.mkdirs()) {
				main.getLogger()
						.info("Folder for " + main.getServer().getPlayer(user.getUUID()).getName() + " created.");
			}
		}
	}
	
	/**
	 * Returns user specific directory
	 * @param user IUser object filled with uuid
	 * @return dir returns user specfic folder
	 */
	public File getUserFolder(IUser user) {
		File dir = new File(getUsersFolder() + File.separator + user.getUUID().toString());
		if (!dir.exists())
			return null;
		return dir;
	}

	private void createUserFile(IUser user) {
		createUserFolder(user);
		File file = new File(getUserFolder(user), "player.yml");
		if (!file.exists()) {
			try {
				file.createNewFile();
				Player p = main.getServer().getPlayer(user.getUUID());
				FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
				cfg.addDefault("UUID", p.getUniqueId().toString());
				cfg.addDefault("Name", p.getName());
				cfg.addDefault("Banned", false);
				cfg.addDefault("Banner", "");
				cfg.addDefault("BanReason", "");
				cfg.addDefault("Warns", 0);
				cfg.options().copyDefaults(true);
				try {
					cfg.save(getUserFile(user));
				} catch (IOException e) {
					e.printStackTrace();
				}
				main.getLogger()
						.info("UserFile for" + main.getServer().getPlayer(user.getUUID()).getName() + " created.");
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * Create User Object from Player
	 * @param p Player
	 * @returns IUser User object filled with userObject
	 */
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

	/**
	 *Saves user file after editing it. 
	 * @param user IUser object filled with userdata;
	 */
	public void saveUserFile(IUser user) {
		FileConfiguration cfg = getUserCfg(user);
		try {
			cfg.save(getUserFile(user));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Returns the flat user file.
	 * @param user IUser object filled with user data.
	 * @return file User File stored in folder
	 */
	public File getUserFile(IUser user) {
		File file = new File(getUserFolder(user), "player.yml");
		if (!file.exists())
			return null;
		return file;
	}
	
	/**
	 * Returns FileConfiguration for a user.
	 * @param user IUser object file with uuid
	 * @return cfg FileConfiguration of user
	 */
	public FileConfiguration getUserCfg(IUser user) {
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(getUserFile(user));
		return cfg;
	}

	private String getUserName(IUser user) {
		return main.getServer().getPlayer(user.getUUID()).getName();
	}

}
