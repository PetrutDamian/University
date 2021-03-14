import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Node {
    public volatile Integer c;
    public volatile Integer exp;
    public volatile Node next ;
    public volatile Lock lock = new ReentrantLock();

    public Node(){
        this.c = null;
        this.exp = null;
        this.next = null;
    }
    public Node(Integer c, Integer exp){
        this.c = c;
        this.exp = exp;
        this.next = null;
    }
}
