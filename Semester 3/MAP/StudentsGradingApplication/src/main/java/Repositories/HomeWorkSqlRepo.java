package Repositories;

import Domain.HomeWork;
import Validation.ValidationException;
import Validation.Validator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public class HomeWorkSqlRepo extends AbstractRepository<Integer, HomeWork> implements SqlRepo {
    public HomeWorkSqlRepo(Validator<HomeWork> v) {
        super(v);
        loadFromSql();
    }
    private String createSqlAdd(HomeWork hm){
        String stString = "("+hm.getId().toString()+",'"+hm.getDescriere()+"',"+String.valueOf(hm.getStartWeek())
                +","+String.valueOf(hm.getDeadlineWeek())+");";
        return "INSERT INTO HOMEWORKS(ID,DESCRIERE,STARTWEEK,DEADLINEWEEK) VALUES"+stString;
    }
    private String createSqlDelete(String hm){
        return "DELETE FROM HOMEWORKS WHERE ID="+hm+";";
    }
    private String createSqlUpdate(HomeWork hm){
        return createSqlDelete(hm.getId().toString())+ createSqlAdd(hm);
    }
    @Override
    public HomeWork findOne(Integer key) throws IllegalArgumentException {
        return super.findOne(key);
    }

    @Override
    public Collection<HomeWork> findAll() {
        return super.findAll();
    }

    @Override
    public HomeWork save(HomeWork entity) throws IllegalArgumentException, ValidationException {
        HomeWork hm = super.save(entity);
        if(hm==null){
            String sql = createSqlAdd(entity);
            writeToSql(sql);
        }
        return hm;
    }

    @Override
    public HomeWork delete(Integer key) throws IllegalArgumentException {
        HomeWork hm= super.delete(key);
        if(hm!=null){
            writeToSql(createSqlDelete(hm.getId().toString()));
        }
        return hm;
    }

    @Override
    public HomeWork update(HomeWork entity) throws IllegalArgumentException, ValidationException {
       HomeWork hm = super.update(entity);
       if(hm==null){
           writeToSql(createSqlUpdate(hm));
       }
       return hm;
    }


    @Override
    public void loadFromSql() {
        try {
            ResultSet resultSet = getResultSet("HOMEWORKS");
            while (resultSet.next()) {
                super.save(new HomeWork(resultSet.getInt(1),resultSet.getString(2),resultSet.getInt(3),
                        resultSet.getInt(4)));
            }
        } catch (SQLException | ValidationException e) {
            e.printStackTrace();
        }
    }

}
