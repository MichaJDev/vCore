package nl.vCore.Utils;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
import nl.vCore.Main;
import org.bukkit.entity.Player;

import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class MessageUtils {
    private Main main = Main.getInstance();
    public MessageUtils(Main _main){
        this.main = (_main);
    }

    public void log(String msg){
        main.getLogger().log(Level.INFO, msg);
    }

    public void warn(String msg){
        main.getLogger().log(Level.WARNING, msg);
    }
    public void severe(String msg){
        main.getLogger().log(Level.SEVERE, msg);
    }
    public void msg(Player p, String msg){
        p.sendMessage(colorize(msg));
    }

    private String colorize(String message) {
        Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            String hexCode = message.substring(matcher.start(), matcher.end());
            String replaceSharp = hexCode.replace('#', 'x');

            char[] ch = replaceSharp.toCharArray();
            StringBuilder builder = new StringBuilder("");
            for (char c : ch) {
                builder.append("&").append(c);
            }

            message = message.replace(hexCode, builder.toString());
            matcher = pattern.matcher(message);
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }

}
