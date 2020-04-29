package Repository;

import Domain.MembruActiv;
import Domain.Mesaj;
import Utils.ActiveType;
import Utils.MemberType;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class RepoMesaje {
    String filename;
    List<Mesaj> elems = new ArrayList<Mesaj>();
    public RepoMesaje(String filename){
        this.filename=filename;
        loadFromFile();
    }
    private Mesaj entityFromString(String str){
        String[] args = str.split(",");
        return new Mesaj(args[0],args[1],args[2], LocalDateTime.parse(args[3]));
    }
    private void loadFromFile(){
        String x = new File("").getAbsolutePath();
        try(BufferedReader csvReader = new BufferedReader(new FileReader(new String(x+ "\\src\\main\\java\\Data\\"+filename).replace('\\','/'))))
        {
            String row = null;
            while(true){
                if((row=csvReader.readLine())==null)break;
                Mesaj elem = entityFromString(row);
                elems.add(elem);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    private void writeToFile(){
        try(BufferedWriter csvWriter = new BufferedWriter(new FileWriter(filename))){
            for (Mesaj elem : elems) {
                csvWriter.append(entityToString(elem));
            }
            csvWriter.flush();
        }catch (Exception ex){ex.printStackTrace();}
    }

    private String entityToString(Mesaj elem) {
        return elem.getFrom()+","+elem.getTo()+","+elem.getContent()+","+elem.getDate()+"\n";
    }

    public void save(Mesaj msg){
        elems.add(msg);
        writeToFile();
    }
    public List<Mesaj> getAll(){
        return elems;
    }



}
