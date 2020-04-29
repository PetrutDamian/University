package Repositories;

import Domain.Student;
import Validation.ValidationException;
import Validation.Validator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public class StudentSqlRepo extends AbstractRepository<Integer,Student> implements SqlRepo {
    public StudentSqlRepo(Validator<Student> v) {
        super(v);
        loadFromSql();
    }
    private String createSqlAdd(Student st){
        String stString = "("+st.getId()+","+"'" + st.getNume()+"'"+",'"+st.getPrenume()+"',"+st.getGrp()+",'"+
                st.getEmail()+"','"+st.getCadruDidacticIndrumatorLab()+"');";
        return "INSERT INTO STUDENTS(ID,NUME,PRENUME,GRUPA,EMAIL,CADRU) VALUES"+stString;
    }
    private String createSqlDelete(String st){
        return "DELETE FROM STUDENTS WHERE ID="+st+";";
    }
    private String createSqlUpdate(Student st){

        return "DELETE FROM STUDENTS WHERE ID="+st.getId().toString()+"; "+ createSqlAdd(st);
    }
    @Override
    public Student findOne(Integer key) throws IllegalArgumentException {
        return super.findOne(key);
    }

    @Override
    public Collection<Student> findAll() {
        return super.findAll();
    }

    @Override
    public Student save(Student entity) throws IllegalArgumentException, ValidationException {
       Student st = super.save(entity);
       if(st==null){
           String sql = createSqlAdd(entity);
           writeToSql(sql);
       }
       return st;
    }

    @Override
    public Student delete(Integer key) throws IllegalArgumentException {
        Student st =  super.delete(key);
        if(st!=null){
            writeToSql(createSqlDelete(key.toString()));
        }
        return st;
    }

    @Override
    public Student update(Student entity) throws IllegalArgumentException, ValidationException {
        Student st = super.update(entity);
        if(st ==null){
            writeToSql(createSqlUpdate(entity));
        }
        return st;
    }


    @Override
    public void loadFromSql() {
        try {
            ResultSet resultSet = getResultSet("STUDENTS");
            while (resultSet.next()) {
                super.save(new Student(resultSet.getInt(1),resultSet.getString(2),resultSet.getString(3),
                        resultSet.getInt(4),resultSet.getString(5),resultSet.getString(6)));
            }
        } catch (SQLException | ValidationException e) {
            e.printStackTrace();
        }
    }
}
