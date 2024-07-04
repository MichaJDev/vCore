package nl.vCore.Data.Factories.MySQL;

import nl.vCore.Data.MySql.Users.MySQLUserHandler;
import nl.vCore.Dto.User;
import nl.vCore.Main;

public class MySQLUserFactory {

    private static final MySQLUserHandler uh = new MySQLUserHandler(Main.getInstance());

    public static void triggerFirstStart(){
        uh.createUserTableIfNotExist();
    }

    public static void create(User u){
        uh.create(u);
    }

    public static User read(User u){
        return  uh.read(u.getId());
    }

    public static void update(User u){
        uh.update(u);
    }

    public static void delete(User u){
        uh.delete(u.getId());
    }
}
