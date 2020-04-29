package app.repository;

import app.model.Cursa;
import app.model.Cursa2;
import app.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;


import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class CursaDbRepo implements ICursaRepo {
    private DbUtils dbutils;
    private Logger logger = LogManager.getLogger();
    public CursaDbRepo(Properties props){
        logger.info("Constructing CursaDbRepo with properties : {}",props);
        dbutils=new DbUtils(props);
    }
    @Override
    public void save(Cursa entity) {
        logger.traceEntry("saving Cursa {}",entity);
        Session session = Factory.sessionFactory.openSession();
        Transaction tx = null;
        try{
            tx =session.beginTransaction();
            session.save(new Cursa2(entity.getId(),entity.getDestinatie(),
                    entity.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),entity.getLocuriDisponibile()));
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
    public void delete(Integer key) {
        logger.traceEntry("deleting Cursa with {}",key);
        Connection con = dbutils.getConnection();
        try(PreparedStatement stm = con.prepareStatement("delete from Curse where id=?"))
        {
            stm.setInt(1,key);
            int result =stm.executeUpdate();
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Exceptie sql" + ex);
        }
        logger.traceExit();
    }

    @Override
    public void update(Cursa entity) {
        logger.traceEntry("Updating Cursa {}",entity);
        Session session =  Factory.sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Cursa2 cursa = (Cursa2) session.load(Cursa2.class,entity.getId());
            cursa.setLocuriDisponibile(entity.getLocuriDisponibile());
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            session.close();
        }
        logger.traceExit();

    }

    @Override
    public Cursa findOne(Integer key) {
        logger.traceEntry("Finding Cursa with {}",key);
        Session session =  Factory.sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query query = session.createQuery("from Cursa2 where id=:key");
            query.setParameter("key", key);
            List<Cursa2> curse = query.list();
            Iterator<Cursa2> it = curse.iterator();
            if (!it.hasNext())
                return null;
            Cursa2 cursa = (Cursa2) it.next();
            tx.commit();
            return new Cursa(cursa.getId(),cursa.getDestinatie(),LocalDateTime.parse(cursa.getDate(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),cursa.getLocuriDisponibile());

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            session.close();
        }

        logger.traceExit("Cursa with id {} not found",key);
        return null;
    }

    @Override
    public List<Cursa> findAll() {
        logger.traceEntry();
        List<Cursa2> curse = new ArrayList<>();
        Session session =  Factory.sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query query = session.createQuery("from Cursa2");
            curse = query.list();
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            session.close();
        }
        logger.traceExit(curse);
        List<Cursa> result = curse.stream().map(x->{
            return new Cursa(x.getId(),x.getDestinatie(),LocalDateTime.parse(x.getDate(),DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                    x.getLocuriDisponibile());
        }).collect(Collectors.toList());
        return result;
    }

    @Override
    public List<Cursa> findByDestinationAndDate(String destination, LocalDateTime date) {
        Connection con = dbutils.getConnection();
        List<Cursa2> curse = new ArrayList<>();
        Session session =  Factory.sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query query = session.createQuery("from Cursa2 where destinatie=:key1 and date=:key2");
            query.setParameter("key1",destination);
            query.setParameter("key2",date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            curse = query.list();
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            session.close();
        }
        List<Cursa> result = curse.stream().map(x->{
            return new Cursa(x.getId(),x.getDestinatie(),LocalDateTime.parse(x.getDate(),DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                    x.getLocuriDisponibile());
        }).collect(Collectors.toList());
        return result;
    }
}