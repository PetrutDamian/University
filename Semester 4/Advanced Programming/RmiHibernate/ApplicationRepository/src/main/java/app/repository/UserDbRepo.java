package app.repository;

import app.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
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
        Session session =  Factory.sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query query = session.createQuery("from User where id=:key");
            query.setParameter("key", id);
            List users = query.list();
            if (users.size() == 0)
                return null;
            Iterator it = users.iterator();
            it.hasNext();
            User user = (User) it.next();
            tx.commit();
            return user;

        }catch (Exception e){
            e.printStackTrace();
        }
        finally{
            session.close();
        }

        return null;
    }

    @Override
    public List<User> findAll() {
        return null;
    }
}
