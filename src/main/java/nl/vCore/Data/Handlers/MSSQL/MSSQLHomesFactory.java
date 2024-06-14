package nl.vCore.Data.Handlers.MSSQL;


import nl.vCore.Data.MSSQL.Homes.MSSQLHomesHandler;
import nl.vCore.Dto.Home;
import nl.vCore.Dto.User;
import nl.vCore.Main;
import nl.vCore.Utils.DtoShaper;
import nl.vCore.Utils.MessageUtils;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.List;

public class MSSQLHomesFactory {

    private static final MSSQLHomesHandler sqlHandler = new MSSQLHomesHandler(Main.getInstance());
    private static final MessageUtils msg = new MessageUtils(Main.getInstance());

    public static void createTable() {
        try {
            msg.log("Creating new Homes Table in MSSQL DB...");
            sqlHandler.createHomesTableIfNotExists();
        } catch (SQLException e) {
            msg.severe(e.getMessage());
        }
    }

    public static void create(Home h) {
        sqlHandler.create(h);
    }

    public static void update(Home h) {
        sqlHandler.update(h);
    }

    public static void delete(Home h) {
        sqlHandler.delete(h);
    }

    public static Home read(Player p, String name) {
        User u = DtoShaper.userShaper(p);
        Home h = new Home();
        h.setOwner(u);
        h.setName(name);
        return sqlHandler.read(h);
    }

    public static List<Home> getHomesFromUser(Player p) {
        return sqlHandler.getHomesFromUserUuid(p.getUniqueId().toString());
    }

    public static List<Home> getAll() {
        return sqlHandler.getAll();
    }
}
