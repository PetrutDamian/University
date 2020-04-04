package app.model;

import java.time.LocalDateTime;

public class Cursa extends Entity<Integer> {
    private String destinatie;
    private LocalDateTime date;
    private Integer locuriDisponibile=18;

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
        super(integer);
        this.destinatie = destinatie;
        this.date = date;
        this.locuriDisponibile=18;
    }
    public Cursa(Integer integer, String destinatie, LocalDateTime date,Integer nrLocuri) {
        super(integer);
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
}
