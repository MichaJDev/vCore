package nl.vCore.Data.Factories.MSSQL;


import nl.vCore.Data.MSSQL.Homes.MSSQLHomesHandler;
import nl.vCore.Dto.Home;
import nl.vCore.Dto.User;
import nl.vCore.Main;
import nl.vCore.Utils.DtoShaper;
import nl.vCore.Utils.MessageUtils;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class MSSQLHomesFactory {

    private static final MSSQLHomesHandler sqlHandler = new MSSQLHomesHandler(Main.getInstance());
    private static final MessageUtils msg = new MessageUtils(Main.getInstance());
    /**
     * Creates the homes table in the MSSQL database if it doesn't exist.
     * Logs the process and any errors that occur.
     */
    public static void createTable() {
        try {
            msg.log("Creating new Homes Table in MSSQL DB...");
            sqlHandler.createHomesTableIfNotExists();
        } catch (SQLException e) {
            msg.severe(e.getMessage());
        }
    }

    /**
     * Creates a new home entry in the database.
     *
     * @param h The Home object to be created and stored.
     */
    public static void create(Home h) {
        sqlHandler.create(h);
    }

    /**
     * Updates an existing home entry's information in the database.
     *
     * @param h The Home object with updated information to be stored.
     */
    public static void update(Home h) {
        sqlHandler.update(h);
    }

    /**
     * Deletes a home entry from the database.
     *
     * @param h The Home object to be deleted.
     */
    public static void delete(Home h) {
        sqlHandler.delete(h);
    }

    /**
     * Retrieves a specific home entry from the database.
     *
     * @param p The Player object associated with the home.
     * @param name The name of the home.
     * @return The Home object retrieved from the database.
     */
    public static Home read(Player p, String name) {
        User u = DtoShaper.userShaper(p);
        Home h = new Home();
        h.setOwner(u);
        h.setName(name);
        return sqlHandler.read(h);
    }

    /**
     * Checks if a specific home exists in the database.
     *
     * @param id The UUID of the home to check.
     * @return true if the home exists, false otherwise.
     */
    public static boolean checkIfHomeExist(UUID id) {
        Home h = new Home();
        h.setId(id);
        return sqlHandler.checkIfHomeExist(h);
    }

    /**
     * Retrieves all home entries associated with a specific player.
     *
     * @param p The Player object for which to retrieve homes.
     * @return A List of Home objects associated with the given player.
     */
    public static List<Home> getHomesFromUser(Player p) {
        return sqlHandler.getHomesFromUserUuid(p.getUniqueId().toString());
    }

    /**
     * Retrieves all home entries from the database.
     *
     * @return A List containing all Home objects stored in the database.
     */
    public static List<Home> getAll() {
        return sqlHandler.getAll();
    }
}
