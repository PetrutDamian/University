import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Repository {
    private final Lock lock = new ReentrantLock();
    private Sala sala;
    public List<SpectacolDTO> getDTO(){
        return sala.getDTO();
    }

    public Repository() {
        LoadFromFile();
    }

    public boolean sellTickets(String idSpectacol,Vanzare vanzare){
        lock.lock();
        if(sala.trySellTickets(idSpectacol,vanzare))
        {
            appendVanzareToFile(idSpectacol,vanzare);
            writeSalaToFile();
            writeSpectacoleToFile();
            lock.unlock();
            return true;
        }
        lock.unlock();
        return false;
    }
    public void checkIntegrity(){
        lock.lock();
        System.out.println("checking integrity");
        List<Spectacol> spectacole = sala.spectacole;
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("verificari.txt",true));
            for (int i = 0; i < spectacole.size(); i++) {
                bw.append(spectacole.get(i).ID).append(",");
                bw.append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))).append(",");
                boolean valid = checkSpectacolIntegrity(spectacole.get(i));
                if(valid)
                    bw.append("corect\n");
                else
                    bw.append("incorect\n");
            }
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        lock.unlock();
    }

    private boolean checkSpectacolIntegrity(Spectacol spectacol) {
        int sold = spectacol.getSold();
        List<Vanzare> vanzari = spectacol.getVanzari();
        int soldVanzari = 0;
        if(vanzari.size()!=0)
            soldVanzari = vanzari
                .stream()
                .map(Vanzare::getSuma)
                .reduce(Integer::sum)
                .get();
        if(sold!=soldVanzari)
            return false;

        int nrLocuri = sala.getNr_locuri();
        int[] locuri = new int[nrLocuri+1];
        int locuriDinVanzari = 0;
        for (Vanzare vanzare : vanzari) {
            locuriDinVanzari += vanzare.getListaLocuri().size();
            vanzare.getListaLocuri().forEach(v -> locuri[v]++);
        }
        List<Integer> seatsSold = spectacol.getSeatsSold();
        if(seatsSold.size()!=locuriDinVanzari)
            return false;

        for (Integer loc : seatsSold) {
            if (locuri[loc] != 1)
                return false;
        }
        return true;
    }

    public void writeSpectacoleToFile() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("spectacole.txt"));
            int cnt = 0;
            for (Spectacol s : sala.spectacole){
                /// scrie id, data, titlu, pret
                bw.write(s.ID + "," + s.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + ","
                            + s.getTitle() + "," + s.getPrice() + "\n");

                int idx = 0;
                /// scrie locurile vandute
                for (Integer i : s.getSeatsSold()){
                    bw.write(i.toString());
                    if (idx < s.getSeatsSold().size() - 1)
                        bw.write(",");
                    idx ++;
                }
                /// scrie soldu'
                bw.write("\n" +  s.getSold());
                if (cnt < sala.spectacole.size() - 1)
                    bw.write("\n");
                cnt ++;
            }
            bw.flush();
            bw.close();

        }catch(Exception e){

        }
    }

    public void writeSalaToFile() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("sala.txt"));
            bw.write(sala.getNr_locuri()+"\n");
            int idx = 0;
            for (Spectacol s : sala.spectacole){
                bw.write( s.ID ) ;
                if (idx < sala.spectacole.size()-1)
                    bw.write(",");
                idx++;
            }
            bw.write("\n");
            for (Spectacol s : sala.spectacole){
                int i = 0;
                for (Vanzare v : s.getVanzari()){
                    bw.write(s.ID + "," + v.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) );
                    if (i<s.getVanzari().size()-1)
                        bw.write(";");
                    i++;
                }
                bw.write("\n");
            }
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void appendVanzareToFile(String idSpectacol, Vanzare vanzare) {
        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter("vanzari.txt", true));
            /// scrie id,data vanzare,nr biliete
            bw.append(idSpectacol);
            bw.append(",");
            bw.append(vanzare.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));bw.append(",");
            bw.append(vanzare.getNrBilete() + "\n");

            ///scrie locurile vandute
            int idx = 0;
            for (Integer i : vanzare.getListaLocuri()){
                bw.append(i+"");
                if (idx < vanzare.getListaLocuri().size() - 1)
                    bw.append(',');
                idx++;
            }
            bw.append("\n"+vanzare.getSuma()+"");
            bw.append("\n");
            bw.flush();
            bw.close();
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }

    public void LoadFromFile() {
        sala = loadSalaFromFile();
        sala.spectacole = loadSpectacoleFromFile();
        loadVanzariFromFile();
    }

    private void loadVanzariFromFile() {
        BufferedReader br;
        try {
            String line = "";
            br = new BufferedReader(new FileReader("vanzari.txt"));
            while ((line = br.readLine()) != null) {
                String[] info = line.split(",");
                LocalDateTime date = LocalDateTime.parse(info[1],
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

                Integer nrBilete = Integer.parseInt(info[2]);
                String[] locuri = br.readLine().split(",");

                List<Integer> locuriVandute = new ArrayList<>();
                for(int i=0;i<nrBilete;i++)
                    locuriVandute.add(Integer.parseInt(locuri[i]));

                Integer suma = Integer.parseInt(br.readLine());
                Vanzare vanzare = new Vanzare(date,nrBilete,locuriVandute,suma);
                sala.trySellTickets(info[0],vanzare);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private List<Spectacol> loadSpectacoleFromFile() {
        BufferedReader br;
        List<Spectacol> spectacole = new ArrayList<>();
        try {
            String line = "";
            br = new BufferedReader(new FileReader("spectacole.txt"));
            while((line = br.readLine())!=null){
                String[] info = line.split(",");
                LocalDateTime date = LocalDateTime.parse(info[1],
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                Integer price = Integer.parseInt(info[3]);
                Spectacol spectacol = new Spectacol(info[0],date,info[2],price);
                spectacole.add(spectacol);
                br.readLine();
                br.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return spectacole;
    }

    private Sala loadSalaFromFile() {
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader("sala.txt"));
            String line = br.readLine();
            Integer nrLocuri = Integer.parseInt(line);
            br.close();
            return new Sala(nrLocuri);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
