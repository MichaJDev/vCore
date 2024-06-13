package nl.vCore.Data.Handlers.MSSQL;

import nl.vCore.Data.MSSQL.Homes.MSSQLHomesHandler;
import nl.vCore.Dto.Home;
import nl.vCore.Dto.User;
import nl.vCore.Main;
import nl.vCore.Utils.MessageUtils;

import java.sql.SQLException;

public class MSSQLHomesFactory {

    private static final MSSQLHomesHandler sqlHandler = new MSSQLHomesHandler(Main.getInstance());
    private static final MessageUtils msg = new MessageUtils(Main.getInstance());
    public static void createTable(){
        try{
            msg.log("Creating new Homes Table in MSSQL DB...");
            sqlHandler.createHomesTableIfNotExists();
        }catch(SQLException e){
            msg.severe(e.getMessage());
        }
    }

    public static void create(Home h){

    }

    public static void update(Home h){

    }

    public static void delete(Home h){

    }
    public static Home read(User u, int id){
        Home home = new Home();
        return home;
    }
}
