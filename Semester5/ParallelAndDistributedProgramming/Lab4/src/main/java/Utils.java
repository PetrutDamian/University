import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;

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
}
