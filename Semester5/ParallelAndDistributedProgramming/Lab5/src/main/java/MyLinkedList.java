import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyLinkedList {
    static private final Lock lock = new ReentrantLock();
    public volatile Node head = null;

    public void addNoLock(Node node){
        if(head == null){
            head = node;
            return;
        }
        if(node.exp>head.exp){
            node.next = head;
            head = node;
            return;
        }
        Node crt = head;
        Node previous = head;
        while(crt.exp>node.exp){
            if(crt.next==null)
                break;
            previous = crt;
            crt = crt.next;
        }
        if(crt.exp.equals(node.exp))
            crt.c += node.c;
        else if (crt.exp>node.exp)
            crt.next = node;
        else{
            previous.next = node;
            node.next = crt;
        }
    }

    public  void add(Node node){
        lock.lock();
        //System.out.println(Thread.currentThread().getName() + "node: "+ node.c.toString()+" exp:"+node.exp.toString());
        //System.out.println(Thread.currentThread().getName()+ " locked lock ");
        if(head == null){
            head = node;
            //System.out.println(Thread.currentThread().getName()+ " unlocked lock 1");
            lock.unlock();
            return;
        }
        //System.out.println(Thread.currentThread().getName()+ " unlocked lock 2");

       //System.out.println(Thread.currentThread().getName()+ " locked head "+head.exp.toString());
        if(node.exp>head.exp){// x^61
            node.next = head;
            head = node;
            //System.out.println(Thread.currentThread().getName()+ " unlocked lock 2 ");
            lock.unlock();
            return;
        }
        head.lock.lock();
        lock.unlock();

        Node crt = head;
        Node previous = head;
        while(crt!=null){
                if(crt.exp < node.exp){
                    previous.next = node;
                    node.next = crt;
                    //System.out.println(Thread.currentThread().getName()+ " unlocked previous 1 " +previous.exp.toString());
                    previous.lock.unlock();
                    return;
                }
                crt.lock.lock();
                previous.lock.unlock();
                if(crt.exp.equals(node.exp)){
                    crt.c += node.c;
                   // System.out.println(Thread.currentThread().getName()+ " unlocked previous 2  " +previous.exp.toString());
                    crt.lock.unlock();
                    return;
                }
                if(crt.next==null) {
                    crt.next = node;
                   // System.out.println(Thread.currentThread().getName()+ " unlocked previous 3 " + previous.exp.toString());
                    crt.lock.unlock();
                    return;
                }
                previous = crt;
                crt = crt.next;
                //System.out.println(Thread.currentThread().getName()+ " locked last while "+previous.exp.toString());
                //System.out.println(Thread.currentThread().getName()+ " unlocked last while " + previous2.exp.toString());
        }
    }
}
