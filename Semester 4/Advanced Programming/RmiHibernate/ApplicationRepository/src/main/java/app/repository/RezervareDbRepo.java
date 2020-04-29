package app.repository;

import app.model.Cursa;
import app.model.Rezervare;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

public class RezervareDbRepo implements IRezervareRepo {
    private DbUtils dbutils;
    private Logger logger = LogManager.getLogger();
    public RezervareDbRepo(Properties props){
        logger.info("Constructing RezervareDbRepo with properties : {}",props);
        dbutils=new DbUtils(props);
    }
    @Override
    public void save(Rezervare entity) {
        logger.traceEntry();
        Session session = Factory.sessionFactory.openSession();
        Transaction tx = null;
        try{
            tx =session.beginTransaction();
            session.save(entity);
            tx.commit();

        }catch (RuntimeException ex){
            if(tx!=null)
                tx.rollback();
        }
        finally{
            session.close();
        }


        logger.traceExit();
    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public void update(Rezervare entity) {

    }

    @Override
    public Rezervare findOne(Integer integer) {
        return null;
    }

    @Override
    public List<Rezervare> findAll() {
        return null;
    }

    @Override
    public List<Rezervare> findByIdCursa(Integer idCursa) {
        logger.traceEntry("Entered findByIdCursa: {}", idCursa);
        List<Rezervare> rezervari = new ArrayList<>();
        Session session = Factory.sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query query = session.createQuery("from Rezervare where idCursa=:key");
            query.setParameter("key", idCursa);
            rezervari = query.list();
            tx.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            session.close();
        }
        logger.traceExit(rezervari);
        return rezervari;
    }
}
