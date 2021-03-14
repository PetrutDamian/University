import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Random;

public class Utils {
    public static  boolean checkIfFilesAreEqual(String file1, String file2) throws Exception {
        File f1 = new File(file1);
        File f2 = new File(file2);
        if (!(f1.isFile()  && f2.isFile()))
        {
            System.out.println("File not found!\n");
            throw new Exception("File not found!\n");
        }

        byte[] first = Files.readAllBytes(f1.toPath());
        byte[] second = Files.readAllBytes(f2.toPath());

        boolean egale = Arrays.equals(first,second);
        return egale;
    }
    public static void writeToFile(String filename,int gradMaxim,int nrMonoame){
        Random rand = new Random();
        try{
            FileWriter writer = new FileWriter(filename);
            File f = new File(filename);
            for (int i=0;i<nrMonoame;i++){
                Integer grad = rand.nextInt(gradMaxim);
                Integer c = rand.nextInt(100)+1;
                writer.write(c.toString()+","+grad.toString()+'\n');
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void writeListToFile(MyLinkedList list, String filename){
        try{
            FileWriter writer = new FileWriter(filename);
            File f = new File(filename);
            Node crt = list.head;
            while(crt!=null){
                writer.write(crt.c.toString()+"x^"+crt.exp.toString()+"\n");
                crt = crt.next;
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
