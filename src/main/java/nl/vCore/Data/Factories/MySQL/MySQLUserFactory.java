package nl.vCore.Data.Factories.MySQL;

import nl.vCore.Data.MySql.Users.MySQLUserHandler;
import nl.vCore.Dto.User;
import nl.vCore.Main;

import java.util.List;

public class MySQLUserFactory {

    private static final MySQLUserHandler uh = new MySQLUserHandler(Main.getInstance());

    /**
     * Initializes the database by creating the user table if it doesn't exist.
     * This method should be called when the system starts for the first time.
     */
    public static void triggerFirstStart() {
        uh.createUserTableIfNotExist();
    }

    /**
     * Creates a new user in the database.
     *
     * @param u The User object to be created and stored.
     */
    public static void create(User u) {
        uh.create(u);
    }

    /**
     * Retrieves a user from the database based on their ID.
     *
     * @param u The User object containing the ID to search for.
     * @return The User object retrieved from the database.
     */
    public static User read(User u) {
        return uh.read(u.getId());
    }

    /**
     * Updates an existing user's information in the database.
     *
     * @param u The User object with updated information to be stored.
     */
    public static void update(User u) {
        uh.update(u);
    }

    /**
     * Deletes a user from the database based on their ID.
     *
     * @param u The User object to be deleted.
     */
    public static void delete(User u) {
        uh.delete(u.getId());
    }
    /**
     * Retrieves all users from the database.
     *
     * @return A List containing all User objects stored in the database.
     */
    public static List<User> getAll() {
        return uh.getAll();
    }
}
