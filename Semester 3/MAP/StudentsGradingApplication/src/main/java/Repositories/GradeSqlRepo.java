package Repositories;

import Domain.Grade;
import Validation.ValidationException;
import Validation.Validator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

public class GradeSqlRepo extends AbstractRepository<String, Grade> implements SqlRepo {
    public GradeSqlRepo(Validator<Grade> v) {
        super(v);
        loadFromSql();
    }
    private String createSqlAdd(Grade gr){
        String grString = "('"+gr.getId()+"',"+String.valueOf(gr.getIdStudent())+","+String.valueOf(gr.getIdHomeWork())+
                ",'"+gr.getData().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))+"',"+String.valueOf(gr.getGrade())
                +",'"+gr.getTeacher()+"');";
        return "INSERT INTO GRADES(ID,IDSTUDENT,IDHOMEWORK,DATE,GRADE,TEACHER) VALUES"+grString;
    }

    private String createSqlDelete(String gr){
        return "DELETE FROM GRADES WHERE ID="+gr+";";
    }
    private String createSqlUpdate(Grade gr) {
        return createSqlDelete(gr.getId().toString()) + createSqlAdd(gr);
    }
    @Override
    public Grade findOne(String key) throws IllegalArgumentException {
        return super.findOne(key);
    }

    @Override
    public Collection<Grade> findAll() {
        return super.findAll();
    }

    @Override
    public Grade save(Grade entity) throws IllegalArgumentException, ValidationException {
        Grade gr = super.save(entity);
        if(gr==null){
            String sql = createSqlAdd(entity);
            writeToSql(sql);
        }
        return gr;
    }

    @Override
    public Grade delete(String key) throws IllegalArgumentException {
        Grade g  = super.delete(key);
        if(g!=null){
            writeToSql(createSqlDelete(g.getId()));
        }
        return g;
    }

    @Override
    public Grade update(Grade entity) throws IllegalArgumentException, ValidationException {
        Grade g = super.update(entity);
        if(g==null){
            writeToSql(createSqlUpdate(entity));
        }
        return g;
    }


    @Override
    public void loadFromSql() {
        try {
            ResultSet resultSet = getResultSet("GRADES");
            while (resultSet.next()) {
                super.save(new Grade(resultSet.getInt(2),resultSet.getInt(3),
                        LocalDateTime.parse(resultSet.getString(4),DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                        resultSet.getString(6),resultSet.getFloat(5)));
            }
        } catch (SQLException | ValidationException e) {
            e.printStackTrace();
        }

    }
}
