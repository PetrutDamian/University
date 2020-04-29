package app.repository;

import app.model.Rezervare;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
        Connection con = dbutils.getConnection();
        try(PreparedStatement stm = con.prepareStatement("insert into Rezervari (idCursa, nrLoc, client) values (?,?,?)")){
            stm.setInt(1,entity.getIdCursa());
            stm.setInt(2,entity.getNrLoc());
            stm.setString(3,entity.getClient());
            int result = stm.executeUpdate();
        }catch (SQLException ex){
            logger.error(ex);
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
        Connection con = dbutils.getConnection();
        try(PreparedStatement stm = con.prepareStatement("select * from Rezervari where idCursa=?")){
            stm.setInt(1,idCursa);
            try(ResultSet result = stm.executeQuery()){
                while(result.next()){
                    int id = result.getInt(1);
                    int idCursa2 = result.getInt(2);
                    int nrLoc = result.getInt(3);
                    String client = result.getString(4);
                    rezervari.add(new Rezervare(id,idCursa2,nrLoc,client));
                }
            }
        }catch (SQLException ex){
            logger.error(ex);
        }
        logger.traceExit(rezervari);
        return rezervari;
    }
}
