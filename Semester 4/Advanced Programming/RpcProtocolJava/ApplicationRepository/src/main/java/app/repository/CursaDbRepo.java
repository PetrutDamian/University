package app.repository;

import app.model.Cursa;
import jdk.vm.ci.meta.Local;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

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
        Connection con = dbutils.getConnection();
        try(PreparedStatement stm = con.prepareStatement("insert into Curse values (?,?,?,?)"))
        {
            stm.setInt(1,entity.getId());
            stm.setString(2,entity.getDestinatie());
            stm.setString(3,entity.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            stm.setInt(4,entity.getLocuriDisponibile());
            int result = stm.executeUpdate();
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Eroare la save in bd" + ex);
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
        Connection con = dbutils.getConnection();
        try(PreparedStatement stm = con.prepareStatement("" +
                "update Curse set destinatie=?,date=?,locuri=? where id=?")){
            stm.setString(1,entity.getDestinatie());
            stm.setString(2,entity.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            stm.setInt(3,entity.getLocuriDisponibile());
            stm.setInt(4,entity.getId());
            int result = stm.executeUpdate();
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println(ex);
        }
        logger.traceExit();

    }

    @Override
    public Cursa findOne(Integer key) {
        logger.traceEntry("Finding Cursa with {}",key);
        Connection con = dbutils.getConnection();
        try(PreparedStatement stm = con.prepareStatement("select * from Curse where id=?"))
        {
            stm.setInt(1,key);
            try(ResultSet result = stm.executeQuery()){
                if(result.next()){
                    int id = result.getInt("id");
                    String destinatie = result.getString("destinatie");
                    String r =result.getString("date");
                    LocalDateTime date = LocalDateTime.parse( result.getString("date"),DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    Integer nrLocuri = result.getInt("locuri");
                    return new Cursa(id,destinatie,date,nrLocuri);
                }
            }
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Eroare sql la findOne " + ex);
        }
        logger.traceExit("Cursa with id {} not found",key);
        return null;
    }

    @Override
    public List<Cursa> findAll() {
        logger.traceEntry();
        Connection con = dbutils.getConnection();
        List<Cursa> curse = new ArrayList<>();
        try(PreparedStatement stm = con.prepareStatement("select * from Curse;")){
            try(ResultSet result  = stm.executeQuery()){
                while(result.next()){
                    int id = result.getInt("id");
                    String destinatie = result.getString("destinatie");
                    LocalDateTime date = LocalDateTime.parse( result.getString("date"),DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    Integer nrLocuri = result.getInt("locuri");
                    curse.add(new Cursa(id,destinatie,date,nrLocuri));
                }
            }
        } catch (SQLException e) {
            logger.error(e);
        }
        logger.traceExit(curse);
        return curse;
    }

    @Override
    public List<Cursa> findByDestinationAndDate(String destination, LocalDateTime date) {
        Connection con = dbutils.getConnection();
        List<Cursa> curse = new ArrayList<>();
        try{
            try(PreparedStatement stm = con.prepareStatement("select * from Curse where destinatie=? and date =?")){
                stm.setString(1,destination);
                stm.setString(2,date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                try(ResultSet result = stm.executeQuery()){
                    while(result.next()){
                        int id = result.getInt("id");
                        String destinatie = result.getString("destinatie");
                        LocalDateTime date2 = LocalDateTime.parse( result.getString("date"),DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        Integer nrLocuri = result.getInt("locuri");
                        curse.add(new Cursa(id,destinatie,date2,nrLocuri));
                    }
                }
            }
        }catch (SQLException ex){
            logger.error(ex);
        }
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return curse;
    }
}