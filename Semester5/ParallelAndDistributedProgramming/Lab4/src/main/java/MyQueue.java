import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

public class MyQueue {
    private Queue<Node> q = new LinkedList<>();
    private boolean closed = false;
    public synchronized void add(Node node){
        q.add(node);
        notifyAll();
    }

    public synchronized Node remove() {
        while(q.isEmpty() && !closed) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(closed)
            return  null;
        Node node = q.remove();
        if(node.exp.equals(-1)){
            closed = true;
            return null;
        }
        return node;
    }


}
