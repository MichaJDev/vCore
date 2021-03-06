package vCore.Commands.Info;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
		Player p = (Player) s;
		if (c.getName().equalsIgnoreCase("info")) {
			p.sendMessage(msg("&7# &6vCore &7#&f----------------------&7#"));
			p.sendMessage(msg("&7# &fMade by&7: &6xFlakesID aka &bVyx  "));
			p.sendMessage(msg("&7# &fVersion: " + main.getDescription().getVersion()));
			p.sendMessage(msg("&7# &fAuthors: " + main.getDescription().getAuthors()));
			p.sendMessage(msg("&7# &6vCore &7#&f----------------------&7#"));
			return false;
		}

		return false;
	}

	public String msg(String msg) {
		return ChatColor.translateAlternateColorCodes('&', msg);
	}

}
