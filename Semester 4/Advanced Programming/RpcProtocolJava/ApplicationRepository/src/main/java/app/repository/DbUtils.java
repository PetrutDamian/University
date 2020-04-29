package app.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DbUtils {
    private Properties properties;
    private Connection instance = null;
    Logger logger = LogManager.getLogger();
    public DbUtils(Properties properties) {
        this.properties = properties;
    }
    private Connection getNewConnection(){
        logger.traceEntry();
        String url=properties.getProperty("jdbc.url");
        Connection con = null;
        try{
            con = DriverManager.getConnection(url);
        }catch (SQLException ex){
            System.out.println("Eroare la conectare" + ex);
        }
        return con;
    }
    public Connection getConnection(){
        logger.traceEntry();
        try{
            if(instance==null || instance.isClosed())
                instance=getNewConnection();
        }catch (SQLException ex){
            System.out.println("Eroare baza de date: "+ex);
        }
        logger.traceExit(instance);
        return instance;
    }
}
