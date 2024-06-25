package nl.vCore.Data.Factories.MSSQL;

import nl.vCore.Data.MSSQL.Warps.MSSQLWarpsHandler;
import nl.vCore.Dto.Warp;
import nl.vCore.Main;
import nl.vCore.Utils.MessageUtils;

import java.sql.SQLException;

public class MSSQLWarpsFactory {
    private static final MSSQLWarpsHandler sqlHandler = new MSSQLWarpsHandler(Main.getInstance());
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
        sqlHandler.create(w);
    }

    public static void update(Warp w) {
        sqlHandler.update(w);
    }

    public static void delete(Warp w) {
        sqlHandler.delete(w);
    }

    public static Warp read(Warp w) {
        return sqlHandler.read(w.getName());
    }

}
