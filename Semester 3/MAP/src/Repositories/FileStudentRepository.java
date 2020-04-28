package Repositories;

import Domain.Student;
import Validation.ValidationException;
import Validation.Validator;

import java.io.*;
import java.util.Collection;
import java.util.Iterator;

public class FileStudentRepository extends AbstractFileRepository<Integer, Student> {

    public FileStudentRepository(String filename , Validator<Student> val) throws ValidationException {
        super(filename,val);
    }

    @Override
    protected void writeToFile(String filename)  {
        try {
            Collection<Student> all = super.findAll();
            Iterator<Student> it = all.iterator();
            FileWriter csvWriter = new FileWriter(filename);
            while (it.hasNext()) {
                Student st = it.next();
                String id = String.valueOf(st.getId());
                String grp = String.valueOf(st.getGrp());
                csvWriter.append(id + "," + st.getNume() + "," + st.getPrenume() + "," + grp + ","
                        + st.getEmail() + "," + st.getCadruDidacticIndrumatorLab() + "\n");
                csvWriter.flush();
            }
            csvWriter.close();
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    @Override
    protected void loadFromFile(String filename) throws ValidationException {
        try {
            BufferedReader csvReader = null;
            csvReader = new BufferedReader(new FileReader(filename));
            String row = null;
            while (true) {
                if ((row = csvReader.readLine()) == null) break;
                String[] data = row.split(",");
                int id = Integer.parseInt(data[0]);
                int grp = Integer.parseInt(data[3]);
                Student st = new Student(id, data[1], data[2], grp, data[4], data[5]);
                super.saveNoWrite(st);
            }
            csvReader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
