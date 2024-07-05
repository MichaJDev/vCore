package nl.vCore.Data.Factories.MySQL;

import nl.vCore.Data.MySql.Bans.MySQLBanHandler;
import nl.vCore.Dto.Ban;
import nl.vCore.Main;

public class MySQLBansFactory {

    private static MySQLBanHandler bh = new MySQLBanHandler(Main.getInstance());

    /**
     * Creates a new ban entry in the database.
     *
     * @param b The Ban object to be created and stored.
     */
    private static void create(Ban b) {
        bh.create(b);
    }

    /**
     * Retrieves a ban entry from the database based on its ID.
     *
     * @param b The Ban object containing the ID to search for.
     * @return The Ban object retrieved from the database.
     */
    private static Ban read(Ban b) {
        return bh.read(b.getId());
    }

    /**
     * Updates an existing ban entry's information in the database.
     *
     * @param b The Ban object with updated information to be stored.
     */
    private static void update(Ban b) {
        bh.update(b);
    }

    /**
     * Deletes a ban entry from the database.
     *
     * @param b The Ban object to be deleted.
     */
    private static void delete(Ban b) {
        bh.delete(b);
    }

    /**
     * Initializes the database by creating the bans table if it doesn't exist.
     * This method should be called when the system starts for the first time.
     */
    private static void triggerFirstStart() {
        bh.createBansTableIfNotExists();
    }

}
