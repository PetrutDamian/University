public class MyLinkedList {
    public Node head = null;

    public synchronized void add(Node node){
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
}
