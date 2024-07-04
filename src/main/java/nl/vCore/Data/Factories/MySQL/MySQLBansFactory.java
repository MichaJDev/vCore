package nl.vCore.Data.Factories.MySQL;

import nl.vCore.Data.MySql.Bans.MySQLBanHandler;
import nl.vCore.Dto.Ban;
import nl.vCore.Main;

public class MySQLBansFactory {

    private static MySQLBanHandler bh = new MySQLBanHandler(Main.getInstance());

    private static void create(Ban b){
        bh.create(b);
    }

    private static Ban read(Ban b){
        return bh.read(b.getId());
    }

    private static void update(Ban b){
        bh.update(b);
    }

    private static void delete(Ban b){
        bh.delete(b);
    }

    private static void triggerFirstStart(){
        bh.createBansTableIfNotExists();
    }
}
