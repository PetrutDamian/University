import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    static class MyThread extends  Thread{
        private int lStart,cStart,lEnd,cEnd;
        private int[][] ma,kernel,ma2;

        public MyThread(int lStart, int cStart, int lEnd, int cEnd, int[][] ma, int[][] kernel, int[][] ma2) {
            this.lStart = lStart;
            this.cStart = cStart;
            this.lEnd = lEnd;
            this.cEnd = cEnd;
            this.ma = ma;
            this.kernel = kernel;
            this.ma2 = ma2;
        }

        @Override
        public void run(){
            int j = cStart;
            for(int i=lStart;i<=lEnd;i++) {
                while (j < ma2[0].length) {
                    if (i == lEnd && j == cEnd)
                        break;
                    ma2[i][j] = filter(ma, kernel, i + kernel.length / 2, j + kernel[0].length / 2);
                    j++;
                }
                j=0;
            }
        }

    }
    private static void writeToFile(String filename,int [][] ma) throws IOException {
        FileWriter writer =new FileWriter(filename);
        File f = new File(filename);
        for(int i=0;i<ma.length;i++)
        {
            for(int j=0;j<ma[0].length;j++)
                writer.write(ma[i][j]+" ");
            writer.write("\n");
        }
        writer.flush();
        writer.close();
    }
    private static void printMatrix(int[][] ma){
        for(int i=0;i<ma.length;i++)
        {
            for(int j=0;j<ma[0].length;j++)
                System.out.print(ma[i][j]+" ");
            System.out.println();   
        }
    }
    private static int filter(int[][] ma,int [][] kernel, int l,int c){
        int sum = 0;
        int kl=0,kc=0;
        for(int i=l-kernel.length/2;i<=l+kernel.length/2;i++) {
            for (int j = c - kernel[0].length / 2; j <= c + kernel[0].length / 2; j++) {
                sum = sum + ma[i][j]*kernel[kl][kc++];
            }
            kl++;
            kc=0;
        }
        return sum;
    }

    public static int[][] readFile(String filename,int n,int m,int l,int c) throws FileNotFoundException {
        int borderLines = l-1;
        int borderColumns = c-1;
        int[][] ma = new int[n+borderLines][m+borderColumns];

        Scanner scanner = new Scanner(new File(filename));
        int nr = 0;
        while(scanner.hasNextInt()){
            int x = scanner.nextInt();
            ma[borderLines/2+nr/m][borderColumns/2+nr%m] = x;
            nr++;
        }
    return ma;
    }

    public static void main(String[] args) throws Exception {
        int p = 16; // numar de threaduri
        int n = 10000,m=10;//dimensiunile matricii
        int k=5,l=5;//dimensiunile filtrului
        int [][] kernel =  new int[][]{{25,-2,-48,101,54},{14,10,50,32,32},{33,21,-100,33,42},{33,21,-100,33,42},{33,21,-100,33,42}
        };
        //Utils.generateRandomFile("date.txt",n*m,0,10000);
        int[][] ma = readFile("date.txt",n,m,k,l);

        //System.out.println("Matricea initiala:");
        //printMatrix(ma);
        //System.out.println("Kernel:");
        //printMatrix(kernel);

        int [][] ma2 = new int [n][m];

        long start = System.currentTimeMillis();
        for(int i=0;i<n;i++)
            for(int j=0;j<m;j++){
                ma2[i][j] = filter(ma,kernel,i+k/2,j+l/2);
            }
        long end = System.currentTimeMillis();

        //System.out.println("Matricea dupa filtru");
        //printMatrix(ma2);
        writeToFile("secvential.txt",ma2);
        System.out.println("Secvential: "+(end-start));
        ma2 = new int [n][m];

        MyThread[] threads = new MyThread[p];
        start = System.currentTimeMillis();
        int elements = n*m;
        int chunk = elements/p;
        int remainder = elements%p;

        int lStart=0,lEnd=0,cStart =0,cEnd=0;

        for(int i=0;i<p;i++){
            int toProcess = (remainder--)>0?chunk+1:chunk;
            while(toProcess>0){
                cEnd++;
                if(cEnd%m==0)
                {
                    cEnd = 0;
                    lEnd++;
                }
                toProcess--;
            }
            //System.out.println("line:"+lStart+" col:"+cStart+" to line:"+lEnd+" col:"+cEnd);
            MyThread t = new MyThread(lStart,cStart,lEnd,cEnd,ma,kernel,ma2);
            threads[i] = t;
            cStart = cEnd;
            lStart = lEnd;
        }

        for(int i=0;i<p;i++)
            threads[i].start();
        for(int i=0;i<p;i++)
            threads[i].join();

        end = System.currentTimeMillis();
        writeToFile("paralel.txt",ma2);
        System.out.println("Sunt identice:"+Utils.checkIfFilesAreEqual("secvential.txt","paralel.txt"));
        //System.out.println("Matricea dupa filtru");
        //printMatrix(ma2);
        System.out.println("Paralel :"+(end-start));
    }
}
