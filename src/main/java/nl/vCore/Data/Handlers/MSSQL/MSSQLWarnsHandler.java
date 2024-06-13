package nl.vCore.Data.Handlers.MSSQL;

import nl.vCore.Data.MSSQL.MSSQLHandler;
import nl.vCore.Dto.Warn;
import nl.vCore.Main;
import nl.vCore.Utils.MessageUtils;

import java.sql.SQLException;

public class MSSQLWarnsHandler {

    private static final MSSQLHandler sqlHandler = new MSSQLHandler(Main.getInstance());
    private static final MessageUtils msg = new MessageUtils(Main.getInstance());

    public static void createTable() {
        try {
            msg.log("Creating new Warps Table in MSSQL DB...");
            sqlHandler.createWarnsTableIfNotExists();
        } catch (SQLException e) {
            msg.severe(e.getMessage());
        }
    }

    public static void create(Warn w) {

    }

    public static void update(Warn w) {

    }

    public static void delete(Warn w) {

    }

    public static Warn read(int id) {
        Warn w = new Warn();
        return w;
    }
}
