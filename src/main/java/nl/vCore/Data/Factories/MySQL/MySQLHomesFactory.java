package nl.vCore.Data.Factories.MySQL;

import java.util.List;
import nl.vCore.Data.MySql.Homes.MySQLHomesHandler;
import nl.vCore.Dto.Home;
import nl.vCore.Dto.User;
import nl.vCore.Main;
import nl.vCore.Utils.MessageUtils;

public class MySQLHomesFactory {

    private static final MySQLHomesHandler sql = new MySQLHomesHandler(Main.getInstance());

    /**
     * Creates a new home in the database.
     *
     * @param h The Home object to be created and stored.
     */
    public static void create(Home h) {
        sql.create(h);
    }

    /**
     * Retrieves a home from the database based on its ID.
     *
     * @param h The Home object containing the ID to search for.
     * @return The Home object retrieved from the database.
     */
    public static Home read(Home h) {
        return sql.read(h.getId());
    }

    /**
     * Updates an existing home's information in the database.
     *
     * @param h The Home object with updated information to be stored.
     */
    public static void update(Home h) {
        sql.update(h);
    }

    /**
     * Deletes a home from the database.
     *
     * @param h The Home object to be deleted.
     */
    public static void delete(Home h) {
        sql.delete(h);
    }

    /**
     * Checks if a specific home exists in the database.
     *
     * @param h The Home object to check for existence.
     * @return true if the home exists, false otherwise.
     */
    public static boolean checkIfHomeExists(Home h) {
        return sql.checkIfHomeExist(h);
    }

    /**
     * Retrieves all homes associated with a specific user.
     *
     * @param u The User object for which to retrieve homes.
     * @return A List of Home objects associated with the given user.
     */
    public static List<Home> getListFromUser(User u) {
        return sql.getListFromUser(u);
    }

    /**
     * Retrieves all homes from the database.
     *
     * @return A List containing all Home objects stored in the database.
     */
    public static List<Home> getAll() {
        return sql.getAll();
    }

    /**
     * Initializes the database by creating the homes table if it doesn't exist.
     * This method should be called when the system starts for the first time.
     */
    public static void triggerFirstStart() {
        sql.createHomesTableIfNotExists();
    }
}
