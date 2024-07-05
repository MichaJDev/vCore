package nl.vCore.Data.Factories.MySQL;

import nl.vCore.Data.MySql.Warns.MySQLWarnsHandler;
import nl.vCore.Dto.User;
import nl.vCore.Dto.Warn;
import nl.vCore.Main;
import nl.vCore.Utils.MessageUtils;

import java.util.UUID;
import java.util.List;

public class MysqlWarnsFactory {
    private static final MySQLWarnsHandler sql = new MySQLWarnsHandler(Main.getInstance());

    /**
     * Creates a new warning in the database.
     *
     * @param w The Warn object to be created and stored.
     */
    private static void create(Warn w) {
        sql.create(w);
    }

    /**
     * Retrieves a warning from the database by its unique identifier.
     *
     * @param id The UUID of the warning to retrieve.
     * @return The Warn object associated with the given ID.
     */
    private static Warn read(UUID id){
        return sql.read(id);
    }

    /**
     * Updates an existing warning in the database.
     *
     * @param w The Warn object to be updated.
     */
    private static void update(Warn w){
        sql.update(w);
    }

    /**
     * Deletes a warning from the database.
     *
     * @param w The Warn object to be deleted.
     */
    private static void delete(Warn w){
        sql.delete(w);
    }

    /**
     * Retrieves all warnings from the database.
     *
     * @return A List of all Warn objects stored in the database.
     */
    private static List<Warn> getAll(){
        return sql.getAll();
    }

    /**
     * Retrieves all warnings associated with a specific user.
     *
     * @param u The User object for which to retrieve warnings.
     * @return A List of Warn objects associated with the given user.
     */
    private static List<Warn> getWarnsFromUser(User u){
        return sql.getWarnsFromUser(u);
    }

    /**
     * Initializes the database by creating the warns table if it doesn't exist.
     * This method should be called when the system starts for the first time.
     */
    private static void triggerFirstStart(){
        sql.createWarnsTableIfNotExists();
    }
}