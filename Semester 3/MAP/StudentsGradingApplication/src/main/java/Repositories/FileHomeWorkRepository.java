package Repositories;

import Domain.HomeWork;
import Validation.ValidationException;
import Validation.Validator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

public class FileHomeWorkRepository extends AbstractFileRepository<Integer, HomeWork> {

    public FileHomeWorkRepository(String filename, Validator<HomeWork> val) throws ValidationException {
        super(filename, val);

    }

    @Override
    protected void writeToFile(String filename) {
        try{
            Collection<HomeWork> all = super.findAll();
            Iterator<HomeWork> it = all.iterator();
            FileWriter csvWriter = new FileWriter(filename);
            while (it.hasNext()) {
                HomeWork hm = it.next();
                String id = String.valueOf(hm.getId());
                String startWeek = String.valueOf(hm.getStartWeek());
                String endWeek = String.valueOf(hm.getDeadlineWeek());
                csvWriter.append(id + "," + hm.getDescriere() + ","+startWeek + ","+endWeek+"\n");
                csvWriter.flush();
            }
            csvWriter.close();
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    @Override
    protected void loadFromFile(String filename) {
        try{
            BufferedReader csvReader = new BufferedReader(new FileReader(filename));
            String row = null;
            while(true){
                if ((row = csvReader.readLine()) == null) break;
                String[] data = row.split(",");
                int id = Integer.parseInt(data[0]);
                int startWeek = Integer.parseInt(data[2]);
                int endWeek = Integer.parseInt(data[3]);
                super.saveNoWrite(new HomeWork(id,data[1],startWeek,endWeek));
            }
        }catch(IOException|ValidationException ex){
            ex.printStackTrace();
        }
    }
}
