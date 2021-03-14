import java.time.LocalDateTime;
import java.util.List;

public class Vanzare {
    private LocalDateTime date;
    private  int nrBilete;
    private List<Integer> listaLocuri;
    private int suma;

    public Vanzare(LocalDateTime date, int nrBilete,List<Integer> listaLocuri,Integer suma) {
        this.date = date;
        this.nrBilete = nrBilete;
        this.listaLocuri = listaLocuri;
        this.suma =suma;
    }
    public Vanzare(LocalDateTime date, int nrBilete,List<Integer> listaLocuri) {
        this.date = date;
        this.nrBilete = nrBilete;
        this.listaLocuri = listaLocuri;
    }


    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public int getNrBilete() {
        return nrBilete;
    }

    public void setNrBilete(int nrBilete) {
        this.nrBilete = nrBilete;
    }

    public List<Integer> getListaLocuri() {
        return listaLocuri;
    }

    public void setListaLocuri(List<Integer> listaLocuri) {
        this.listaLocuri = listaLocuri;
    }

    public int getSuma() {
        return suma;
    }

    public void setSuma(int suma) {
        this.suma = suma;
    }

    @Override
    public String toString() {
        return "Vanzare{" +
                ", date=" + date +
                ", nrBilete=" + nrBilete +
                ", listaLocuri=" + listaLocuri +
                ", suma=" + suma +
                '}';
    }
}
