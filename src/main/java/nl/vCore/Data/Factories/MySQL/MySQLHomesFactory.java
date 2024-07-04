package nl.vCore.Data.Factories.MySQL;

import java.util.List;
import nl.vCore.Data.MySql.Homes.MySQLHomesHandler;
import nl.vCore.Dto.Home;
import nl.vCore.Dto.User;
import nl.vCore.Main;
import nl.vCore.Utils.MessageUtils;

public class MySQLHomesFactory {

    private static final MySQLHomesHandler sql = new MySQLHomesHandler(Main.getInstance());
    private static final MessageUtils msg = new MessageUtils(Main.getInstance());
    public static void create(Home h){
        sql.create(h);
    }

    public static Home read(Home h){
        return sql.read(h.getId());
    }

    public static void update(Home h){
        sql.update(h);
    }

    public static void delete(Home h){
        sql.delete(h);
    }

    public static boolean checkIfHomeExists(Home h){
        return sql.checkIfHomeExist(h);
    }

    public static List<Home> getListFromUser(User u){
        return sql.getListFromUser(u);
    }
    public static List<Home> getAll(){
        return sql.getAll();
    }

    public static void triggerFirstStart(){
        sql.createHomesTableIfNotExists();
    }
}
