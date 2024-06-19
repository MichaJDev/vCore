package nl.vCore.Listeners;

import nl.vCore.Dto.User;
import nl.vCore.Data.Factories.MSSQL.MSSQLUserFactory;
import nl.vCore.Main;
import nl.vCore.Utils.DtoShaper;
import nl.vCore.Utils.MessageUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class UserListener implements Listener {


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        MessageUtils msg = new MessageUtils(Main.getInstance());
        User u = DtoShaper.userShaper(e.getPlayer());
        if(MSSQLUserFactory.doesUserExist(u)){
            msg.log("User found, Updating...");
            MSSQLUserFactory.update(u);
        }else{
            msg.log("User not found, Creating...");
            MSSQLUserFactory.create(u);
        }


    }
}
