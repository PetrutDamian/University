import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void printMessage(){
        System.out.println("1:multimea neterminalelor\n2:multimea terminalelor\n3:multimea regulilor de productie\n" +
                "4:multimea regulilor de productie ce contin un anumit terminal\n5:testeaza o secventa\n");
    }
    public static void main(String[] args) throws IOException {
        Grammar gr = new Grammar("fip.txt");
        if(!gr.assembleFromFile("gramatica.txt"))
            return;

        while(true){
            printMessage();
            Integer x;
            Scanner reader = new Scanner(System.in);
            x = Integer.parseInt(reader.nextLine());
            switch (x){
                case 1:
                    System.out.println(gr.getNonterminals());
                    break;
                case 2:
                    System.out.println(gr.getTerminals());
                    break;
                case 3:
                    System.out.println(gr.getProductions());
                    break;
                case 4:
                    String terminal;
                    System.out.println("Introduceti terminalul:");
                    terminal = reader.nextLine();
                    System.out.println(gr.getProductionsByTerminal(terminal));
                    break;
                case 5:
                    String file;
                    System.out.println("Introduceti fisierul ce contine secventa:");
                    file = reader.nextLine();
                    Analyzer analyzer = new Analyzer(gr);
                    boolean accepted= analyzer.acceptSequence(file);
                    System.out.println(accepted);
                    if(accepted)
                    {
                        analyzer.istoric.forEach(System.out::println);
                    }
                    break;

            }
        }
    }
}
