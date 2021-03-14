import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    static class Worker extends Thread{
        private MyQueue queue;
        private MyLinkedList list;
        public Worker(MyQueue queue,MyLinkedList list){
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
    public static void writeListToFile(MyLinkedList list,String filename){
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


    public static void main(String[] args) throws Exception {
        int n = 40;//numar fisiere
        int p = 4; // numar threaduri
        int gradMaxim = 100000;
        int nrMonoame = 1000;
        //for(int i=1;i<=n;i++)
        //    writeToFile("polinom"+Integer.toString(i)+".txt",gradMaxim,nrMonoame);


        /////////////////////////////// PARALEL ////////////////////////////////////////
        MyLinkedList list = new MyLinkedList();
        MyQueue queue = new MyQueue();

        Worker[] workers = new Worker[p];
        for (int i=0;i<p;i++)
            workers[i] = new Worker(queue,list);

        for(int i=0;i<p;i++)
            workers[i].start();

        long start = System.currentTimeMillis();
        for(int i=1;i<=n;i++){
            File file = new File("polinom"+Integer.toString(i)+".txt");
            Scanner reader = new Scanner(file);
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
        queue.add(new Node(-1,-1));
        for(int i=0;i<p;i++) {
            workers[i].join();
        }
        long end = System.currentTimeMillis();
        System.out.println("Paralel : "+(end-start));
        writeListToFile(list,"Paralel.txt");

        /////////////////////////////////// SECVENTIAL ////////////////////////////
        start = System.currentTimeMillis();
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
                    list.add(node);
                }
            }
        }
        end = System.currentTimeMillis();
        System.out.println("Secvential : "+(end-start));
        writeListToFile(list,"Secvential.txt");
        boolean equal = Utils.checkIfFilesAreEqual("Secvential.txt","Paralel.txt");
        System.out.println("checking if Secvential.txt and Paralel.txt are equal...");
        System.out.println(equal);


    }


}
