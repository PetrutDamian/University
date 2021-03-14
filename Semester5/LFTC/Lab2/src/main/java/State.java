import java.util.HashMap;


public class State {
    private String name;
    public HashMap<String,State> tranzitions = new HashMap<>();
    public boolean isFinal;

    public State(String name, boolean isFinal) {
        this.name = name;
        this.isFinal = isFinal;
    }
    public String getName(){
        return name;
    }

    public boolean isFinal(){
        return isFinal;
    }

    public void addTranzition(String input,State state){
        tranzitions.put(input,state);
    }

    public void printTranzitions(){
        tranzitions.forEach((key, value) -> {
            System.out.println("in:"+key+ " ->"+value.getName());
        });
    }
}



