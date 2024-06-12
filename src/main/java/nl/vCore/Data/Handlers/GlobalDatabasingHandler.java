package nl.vCore.Data.Handlers;

import nl.vCore.Data.Handlers.MSSQL.*;

public class GlobalDatabasingHandler {

    public static void triggerYmlFirstTimeTask(){

    }
    public static void triggerMSSQLFirstTimeTask(){
        MSSQLUserHandler.createTable();
        MSSQLBanHandler.createTable();
        MSSQLHomesHandler.createTable();
        MSSQLWarpsHandler.createTable();
        MSSQLWarnsHandler.createTable();
    }
    public static void triggerMYSQLFirstTimeTask(){
    }
}
