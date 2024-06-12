package nl.vCore.Utils;

import nl.vCore.Dto.Ban;
import nl.vCore.Dto.User;
import org.bukkit.entity.Player;

import java.time.format.DateTimeFormatter;

public class DtoShaper {

    public static User userShaper(Player p){
        User u = new User();
        u.setIP(p.getAddress().getAddress().toString());
        u.setName(p.getName());
        u.setDisplayName(p.getDisplayName());
        u.setId(p.getUniqueId());
        return u;
    }
    public static Ban banShaper(Player banned, String reason, Player banner){
        Ban ban = new Ban();
        ban.setBannedUser(userShaper(banned));
        ban.setBanner(userShaper(banner));
        ban.setReason(reason);
        ban.setDate(DateTimeFormatter.BASIC_ISO_DATE.toString());
        return ban;
    }
}
