package database;

public class DatabaseConnectionFactory {

    public static JDBConnectionWrapper getConnectionWrapper(boolean test){
        if (test)
            return new JDBConnectionWrapper(Constants.Schemas.TEST);
        else
            return new JDBConnectionWrapper(Constants.Schemas.PRODUCTION);
    }
}
