package vCore.Listeners.Login;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import vCore.Main;
import vCore.Data.Builders.Users.Interface.IUserBuilder;
import vCore.Data.SQL.Interface.IDatabase;
import vCore.Dto.User.User;
import vCore.Dto.User.Interface.IUser;

public class LoginListener implements Listener {

	Main main = Main.getInstance();
	IUserBuilder users;
	IDatabase db;

	public LoginListener(Main _main, IUserBuilder _user, IDatabase _db) {
		main = _main;
		users = _user;
		db = _db;
	}

	@EventHandler
	public void onLogin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if (!main.getConfig().getBoolean("useMYSQL")) {
			IUser user = new User();
			user.setUUID(p.getUniqueId().toString());
			if (!users.userExist(user)) {
				main.getLogger().info("User not found! Creating new files for: " + p.getName());
				users.create(p);
			}
		} else {
			IUser user = new User();
			user.setName(p.getName());
			user.setIP(p.getAddress().getHostName());
			user.setUUID(p.getUniqueId().toString());
			user.setBanned(false);
			user.setBanner("");
			user.setBanReason("");
			user.setWarns(0);
			if (!db.userExists(user)) {
				main.getLogger().info("MYSQL: User not found! Creating new row for: " + p.getName());
				db.createNewUser(user);
			}
		}

	}
}
