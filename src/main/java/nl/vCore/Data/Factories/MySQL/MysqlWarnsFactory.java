package nl.vCore.Data.Factories.MySQL;

import nl.vCore.Data.MySql.Warns.MySQLWarnsHandler;
import nl.vCore.Dto.User;
import nl.vCore.Dto.Warn;
import nl.vCore.Main;
import nl.vCore.Utils.MessageUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Factory for MySQL warns database operations
 * 
 * @version 1.1.0
 */
public class MySQLWarnsFactory {
    private static final MySQLWarnsHandler warnsHandler = new MySQLWarnsHandler(Main.getInstance());
    private static final MessageUtils msgUtils = new MessageUtils(Main.getInstance());

    /**
     * Creates the warns table if it doesn't exist
     */
    public static void createTable() {
        try {
            msgUtils.log("Creating MySQL warns table if not exists...");
            warnsHandler.createWarnsTableIfNotExists();
            msgUtils.log("MySQL warns table created or already exists.");
        } catch (Exception e) {
            msgUtils.severe("Failed to create warns table: " + e.getMessage());
        }
    }

    /**
     * Creates a new warn in the database
     * 
     * @param warn The warn to create
     */
    public static void create(Warn warn) {
        try {
            warnsHandler.create(warn);
            msgUtils.log("Warn record created successfully!");
        } catch (Exception e) {
            msgUtils.severe("Failed to create warn: " + e.getMessage());
        }
    }

    /**
     * Retrieves a warning from the database by its ID.
     *
     * @param id The UUID of the warning to retrieve.
     * @return The Warn object with the specified ID.
     */
    public static Warn read(UUID id) {
        return warnsHandler.read(id);
    }

    /**
     * Updates a warn in the database
     * 
     * @param warn The warn to update
     */
    public static void update(Warn warn) {
        try {
            warnsHandler.update(warn);
            msgUtils.log("Warn record updated successfully!");
        } catch (Exception e) {
            msgUtils.severe("Failed to update warn: " + e.getMessage());
        }
    }

    /**
     * Deletes a warn from the database
     * 
     * @param warn The warn to delete
     */
    public static void delete(Warn warn) {
        try {
            warnsHandler.delete(warn);
            msgUtils.log("Warn record deleted successfully!");
        } catch (Exception e) {
            msgUtils.severe("Failed to delete warn: " + e.getMessage());
        }
    }

    /**
     * Retrieves all warnings from the database.
     *
     * @return A List containing all Warn objects stored in the database.
     */
    public static List<Warn> getAll() {
        return warnsHandler.getAll();
    }

    /**
     * Retrieves all warnings associated with a specific user.
     *
     * @param user The User object for which to retrieve warnings.
     * @return A List of Warn objects associated with the given user.
     */
    public static List<Warn> getWarnsFromUser(User user) {
        return warnsHandler.getWarnsFromUser(user);
    }
    
    /**
     * Initializes the database by creating the warns table if it doesn't exist.
     * This method should be called when the system starts for the first time.
     */
    public static void triggerFirstStart() {
        warnsHandler.createWarnsTableIfNotExists();
    }

    /**
     * Gets all warns for a player
     * 
     * @param playerUuid The UUID of the player
     * @return List of warns for the player
     */
    public static List<Warn> getWarnsForPlayer(UUID playerUuid) {
        try {
            return warnsHandler.getWarnsForPlayer(playerUuid);
        } catch (Exception e) {
            msgUtils.severe("Failed to get warns for player: " + e.getMessage());
            return java.util.Collections.emptyList();
        }
    }

    /**
     * Gets all warns from the database
     * 
     * @return List of all warns
     */
    public static List<Warn> getAllWarns() {
        try {
            return warnsHandler.getAllWarns();
        } catch (Exception e) {
            msgUtils.severe("Failed to get all warns: " + e.getMessage());
            return java.util.Collections.emptyList();
        }
    }

    /**
     * Checks if a warn exists
     * 
     * @param warn The warn to check
     * @return true if the warn exists, false otherwise
     */
    public static boolean doesWarnExist(Warn warn) {
        try {
            return warnsHandler.doesWarnExist(warn);
        } catch (Exception e) {
            msgUtils.severe("Failed to check if warn exists: " + e.getMessage());
            return false;
        }
    }
}