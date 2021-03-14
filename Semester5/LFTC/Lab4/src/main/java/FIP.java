import Automat.AF;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FIP {
    private HashMap<String,Integer> symbols;
    private TS ts = new TS();
    private AF afID = new AF();
    private AF afLiterals = new AF();
    private AF afReserved = new AF();
    private AF afOperators = new AF();

    public FIP(HashMap<String,Integer> map1) throws IOException {
        afID.assembleFromFile("af_id.txt");
        afLiterals.assembleFromFile("af_literals.txt");
        afReserved.assembleFromFile("af_reserved.txt");
        afOperators.assembleFromFile("af_operators.txt");
        symbols = map1;
    }

    private List<Pair<String, Pair<Integer,Integer>>>  elems = new ArrayList<>();

    public boolean generate(List<String> list) {
        String error = "";
        for(String el:list){
            System.out.println(el);
            List<String> all = new ArrayList<>();
            all.add(el);
            int type = 0;
            int size = 0;
            int prefix = 0;
            while(all.size()>0 && error.length()==0){
                String crt =all.get(0);
                type = 0;size=0;
                prefix = afReserved.prefix2(crt).length();
                if(prefix>size){
                    size = prefix;
                    type = 1;
                }
                prefix = afOperators.prefix2(crt).length();
                if(prefix>size){
                    size = prefix;
                    type = 2;
                }
                prefix = afID.prefix2(crt).length();
                if(prefix>size){
                    size = prefix;
                    type = 3;
                }
                prefix = afLiterals.prefix2(crt).length();
                if(prefix>size){
                    size = prefix;
                    type = 4;
                }
                if(size==crt.length()){
                    switch (type){
                        case 1:
                        case 2:
                            elems.add(new Pair<>(crt,new Pair<>(symbols.get(crt),-1)));
                            break;
                        default:
                            Integer codTS = ts.add(crt);
                            elems.add(new Pair<>(crt,new Pair<>(type==3?1:2,codTS)));
                            break;
                    }
                    all.remove(0);
                    continue;
                }
                if(size == 0)
                    error = crt;
                else {
                    String rest = crt.substring(size);
                    String pref = crt.substring(0,size);
                    switch (type){
                        case 3:
                        case 4:
                            if(afOperators.prefix2(rest).length() != 0) {
                                Integer code = ts.add(pref);
                                elems.add(new Pair<>(pref,new Pair<>(type==3?1:2,code)));
                                all.remove(0);
                                all.add(0,rest);
                            }
                            else
                                error= crt;
                            break;
                        case 2:
                            if(afID.prefix2(rest).length()!=0 || afLiterals.prefix2(rest).length()!=0){
                                elems.add(new Pair<>(pref,new Pair<>(symbols.get(pref),-1)));
                                all.remove(0);
                                all.add(0,rest);
                            }
                            else
                                error = crt;
                            break;
                        case 1:
                            if(afOperators.prefix2(rest).equals(";")
                                    || afOperators.prefix2(rest).equals("(")
                                    || afOperators.prefix2(rest).equals("<<")
                                    || afOperators.prefix2(rest).equals(">>")
                            ){
                               elems.add(new Pair<>(pref,new Pair<>(symbols.get(pref),-1)));
                               all.remove(0);
                               all.add(0,rest);
                            }else
                                error = crt;
                            break;
                    }
                }
            }
            if (error.length()>0)
            {
                System.out.println("Eroare lexicala la :"+error);
                return false;
            }
        }
        sync();
        return true;
    }

    private void sync() {
        elems.forEach(x->{
            if(x.getSecond().getSecond()!=-1){
                x.getSecond().setSecond(ts.add(x.getFirst()));
            }
        });
    }

    public void printFIP(){
       elems
               .stream()
               .map(x-> "{Simbol:" + x.getFirst() + ",CodAtom:" + x.getSecond().getFirst() + ",CodTS:" + x.getSecond().getSecond())
               .forEach(System.out::println);
    }
}
