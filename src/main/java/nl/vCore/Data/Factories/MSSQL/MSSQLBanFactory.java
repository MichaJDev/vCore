package nl.vCore.Data.Factories.MSSQL;

import java.util.List;
import nl.vCore.Data.MSSQL.Bans.MSSQLBansHandler;
import nl.vCore.Dto.Ban;
import nl.vCore.Dto.User;
import nl.vCore.Main;
import nl.vCore.Utils.MessageUtils;

import java.sql.SQLException;

public class MSSQLBanFactory {

    private static final MSSQLBansHandler sqlHandler = new MSSQLBansHandler(Main.getInstance());
    private static final MessageUtils msg = new MessageUtils(Main.getInstance());
    public static void createTable(){
        try{
            msg.log("Creating new Bans Table in MSSQL DB...");
            sqlHandler.createBanTableIfNotExists();
        }catch(SQLException e){
            msg.severe(e.getMessage());
        }
    }
    public static void create(Ban b){
        msg.log("Creating Ban for " + Main.getInstance().getServer().getPlayer(b.getBannedUser().getId()) + "...") ;
        sqlHandler.createBan(b);
    }

    public static void update(Ban b){
        sqlHandler.updateBan(b);
    }

    public static void delete(Ban b){
        sqlHandler.deleteBan(b);
    }

    public static Ban read(User u, int id) {
        return sqlHandler.read(u,id);
    }
    public static List<Ban> getAllBans(){
        return sqlHandler.getAllBans();
    }
    public static List<Ban> getAllBansFromUser(User u){
        return sqlHandler.getAllBansFromUser(u);
    }
}
