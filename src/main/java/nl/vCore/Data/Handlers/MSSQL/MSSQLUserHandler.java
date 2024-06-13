package nl.vCore.Data.Handlers.MSSQL;

import com.google.common.collect.ImmutableMultiset;
import nl.vCore.Data.MSSQL.MSSQLHandler;
import nl.vCore.Dto.User;
import nl.vCore.Main;
import nl.vCore.Utils.DtoShaper;
import nl.vCore.Utils.MessageUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MSSQLUserHandler {
    private static final MSSQLHandler sqlHandler = new MSSQLHandler(Main.getInstance());

    public static void createTable() {
        try {
            sqlHandler.createPlayerTableIfNotExists();
        } catch (SQLException e) {
            MessageUtils msg = new MessageUtils(Main.getInstance());
            msg.log(e.getMessage());
        }
    }

    public static void create(User u) {
        sqlHandler.createPlayer(u);
    }

    public static void update(User u) {
        sqlHandler.updatePlayer(u);
    }

    public static void delete(User u) {
        sqlHandler.deletePlayer(u);
    }

    public static boolean doesUserExist(User u) {
        return sqlHandler.checkIfUserExists(u);
    }

    public static void elevateDB() {
        List<User> localUList = new ArrayList<>();
        for (OfflinePlayer p : Main.getInstance().getServer().getOfflinePlayers()) {
            localUList.add(DtoShaper.userShaper((Player) p));
        }
        for (User u : localUList) {
            if (sqlHandler.checkIfUserExists(u)) {
                sqlHandler.updatePlayer(u);
            } else {
                sqlHandler.createPlayer(u);
            }
        }
        MessageUtils msgUtils = new MessageUtils(Main.getInstance());
        msgUtils.log("Database elevated...");
    }

    public static boolean compareDB() {
        boolean isEqual = false;
        List<User> localUList = new ArrayList<>();
        List<User> dbUList = MSSQLUserHandler.sqlHandler.getAllPlayers();

        return equalsIgnoreOrder(dbUList, localUList);
    }

    private static <T> boolean equalsIgnoreOrder(List<T> a, List<T> b) {
        return ImmutableMultiset.copyOf(a).equals(ImmutableMultiset.copyOf(b));
    }


}
