
package main.java.edu.mmcoffee.colegiogotitas.config;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnection {
    private static Connection connection;
/*
    el constructor tiene que ser privado, esto para
    evitar que se creen instancias de esta clse
    */
 
    private DataBaseConnection() {
    }
    public static Connection getConnectionDataBase() throws Exception{
        if(connection == null || connection.isClosed()){
            connection = DriverManager.getConnection(Credentials.URL_DB, Credentials.USER_DB, Credentials.PASS_DB);
        }
        return connection;
    }
}