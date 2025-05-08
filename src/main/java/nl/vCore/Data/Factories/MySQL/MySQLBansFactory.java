package nl.vCore.Data.Factories.MySQL;

import nl.vCore.Data.MySql.Bans.MySQLBanHandler;
import nl.vCore.Dto.Ban;
import nl.vCore.Main;
import nl.vCore.Utils.MessageUtils;

import java.util.ArrayList;
import java.util.List;

public class MySQLBansFactory {

    private static final MySQLBanHandler bh = new MySQLBanHandler(Main.getInstance());
    private static final MessageUtils msgUtils = new MessageUtils(Main.getInstance());

    /**
     * Creates the bans table in the database if it doesn't exist.
     * This method should be called during initial setup.
     */
    public static void createTable() {
        try {
            msgUtils.log("Creating bans table in MySQL database...");
            bh.createBansTableIfNotExists();
            msgUtils.log("Bans table created successfully!");
        } catch (Exception e) {
            msgUtils.severe("Failed to create bans table: " + e.getMessage());
        }
    }

    /**
     * Creates a new ban entry in the database.
     *
     * @param b The Ban object to be created and stored.
     */
    public static void create(Ban b) {
        bh.create(b);
    }

    /**
     * Retrieves a ban entry from the database based on its ID.
     *
     * @param b The Ban object containing the ID to search for.
     * @return The Ban object retrieved from the database.
     */
    public static Ban read(Ban b) {
        return bh.read(b.getId());
    }

    /**
     * Updates an existing ban entry's information in the database.
     *
     * @param b The Ban object with updated information to be stored.
     */
    public static void update(Ban b) {
        bh.update(b);
    }

    /**
     * Deletes a ban entry from the database.
     *
     * @param b The Ban object to be deleted.
     */
    public static void delete(Ban b) {
        bh.delete(b);
    }

    /**
     * Initializes the database by creating the bans table if it doesn't exist.
     * This method should be called when the system starts for the first time.
     */
    public static void triggerFirstStart() {
        bh.createBansTableIfNotExists();
    }
    
    /**
     * Retrieves all bans from the database.
     *
     * @return A List containing all Ban objects stored in the database.
     */
    public static List<Ban> getAll() {
        // Stub method - to be implemented
        return new ArrayList<>();
    }
}
