package nl.vCore.Data.Factories.MSSQL;

import nl.vCore.Data.MSSQL.Warns.MSSQLWarnsHandler;
import nl.vCore.Dto.User;
import nl.vCore.Dto.Warn;
import nl.vCore.Main;
import nl.vCore.Utils.MessageUtils;

import java.sql.SQLException;
import java.util.List;

public class MSSQLWarnsFactory {

    private static final MSSQLWarnsHandler sqlHandler = new MSSQLWarnsHandler(Main.getInstance());
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
        sqlHandler.create(w);
    }

    public static void update(Warn w) {
        sqlHandler.update(w);
    }

    public static void delete(Warn w) {
        sqlHandler.delete(w);
    }

    public static Warn read(User u, int id) {
        Warn w = new Warn();
        w.setId(id);
        w.setWarned(u);
        return sqlHandler.read(w);
    }

    public static List<Warn> getAll(User u){
        return sqlHandler.getAll(u);
    }
    public static List<Warn> getWarnsFromUser(User u){
        return sqlHandler.getWarningsFromUser(u);
    }
}
