package vCore.Commands.Info;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.md_5.bungee.api.ChatColor;
import vCore.Main;
import vCore.Data.Builders.Users.Interface.IUserBuilder;

public class InfoCmd implements CommandExecutor {

	Main main = Main.getInstance();
	IUserBuilder user;

	public InfoCmd(Main _main, IUserBuilder _user) {
		main = _main;
		user = _user;
	}

	@Override
	public boolean onCommand(CommandSender s, Command c, String l, String[] args) {
		if (c.getName().equalsIgnoreCase("info")) {
			msg("&7# &6vCore &7#&f----------------------&7#");
			msg("&7# &fMade by&7: &6xFlakesID aka &bVyx  ");
			msg("&7# &fVersion: " + main.getDescription().getVersion());
			msg("&7# &fAuthors: " + main.getDescription().getAuthors());
			msg("&7# &6vCore &7#&f----------------------&7#");
			return false;
		}

		return false;
	}

	public String msg(String msg) {
		return ChatColor.translateAlternateColorCodes('&', msg);
	}

}
