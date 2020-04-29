package Repository;

import Domain.Membru;
import Domain.MembruActiv;
import Utils.ActiveType;
import Utils.MemberType;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class RepoMembrii {
    String filename;
    List<MembruActiv> elems = new ArrayList<MembruActiv>();
    public RepoMembrii(String filename){
        this.filename=filename;
        loadFromFile();
    }
    private MembruActiv entityFromString(String str){
        String[] args = str.split(":");
        return new MembruActiv(args[0], MemberType.valueOf(args[1]), ActiveType.ACTIVE);
    }

    private void loadFromFile(){
        String x = new File("").getAbsolutePath();
        try(BufferedReader csvReader = new BufferedReader(new FileReader(new String(x+ "\\src\\main\\java\\Data\\"+filename).replace('\\','/'))))
        {
            String row = null;
            while(true){
                if((row=csvReader.readLine())==null)break;
                MembruActiv elem = entityFromString(row);
                elems.add(elem);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    public List<MembruActiv> getAll(){
        return elems;
    }
}
