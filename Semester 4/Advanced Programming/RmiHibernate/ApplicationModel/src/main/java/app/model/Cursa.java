package app.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Cursa implements Entity<Integer>, Serializable {
    private Integer id;
    private String destinatie;
    private LocalDateTime date;
    private Integer locuriDisponibile=18;
    public Cursa(){
        super();
    }
    @Override
    public String toString() {
        return "Cursa{" +
                "destinatie='" + destinatie + '\'' +
                ", date=" + date +
                ", locuriDisponibile=" + locuriDisponibile +
                ", id=" + getId() +
                '}';
    }

    public Cursa(Integer integer, String destinatie, LocalDateTime date) {
        this.id = integer;
        this.destinatie = destinatie;
        this.date = date;
        this.locuriDisponibile=18;
    }
    public Cursa(Integer integer, String destinatie, LocalDateTime date,Integer nrLocuri) {
        this.id = integer;
        this.destinatie = destinatie;
        this.date = date;
        this.locuriDisponibile = nrLocuri;
    }
    public void decrementSeats(int nr){
        this.locuriDisponibile-=nr;
    }

    public String getDestinatie() {
        return destinatie;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public Integer getLocuriDisponibile() {
        return locuriDisponibile;
    }

    public void setDestinatie(String destinatie) {
        this.destinatie = destinatie;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setLocuriDisponibile(Integer locuriDisponibile) {
        this.locuriDisponibile = locuriDisponibile;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
