package nl.vCore.Data.Factories.MySQL;

import java.util.List;
import nl.vCore.Data.MySql.Homes.MySQLHomesHandler;
import nl.vCore.Dto.Home;
import nl.vCore.Dto.User;
import nl.vCore.Main;
import nl.vCore.Utils.MessageUtils;

public class MySQLHomesFactory {

    private static final MySQLHomesHandler hh = new MySQLHomesHandler(Main.getInstance());
    private static final MessageUtils msgUtils = new MessageUtils(Main.getInstance());

    /**
     * Creates the homes table in the database if it doesn't exist.
     * This method should be called during initial setup.
     */
    public static void createTable() {
        try {
            msgUtils.log("Creating homes table in MySQL database...");
            hh.createHomesTableIfNotExists();
            msgUtils.log("Homes table created successfully!");
        } catch (Exception e) {
            msgUtils.severe("Failed to create homes table: " + e.getMessage());
        }
    }

    /**
     * Creates a new home in the database.
     *
     * @param h The Home object to be created and stored.
     */
    public static void create(Home h) {
        hh.create(h);
    }

    /**
     * Retrieves a home from the database based on its ID.
     *
     * @param h The Home object containing the ID to search for.
     * @return The Home object retrieved from the database.
     */
    public static Home read(Home h) {
        return hh.read(h.getId());
    }

    /**
     * Retrieves all homes belonging to a specific user.
     *
     * @param u The User whose homes to retrieve.
     * @return A List of Home objects belonging to the user.
     */
    public static List<Home> getListFromUser(User u) {
        return hh.getListFromUser(u);
    }

    /**
     * Updates an existing home in the database.
     *
     * @param h The Home object to be updated.
     */
    public static void update(Home h) {
        hh.update(h);
    }

    /**
     * Deletes a home from the database.
     *
     * @param h The Home object to be deleted.
     */
    public static void delete(Home h) {
        hh.delete(h);
    }

    /**
     * Checks if a home exists in the database.
     *
     * @param h The Home object to check for.
     * @return true if the home exists, false otherwise.
     */
    public static boolean checkIfHomeExists(Home h) {
        return hh.checkIfHomeExist(h);
    }

    /**
     * Retrieves all homes from the database.
     *
     * @return A List of all Home objects stored in the database.
     */
    public static List<Home> getAll() {
        return hh.getAll();
    }

    /**
     * Initializes the database by creating the homes table if it doesn't exist.
     * This method should be called when the system starts for the first time.
     */
    public static void triggerFirstStart() {
        hh.createHomesTableIfNotExists();
    }
}
