import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Main {
    private static CyclicBarrier barrier;

    static class MyThread extends Thread {
        private int lStart, cStart, lEnd, cEnd;
        private int[][] ma, kernel, ma2;

        public MyThread(int lStart, int cStart, int lEnd, int cEnd, int[][] ma, int[][] kernel) {
            this.lStart = lStart;
            this.cStart = cStart;
            this.lEnd = lEnd;
            this.cEnd = cEnd;
            this.ma = ma;
            this.kernel = kernel;
        }

        @Override
        public void run() {
            int initialLines = (cEnd==0?lEnd-lStart:lEnd-lStart+1);
            int  lines = initialLines + Math.min(kernel.length/2,ma.length-lStart-initialLines);
            lines = lines + Math.min(kernel.length / 2, lStart);

            ma2 = new int[lines][ma[0].length];
            for(int i=0;i<lines;i++)
                for(int j=0;j<ma[0].length;j++)
                    ma2[i][j] =  ma[i+Math.max(lStart-kernel.length/2,0)][j];

            try {
                barrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
            int j = cStart;
            for (int i = lStart; i <= lEnd; i++,j=0) {
                for(;j<ma[0].length;j++)
                {
                    if (i==lEnd && j == cEnd)
                        break;
                    ma[i][j] = filter(ma2,kernel,i+Math.min(kernel.length/2,lStart)-lStart,j);
                }
            }
        }
    }

    private static void writeToFile(String filename, int[][] ma) throws IOException {
        FileWriter writer = new FileWriter(filename);
        File f = new File(filename);
        for (int i = 0; i < ma.length; i++) {
            for (int j = 0; j < ma[0].length; j++)
                writer.write(ma[i][j] + " ");
            writer.write("\n");
        }
        writer.flush();
        writer.close();
    }

    private static void printMatrix(int[][] ma) {
        for (int i = 0; i < ma.length; i++) {
            for (int j = 0; j < ma[0].length; j++)
                System.out.print(ma[i][j] + " ");
            System.out.println();
        }
    }

    private static int filter(int[][] ma, int[][] kernel, int l, int c) {
        int sum = 0;
        int kl = 0, kc = 0;
        for (int i = l - kernel.length / 2; i <= l + kernel.length / 2; i++) {
            for (int j = c - kernel[0].length / 2; j <= c + kernel[0].length / 2; j++) {
                if(i>=0 && i<ma.length && j>=0 && j<ma[0].length)
                    sum = sum + ma[i][j] * kernel[kl][kc];
                kc++;
            }
            kl++;
            kc = 0;
        }
        return sum;
    }

    public static int[][] readFile(String filename, int n, int m) throws FileNotFoundException {
        int[][] ma = new int[n][m];
        Scanner scanner = new Scanner(new File(filename));
        int nr = 0;
        while (scanner.hasNextInt()) {
            int x = scanner.nextInt();
            ma[nr / m][nr % m] = x;
            nr++;
        }
        return ma;
    }

    public static void start(int p, int n, int m) throws Exception {
        int k = 5, l = 5;//dimensiunile filtrului
        int[][] kernel = new int[][]{{199,-220,340,678,924},{599,123,-999,-421,554},
                {234,421,421,567,444},{234,421,421,567,444},{234,421,421,567,444}};
        //Utils.generateRandomFile("date.txt",n*m,0,10000);
        int[][] ma = readFile("date.txt", n, m);

        //System.out.println("Matricea initiala:");
        //printMatrix(ma);
        //System.out.println("Kernel:");
        //printMatrix(kernel);

        int[][] ma2 = new int[n][m];

        long start = System.currentTimeMillis();
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++) {
                ma2[i][j] = filter(ma, kernel, i, j);
            }
        long end = System.currentTimeMillis();

        //System.out.println("Matricea dupa filtru");
        //printMatrix(ma2);
        writeToFile("secvential.txt", ma2);
        System.out.println("Secvential: " + (end - start));

        MyThread[] threads = new MyThread[p];
        start = System.currentTimeMillis();
        int elements = n * m;
        int chunk = elements / p;
        int remainder = elements % p;

        int lStart = 0, lEnd = 0, cStart = 0, cEnd = 0;

        for (int i = 0; i < p; i++) {
            int toProcess = (remainder--) > 0 ? chunk + 1 : chunk;
            lEnd += toProcess / m;
            cEnd += toProcess % m;
            if (cEnd >= m) {
                cEnd = cEnd % m;
                lEnd++;
            }

            //System.out.println("line:"+lStart+" col:"+cStart+" to line:"+lEnd+" col:"+cEnd);
            MyThread t = new MyThread(lStart, cStart, lEnd, cEnd, ma, kernel);
            threads[i] = t;
            cStart = cEnd;
            lStart = lEnd;
        }

        for (int i = 0; i < p; i++)
            threads[i].start();
        for (int i = 0; i < p; i++)
            threads[i].join();

        end = System.currentTimeMillis();
        writeToFile("paralel.txt", ma);
        System.out.println("Sunt identice:" + Utils.checkIfFilesAreEqual("secvential.txt", "paralel.txt"));
        //System.out.println("Matricea dupa filtru");
        //printMatrix(ma2);
        System.out.println("Paralel :" + (end - start));

    }

    public static void main(String[] args) throws Exception {
        int p = 16; // numar de threaduri
        int n = 10000, m = 10;//dimensiunile matricii
        barrier = new CyclicBarrier(p);
        start(p,n,m);
    }
}
