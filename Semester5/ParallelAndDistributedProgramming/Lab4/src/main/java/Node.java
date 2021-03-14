public class Node {
    public Integer c;
    public Integer exp;
    public Node next;
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
