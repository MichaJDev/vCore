package nl.vCore.Data.Factories.MSSQL;

import java.util.List;
import nl.vCore.Data.MSSQL.Bans.MSSQLBansHandler;
import nl.vCore.Dto.Ban;
import nl.vCore.Dto.User;
import nl.vCore.Main;
import nl.vCore.Utils.MessageUtils;

import java.sql.SQLException;

public class MSSQLBanFactory {

    private static final MSSQLBansHandler sqlHandler = new MSSQLBansHandler(Main.getInstance());
    private static final MessageUtils msg = new MessageUtils(Main.getInstance());
    /**
     * Creates the bans table in the MSSQL database if it doesn't exist.
     * Logs the process and any errors that occur.
     */
    public static void createTable() {
        try {
            msg.log("Creating new Bans Table in MSSQL DB...");
            sqlHandler.createBanTableIfNotExists();
        } catch(SQLException e) {
            msg.severe(e.getMessage());
        }
    }

    /**
     * Creates a new ban entry in the database.
     * Logs the creation process.
     *
     * @param b The Ban object to be created and stored.
     */
    public static void create(Ban b) {
        msg.log("Creating Ban for " + Main.getInstance().getServer().getPlayer(b.getBannedUser().getId()) + "...");
        sqlHandler.createBan(b);
    }

    /**
     * Updates an existing ban entry's information in the database.
     *
     * @param b The Ban object with updated information to be stored.
     */
    public static void update(Ban b) {
        sqlHandler.updateBan(b);
    }

    /**
     * Deletes a ban entry from the database.
     *
     * @param b The Ban object to be deleted.
     */
    public static void delete(Ban b) {
        sqlHandler.deleteBan(b);
    }

    /**
     * Retrieves a specific ban entry from the database.
     *
     * @param u The User object associated with the ban.
     * @param id The ID of the ban entry.
     * @return The Ban object retrieved from the database.
     */
    public static Ban read(User u, int id) {
        return sqlHandler.read(u, id);
    }

    /**
     * Retrieves all ban entries from the database.
     *
     * @return A List containing all Ban objects stored in the database.
     */
    public static List<Ban> getAllBans() {
        return sqlHandler.getAllBans();
    }

    /**
     * Retrieves all ban entries associated with a specific user.
     *
     * @param u The User object for which to retrieve bans.
     * @return A List of Ban objects associated with the given user.
     */
    public static List<Ban> getAllBansFromUser(User u) {
        return sqlHandler.getAllBansFromUser(u);
    }
}
