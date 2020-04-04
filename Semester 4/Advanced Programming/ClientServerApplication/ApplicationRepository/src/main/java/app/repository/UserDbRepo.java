package app.repository;

import app.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public class UserDbRepo implements IUserRepo {
    private DbUtils dbutils;
    private Logger logger = LogManager.getLogger();
    public UserDbRepo(Properties props) {
        logger.info("Constructing UserDbRepo with properties : {}",props);
        dbutils=new DbUtils(props);
    }
    @Override
    public void save(User entity) {

    }

    @Override
    public void delete(String s) {

    }

    @Override
    public void update(User entity) {

    }

    @Override
    public User findOne(String id){
        logger.traceEntry("Finding User with key : {}",id);
        Connection con = dbutils.getConnection();
        try(PreparedStatement stm = con.prepareStatement("select * from Users where id =?")){
            stm.setString(1,id);
            try(ResultSet result = stm.executeQuery()){
                if(result.next()){
                    String id2 = result.getString("id");
                    String pass = result.getString("password");
                    return new User(id2,pass);
                }
            }
        }catch (SQLException ex){
            logger.error(ex);
        }
        logger.traceExit();
        return null;
    }

    @Override
    public List<User> findAll() {
        return null;
    }
}
