import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Analyzer {
    private Grammar grammar;
    public ArrayList<String> istoric;
    private ArrayList<String> sequence = new ArrayList<>();
    private Pattern pattern ;
    public Analyzer(Grammar gr){
        grammar = gr;
        List<String> terminals = gr.getTerminals().stream().sorted((a, b)->{
            if(a.length()>b.length())
                return -1;
            if (a.length()==b.length())
                return 0;
            return 1;
        }).collect(Collectors.toList());
        String patternString = "";
        for(int i=0;i<terminals.size();i++)
            patternString = patternString + "(" + Pattern.quote(terminals.get(i)) +")" + "|";
        patternString = patternString.substring(0,patternString.length()-1);
        Pattern pattern = Pattern.compile(patternString);
        this.pattern = pattern;
    }

    private ArrayList<String> copyList(ArrayList<String> list){
        return (ArrayList<String>) list.clone();
    }

    public boolean acceptSequence(String filename) throws IOException {
        String sequence = getSequenceFromFile(filename);
        int size = 0;
        Matcher matcher = pattern.matcher(sequence);
        while(true){
            boolean found = matcher.find();
            if(found){
                String matched = matcher.group();
                size +=matched.length();
                this.sequence.add(matched);
            }
            else{
                if(size!=sequence.length())
                    return false;
                ArrayList<String> start = new ArrayList<>();
                start.add("S");
                return trySequence(0,new ArrayList<>(),start);
            }
        }
    }

    private String getSequenceFromFile(String filename) throws IOException {
        File file = new File(filename);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        String sequence = "";
        while ((line = reader.readLine()) != null) {
            if (line.equals(""))
                continue;
            sequence += line + "\n";
        }
        return sequence;
    }

    private boolean trySequence( Integer poz, ArrayList<String> istoric, ArrayList<String> band){
        if(poz == sequence.size()) {
                if(band.size()==0){
                    this.istoric = istoric;
                    return true; //success
                }
                return false;
        }
        if(band.size() == 0)
            return false;
        String crt = band.get(0);
        band.remove(0);
        if(sequence.get(poz).equals(crt))
            return trySequence(poz+1,istoric,band); //avans
        if(grammar.nonterminals.contains(crt)){
            //expandare
            var productions = grammar.getProductions();
            var  prods = productions.get(crt);
            if(prods == null)
                return false;
            for(int i=0;i<prods.size();i++){
                var next = copyList(prods.get(i));
                next.addAll(band);
                istoric.add(crt+"->"+ String.join("", prods.get(i)));
                boolean accepted = trySequence(poz,istoric,next);
                if(accepted)
                    return true;
                //insuccess
                istoric.remove(istoric.size()-1);
            }
        }
        return false;
    }

}
