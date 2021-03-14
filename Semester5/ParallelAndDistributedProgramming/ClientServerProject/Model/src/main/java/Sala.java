import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Sala {
    private int nrLocuri;
    public List<Spectacol> spectacole = new ArrayList<>();

    public List<SpectacolDTO> getDTO(){
       List<SpectacolDTO> spectacole = new ArrayList<>();
       this.spectacole.forEach(s->{
           List<Integer> locuriDisponibile = new ArrayList<>();
           for(int i=1;i<=nrLocuri;i++) {
               int finalI = i;
               if(s.getSeatsSold().stream().noneMatch(loc->loc.equals(finalI)))
                   locuriDisponibile.add(i);
           }
           spectacole.add(new SpectacolDTO(s.ID,locuriDisponibile));
       });
       return spectacole;
    }

    public Sala(){}
    public Sala(Integer nrLocuri,List<Spectacol> spectacole) {
        this.nrLocuri = nrLocuri;
        this.spectacole = spectacole;
    }
    public Sala(Integer nrLocuri){
        this.nrLocuri = nrLocuri;
    }
    public boolean trySellTickets(String idSpectacol,Vanzare vanzare){
        return sellTickets(idSpectacol,vanzare);
    }


    private boolean sellTickets(String idSpectacol,Vanzare vanzare){
        for(int i=0;i<spectacole.size();i++)
            if(spectacole.get(i).ID.equals(idSpectacol))
               return spectacole.get(i).sellTickets(vanzare);
        return  false;
    }

    public int getNr_locuri() {
        return nrLocuri;
    }

    public void setNr_locuri(int nrLocuri) {
        this.nrLocuri = nrLocuri;
    }
}
