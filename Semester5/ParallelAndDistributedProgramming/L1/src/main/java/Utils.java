import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Random;

public class Utils {
    /*
    Checks if 2 files have identical contents.
    in - file1:String,file2:String
    out - True if files have identical content
        - False otherwise
    Throws exception if either file doesn't exist.
     */
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
    /*
        Generates random file
        size - number of integers generated
        [min,max] - integer range
     */
    public static void generateRandomFile(String filename,Integer size,Integer min,Integer max){
        Random rand = new Random();
        Integer range = max-min;
        try {
            FileWriter writer = new FileWriter(filename);
            File f = new File(filename);
            if (f.createNewFile())
                System.out.println("File created");
            else
                System.out.println("File overwritten");
            for(int i=1;i<=size;i++){
                Integer rnd = (rand.nextInt(range)+min);
                writer.write(rnd+" ");
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            System.out.println("Eroare la FileWriter!\n");
            e.printStackTrace();
        }
    }

}
