package Automat;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;


public class AF {
    private HashMap<String, State> states = new HashMap<>();
    private String startState;
    private String[] in ;

    public void assembleFromFile(String filename) throws IOException {
        File file = new File(filename);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        line = reader.readLine();
        String[] stari = line.split("\\|");
        startState = stari[0];

        //alfabet stari
        if(!stari[1].equals("")){
            String[] stariIntermediare = stari[1].split(",",0);
            for (String value : stariIntermediare) {
                states.put(value, new State(value, false));
            }
        }

        String[] stariFinale= stari[2].split(",");
        for (String s : stariFinale) {
            states.put(s, new State(s, true));
        }

        //alfabet intrare
        line = reader.readLine();
        String[] in = line.split(",");
        for(int i=0;i<in.length;i++)
            if(in[i].equals("`"))
                in[i] = ",";
        if(in.length == 0)
            this.in = new String[]{line};
        else
            this.in = in;

        //tranzitii
        while((line=reader.readLine())!=null){
            String[] parts = line.split(",");
            for(int i=0;i<parts.length;i++)
                if(parts[i].equals("`"))
                    parts[i] = ",";
            for(int i=2;i<parts.length;i++)
                states.get(parts[0]).addTranzition(parts[i],states.get(parts[1]));
        }

    }

    public void printInternalStructure() {
        System.out.println("Multimea starilor:");
        System.out.println(startState + " : start");
        states.entrySet().stream().map(s -> s.getKey() + " final : " + s.getValue().isFinal()).forEach(System.out::println);

        System.out.println("Simboluri de intrare:");
        System.out.println(Arrays.toString(in));

        System.out.println("Tranzitii:");
        states.values().forEach(x->{
            System.out.println("Rules for state : "+x.getName());
            x.printTranzitions();
        });
    }

    public boolean trySequence(String sequence) {
        String pre = null;
        try {
            pre = prefix(sequence);
            return true;
        } catch (BadSequenceException e) {
        }
        return false;
    }
    public String prefix(String sequence) throws BadSequenceException {
        State currentState = states.get(startState);
        String prefix = "";
        String longestPrefix = "";

        for (int i = 0; i < sequence.length(); i++) {
            String ch = sequence.substring(i, i + 1);
            if (currentState.tranzitions.containsKey(ch)) {
                currentState = currentState.tranzitions.get(ch);
                prefix += ch;
            } else
                throw new BadSequenceException(longestPrefix);
            if (currentState.isFinal())
                   longestPrefix = prefix;
        }
        if(!currentState.isFinal())
            throw new BadSequenceException(longestPrefix);
        return longestPrefix;
    }
    public String prefix2(String sequence){
        String pre;
        try{
            pre = prefix(sequence);
        } catch (BadSequenceException e) {
            pre = e.getMessage();
        }
        return pre;
    }
}
