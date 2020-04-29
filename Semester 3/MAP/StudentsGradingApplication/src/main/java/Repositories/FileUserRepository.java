package Repositories;

import Domain.Grade;
import Domain.User;
import Validation.ValidationException;
import Validation.Validator;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

public class FileUserRepository extends AbstractFileRepository<String, User>{
    public FileUserRepository(String filename, Validator<User> val) throws ValidationException {
        super(filename, val);
    }
    @Override
    protected void writeToFile(String filename) {
        try{
            Iterator<User> it = super.findAll().iterator();
            BufferedWriter csvWriter = new BufferedWriter(new FileWriter(filename));
            while(it.hasNext()){
                User u = it.next();
                csvWriter.append(u.getId()+","+u.getPassword()+","+u.getPrivilege()+"\n");
                csvWriter.flush();
            }
            csvWriter.close();
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }
    @Override
    protected void loadFromFile(String filename) throws ValidationException {
        try{
            BufferedReader csvReader = new BufferedReader(new FileReader(filename));
            String row = null;
            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split(",");
                super.saveNoWrite(new User(data[0], data[1], data[2]));
            }
            csvReader.close();
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }
}