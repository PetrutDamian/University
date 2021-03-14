import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class Main {
    static class Reader extends Thread{
        private MyQueue queue;
        private Integer start;
        private Integer end;
        public Reader(MyQueue queue,Integer start,Integer end){
            this.queue = queue;
            this.start = start;
            this.end = end;
        }

        private void processFile(String filename) throws FileNotFoundException {
            File f = new File(filename);
            Scanner reader = new Scanner(f);
            while(reader.hasNextLine()){
                String line = reader.nextLine();
                if(!line.equals(""))
                {
                    String[] components = line.split(",");
                    Node node = new Node(Integer.parseInt(components[0]),Integer.parseInt(components[1]));
                    queue.add(node);
                }
            }
        }

        @Override
        public void run() {
            for(Integer i=start;i<end;i++){
                String filename = "polinom" + i.toString() + ".txt";
                try {
                    processFile(filename);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class Worker extends Thread{
        private MyQueue queue;
        private  MyLinkedList list;
        public Worker(MyQueue queue, MyLinkedList list){
            this.queue =  queue;
            this.list = list;
        }

        @Override
        public void run() {
            boolean active = true;
            while(active){
                Node node = queue.remove();
                if(node!=null)
                    list.add(node);
                else
                    active = false;
            }
        }
    }



    public static void main(String[] args) throws Exception {
        int n = 40;//numar fisiere
        int p = 8; // numar threaduri
        int p1= 2;
        int p2 = p-p1;
        int gradMaxim = 100000;
        int nrMonoame =1000;
        //for(int i=1;i<=n;i++)
        //    Utils.writeToFile("polinom"+Integer.toString(i)+".txt",gradMaxim,nrMonoame);


        /////////////////////////////// PARALEL ////////////////////////////////////////
        MyLinkedList list = new MyLinkedList();
        MyQueue queue = new MyQueue();

        Worker[] workers = new Worker[p2];


        for (int i=0;i<p2;i++)
            workers[i] = new Worker(queue,list);

        for(int i=0;i<p2;i++)
            workers[i].start();


        int chunk = n/(p1+1);
        int rest = n%(p1+1);
        int start=1,end=chunk+1;
        if((rest--)>0)
            end++;
        int start2 = end,end2 = start2;
        Reader[] readers = new Reader[p1];
        for(int i=0;i<p1;i++){
            end2+=chunk;
            if((rest--)>0)
                end2++;
            readers[i] = new Reader(queue,start2,end2);
            start2=end2;
        }

        long startTime = System.currentTimeMillis();
        //System.out.println("da");
        for(int i=0;i<p1;i++)
            readers[i].start();

        for(int i = start; i<end; i++){
            File f = new File("polinom"+ i + ".txt");
            Scanner readFile = new Scanner(f);
            while(readFile.hasNextLine()){
                String line = readFile.nextLine();
                if(!line.equals(""))
                {
                    String[] components = line.split(",");
                    Node node = new Node(Integer.parseInt(components[0]),Integer.parseInt(components[1]));
                    queue.add(node);
                }
            }
        }

        for(int i=0;i<p1;i++)
            readers[i].join();

        queue.add(new Node(-1,-1));
        for(int i=0;i<p2;i++)
            workers[i].join();

        long endTime = System.currentTimeMillis();
        System.out.println("Paralel : "+(endTime-startTime));
        Utils.writeListToFile(list,"Paralel.txt");

        /////////////////////////////////// SECVENTIAL ////////////////////////////
        startTime = System.currentTimeMillis();
        list = new MyLinkedList();
        for(int i=1;i<=n;i++){
            File file = new File("polinom"+Integer.toString(i)+".txt");
            Scanner reader = new Scanner(file);
            while(reader.hasNextLine()){
                String line = reader.nextLine();
                if(!line.equals(""))
                {
                    String[] components = line.split(",");
                    Node node = new Node(Integer.parseInt(components[0]),Integer.parseInt(components[1]));
                    list.addNoLock(node);
                }
            }
        }
        endTime = System.currentTimeMillis();
        System.out.println("Secvential : "+(endTime-startTime));
        Utils.writeListToFile(list,"Secvential.txt");
        boolean equal = Utils.checkIfFilesAreEqual("Secvential.txt","Paralel.txt");
        System.out.println("checking if Secvential.txt and Paralel.txt are equal...");
        System.out.println(equal);


    }


}
