import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Grammar {
    Set<String> terminals = new HashSet<>();
    Set<String> nonterminals = new HashSet<>();
    HashMap<String, ArrayList<ArrayList<String>>> productions = new HashMap<>();
    ArrayList<String> fip = new ArrayList<>();

    public Grammar(){}
    public Grammar(String filename) throws IOException {
        File file = new File(filename);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] args = line.split(":");
            fip.add(args[1]);
        }
    }
    public boolean assembleFromFile(String filename) throws IOException {
        File file = new File(filename);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        String patternString = "";
        for(int i=0;i<fip.size();i++) {
            String crt = fip.get(i);
            switch (crt) {
                case ".":
                    crt = "\\.";
                    break;
                case "(":
                    crt = "\\(";
                    break;
                case ")":
                    crt = "\\)";
                    break;
                case "[":
                    crt = "\\[";
                    break;
                case "{":
                    crt = "\\{";
                    break;
                case "+":
                    crt = "\\+";
                    break;
                case "*":
                    crt = "\\*";
                    break;
            }
            patternString = patternString  +crt+ "|";
        }
        patternString = patternString.substring(0, patternString.length() - 1);
        Pattern fipPattern = Pattern.compile(patternString);

        while ((line = reader.readLine()) != null) {
            if (line.equals(""))
                continue;
            String[] components = line.split("->");
            String left = components[0].replaceAll("'","");
            String right = components[1];
            nonterminals.add(left);
            if(!productions.containsKey(left))
                productions.put(left, new ArrayList<>());

            Pattern nonTerminalPattern  =Pattern.compile("([A-Z].*?')|([A-Z])");
            String[] rules = right.split("\\|");
            for(String rule:rules){
                ArrayList<ArrayList<String>> prod = productions.get(left);
                prod.add(new ArrayList<>());
                for(int j=0;j<rule.length();j++){
                    String c = rule.substring(j,j+1);
                    if(c.matches("[A-Z]")) {
                        Matcher matcher = nonTerminalPattern.matcher(rule.substring(j));
                        matcher.find();
                        String nonTerminal = matcher.group();
                        j += nonTerminal.length() -1;
                        nonTerminal = nonTerminal.replaceAll("'","");
                        nonterminals.add(nonTerminal);
                        prod.get(prod.size()-1).add(nonTerminal);
                    }
                    else {
                        Matcher matcher = fipPattern.matcher(rule.substring(j));
                        boolean found = matcher.find();
                        if(found){
                            String terminal = matcher.group();
                            if(!rule.startsWith(terminal, j))
                            {
                                c = c.replaceAll("~","\n");
                                terminals.add(c);
                                prod.get(prod.size()-1).add(c);
                            }
                            else{
                                j+=terminal.length() -1;
                                terminals.add(terminal);
                                prod.get(prod.size()-1).add(terminal);
                            }
                        }else{
                            c = c.replaceAll("~","\n");
                            terminals.add(c);
                            prod.get(prod.size()-1).add(c);
                        }
                    }
                    }
                }
            }
        return true;
        }

    public Set<String> getNonterminals(){
        return nonterminals;
    }
    public Set<String> getTerminals(){
        return terminals;
    }
    public HashMap<String,ArrayList<ArrayList<String>>> getProductions(){
        return productions;
    }
    public ArrayList<String> getProductionsByTerminal(String terminal){
        ArrayList<String> prods = new ArrayList<>();
        for(Map.Entry<String, ArrayList<ArrayList<String>>> s : productions.entrySet()){
            String left = s.getKey();
            ArrayList<ArrayList<String>> right = s.getValue();
            right.forEach(p->{
                if(p.contains(terminal)){
                    String r = p.stream().reduce((t1,t2)->t1+t2).get();
                    prods.add(left+"->"+r);
            }}
            );
        };
        return prods;
    }
}
