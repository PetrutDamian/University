package app.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Cursa2 implements Entity<Integer>, Serializable {
    private Integer id;
    private String destinatie;
    private String date;
    private Integer locuriDisponibile=18;
    public Cursa2(){
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

    public Cursa2(Integer integer, String destinatie, String date) {
        this.id = integer;
        this.destinatie = destinatie;
        this.date = date;
        this.locuriDisponibile=18;
    }
    public Cursa2(Integer integer, String destinatie, String date,Integer nrLocuri) {
        this.id = integer;
        this.destinatie = destinatie;
        this.date = date;
        this.locuriDisponibile = nrLocuri;
    }
    public Cursa2(String destinatie, String date,Integer nrLocuri) {
        this.destinatie = destinatie;
        this.date = date;
        this.locuriDisponibile = nrLocuri;
    }
    public Cursa2(Cursa cursa){
        this.id = cursa.getId();
        this.destinatie = cursa.getDestinatie();
        this.locuriDisponibile = cursa.getLocuriDisponibile();
        this.date = cursa.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
    public void decrementSeats(int nr){
        this.locuriDisponibile-=nr;
    }

    public String getDestinatie() {
        return destinatie;
    }

    public String getDate() {
        return date;
    }

    public Integer getLocuriDisponibile() {
        return locuriDisponibile;
    }

    public void setDestinatie(String destinatie) {
        this.destinatie = destinatie;
    }

    public void setDate(String date) {
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
