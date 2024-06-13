package nl.vCore.Data.Handlers.MSSQL;

import nl.vCore.Data.MSSQL.MSSQLHandler;
import nl.vCore.Dto.Warp;
import nl.vCore.Main;
import nl.vCore.Utils.MessageUtils;

import java.sql.SQLException;

public class MSSQLWarpsHandler {
    private static final MSSQLHandler sqlHandler = new MSSQLHandler(Main.getInstance());
    private static final MessageUtils msg = new MessageUtils(Main.getInstance());

    public static void createTable() {
        try {
            msg.log("Creating new Warps Table in MSSQL DB...");
            sqlHandler.createWarpsTableIfNotExists();
        } catch (SQLException e) {
            msg.severe(e.getMessage());
        }
    }

    public static void create(Warp w) {

    }

    public static void update(Warp w) {

    }

    public static void delete(Warp w) {

    }

    public static Warp read() {
        Warp warp = new Warp();
        return warp;
    }

}
