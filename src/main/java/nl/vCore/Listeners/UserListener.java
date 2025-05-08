package nl.vCore.Listeners;

import nl.vCore.Config.ConfigHandler;
import nl.vCore.Dto.User;
import nl.vCore.Data.Factories.MSSQL.MSSQLUserFactory;
import nl.vCore.Main;
import nl.vCore.Utils.DtoShaper;
import nl.vCore.Utils.MessageUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class UserListener implements Listener {

    private final MessageUtils msg = new MessageUtils(Main.getInstance());
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        try {
            Player player = e.getPlayer();
            if (player == null) {
                msg.warn("Received join event with null player");
                return;
            }
            
            ConfigHandler cfg = new ConfigHandler(Main.getInstance());
            
            // Only process player data if MSSQL is enabled
            if (cfg.isMSSQL()) {
                User u = DtoShaper.userShaper(player);
                if (u == null) {
                    msg.warn("Failed to create User DTO for player: " + player.getName());
                    return;
                }
                
                // Update or create the user
                if (MSSQLUserFactory.doesUserExist(u)) {
                    msg.log("User found, updating database record for: " + player.getName());
                    MSSQLUserFactory.update(u);
                } else {
                    msg.log("User not found, creating new database record for: " + player.getName());
                    MSSQLUserFactory.create(u);
                }
            }
        } catch (Exception ex) {
            msg.severe("Error processing player join event: " + ex.getMessage());
        }
    }
}
