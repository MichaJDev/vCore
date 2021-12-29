package vCore.Listeners.Login;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import vCore.Main;
import vCore.Data.Builders.Users.Interface.IUserBuilder;

public class LoginListener implements Listener {

	Main main = Main.getInstance();
	IUserBuilder users;

	public LoginListener(Main _main, IUserBuilder _user) {
		main = _main;
		users = _user;
	}

	@EventHandler
	public void onLogin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if (!users.userExist(p.getUniqueId())) {
			main.getLogger().info("User not found! Creating new files for" + p.getName());
			users.create(p);
		}
	}
}
