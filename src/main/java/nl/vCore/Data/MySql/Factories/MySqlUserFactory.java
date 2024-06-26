package nl.vCore.Data.MySql.Factories;

import nl.vCore.Data.MySql.MySQLUserHandler;
import nl.vCore.Dto.User;
import nl.vCore.Main;

import java.util.UUID;

public class MySqlUserFactory {

    private static final MySQLUserHandler uh = new MySQLUserHandler(Main.getInstance());

    public static void createUserTableIfNotExist(){
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
