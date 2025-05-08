package nl.vCore.Data.Factories.MSSQL;

import com.google.common.collect.ImmutableMultiset;
import nl.vCore.Data.MSSQL.Users.MSSQLUsersHandler;
import nl.vCore.Dto.User;
import nl.vCore.Main;
import nl.vCore.Utils.DtoShaper;
import nl.vCore.Utils.MessageUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MSSQLUserFactory {
    private static final MSSQLUsersHandler sqlHandler = new MSSQLUsersHandler(Main.getInstance());
    private static final MessageUtils msg = new MessageUtils(Main.getInstance());

    public static void createTable() {
        try {
            msg.log("Creating new User Table in MSSQL DB...");
            sqlHandler.createUsersTableIfNotExists();
        } catch (SQLException e) {
            msg.severe("Failed to create users table: " + e.getMessage());
        }
    }

    public static void create(User u) {
        sqlHandler.create(u);
    }

    public static void update(User u) {
        sqlHandler.update(u);
    }

    public static void delete(User u) {
        sqlHandler.delete(u);
    }

    public static User read(Player p) {
        return sqlHandler.read(p.getUniqueId().toString());
    }

    public static User readFromUUID(String uuid) {
        try {
            User u = new User();
            u.setId(UUID.fromString(uuid));
            if (doesUserExist(u)) {
                return sqlHandler.read(uuid);
            }
            return null;
        } catch (IllegalArgumentException e) {
            msg.severe("Invalid UUID format: " + uuid);
            return null;
        }
    }

    public static boolean doesUserExist(User u) {
        return sqlHandler.checkIfUserExists(u);
    }

    public static void elevateDB() {
        List<User> localUList = new ArrayList<>();
        msg.log("Collecting offline players data...");
        
        for (OfflinePlayer p : Main.getInstance().getServer().getOfflinePlayers()) {
            User user = DtoShaper.userShaper(p);
            if (user != null) {
                localUList.add(user);
            }
        }
        
        msg.log("Syncing " + localUList.size() + " players with database...");
        for (User u : localUList) {
            if (sqlHandler.checkIfUserExists(u)) {
                sqlHandler.update(u);
            } else {
                sqlHandler.create(u);
            }
        }
        msg.log("Database elevated...");
    }

    public static boolean compareDB() {
        List<User> localUList = new ArrayList<>();
        List<User> dbUList = sqlHandler.getAll();
        
        msg.log("Comparing database ("+dbUList.size()+" records) with offline players...");
        for (OfflinePlayer p : Main.getInstance().getServer().getOfflinePlayers()) {
            User user = DtoShaper.userShaper(p);
            if (user != null) {
                localUList.add(user);
            }
        }
        
        return equalsIgnoreOrder(dbUList, localUList);
    }

    private static <T> boolean equalsIgnoreOrder(List<T> a, List<T> b) {
        return ImmutableMultiset.copyOf(a).equals(ImmutableMultiset.copyOf(b));
    }
}
