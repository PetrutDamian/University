package Repositories;

import Domain.Grade;
import Validation.ValidationException;
import Validation.Validator;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

public class FileGradeRepository extends AbstractFileRepository<String, Grade> {
    public FileGradeRepository(String filename, Validator<Grade> val) throws ValidationException {
        super(filename, val);
    }

    @Override
    protected void writeToFile(String filename) {
        try{
            Iterator<Grade> it = super.findAll().iterator();
            BufferedWriter csvWriter = new BufferedWriter(new FileWriter(filename));
            while(it.hasNext()){
                Grade gr = it.next();
                String idStudent = String.valueOf(gr.getIdStudent());
                String idHomeWork = String.valueOf(gr.getIdHomeWork());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                String date = gr.getData().format(formatter);
                String grade = String.valueOf(gr.getGrade());
                csvWriter.append(idStudent+","+idHomeWork+","+ date+","+gr.getTeacher()+"," +grade+"\n");
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
            while(true){
                if((row=csvReader.readLine())==null)break;
                String[] data = row.split(",");
                int idStudent = Integer.parseInt(data[0]);
                int idHomeWork = Integer.parseInt(data[1]);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                LocalDateTime date = LocalDateTime.parse(data[2], formatter);
                float grade = Float.parseFloat(data[4]);
                super.saveNoWrite(new Grade(idStudent,idHomeWork,date,data[3],grade));
            }
            csvReader.close();
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }
}
