import java.io.*;

public class UI {
    private AF automat = new AF();

    private void printMessage(){
        System.out.println("1:Seteaza automat finit(fisier)\n2:Afiseaza automatul finit\n3:Introduce o secventa\n4:Seteaza automat finit(consola)\n");
    }
    public void run() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true){
            printMessage();
            String cmd = reader.readLine();
            Integer action = Integer.parseInt(cmd);
            switch (action){
                case 1:
                    System.out.println("Filename:");
                    String name = reader.readLine();
                    automat = new AF();
                    automat.assembleFromFile(name);
                    break;
                case 2:
                    automat.printInternalStructure();
                    break;
                case 3:
                    System.out.println("Introduceti secventa:");
                    String sequence = reader.readLine();
                    System.out.println(automat.trySequence(sequence));
                    try {
                        String s = automat.prefix(sequence);
                        System.out.println(s);
                    } catch (BadSequenceException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 4:
                    automat = new AF();
                    String af = "";
                    System.out.println("Introduceti multimea starilor");
                    af += reader.readLine() +"\n";
                    System.out.println("Introduceti alfabetul de intrare:");
                    af+=reader.readLine()+"\n";
                    System.out.println("Introudecti tranzitia sau exit");
                    String line;
                    while(true){
                        line= reader.readLine();
                        if(line.equals("exit"))
                            break;
                        af+=line+"\n";
                    }
                    FileWriter myWriter = new FileWriter("instr.txt");
                    myWriter.write(af);
                    myWriter.flush();
                    myWriter.close();
                    automat.assembleFromFile("instr.txt");
                    break;

            }
        }



    }
}
