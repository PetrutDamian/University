import Automat.BadSequenceException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main {

    private static HashMap<String,Integer> createTable(){
        HashMap<String,Integer> table = new HashMap<>();
        table.put("<iostream>",2);
        table.put("using",3);
        table.put("namespace",4);
        table.put("std",5);
        table.put(";",6);
        table.put("main",7);
        table.put("(",8);
        table.put(")",9);
        table.put("{",10);
        table.put("}",11);
        table.put("#include",12);
        table.put("<",13);
        table.put(">",14);
        table.put("[",15);
        table.put("]",16);
        table.put("int",17);
        table.put("float",18);
        table.put(",",19);
        table.put("=",20);
        table.put("+",21);
        table.put("-",22);
        table.put("*",23);
        table.put("/",24);
        table.put("%",25);
        table.put("cout",26);
        table.put("<<",27);
        table.put("endl",28);
        table.put("cin",29);
        table.put(">>",30);
        table.put("if",31);
        table.put("else",32);
        table.put("&&",33);
        table.put("||",34);
        table.put("==",35);
        table.put("<=",36);
        table.put(">=",37);
        table.put("!=",38);
        table.put("while",39);
        return table;
    }
    private static List<String> parseFile(String filename){
        List<String> elements = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(filename))){
            String line;
            while((line=reader.readLine())!= null)
            {
                String[] words = line.split(" ");
                for(String word:words)
                    if(word.length()>0)
                        elements.add(word);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return elements;
    }



    public static void main(String[] args) throws IOException, BadSequenceException {

        HashMap<String,Integer> table = createTable();

        FIP fip = new FIP(table);

        List<String> list = parseFile("cod.txt");
        System.out.println(list.toString());

        if(fip.generate(list))
            fip.printFIP();
    }
}
