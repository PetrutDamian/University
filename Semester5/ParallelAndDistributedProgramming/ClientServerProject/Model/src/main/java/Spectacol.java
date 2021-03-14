import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Spectacol {
    public String ID;
    private LocalDateTime date;
    private String title;
    private int price;
    private int sold = 0 ;
    private List<Integer> seatsSold = new ArrayList<>();
    private List<Vanzare> vanzari = new ArrayList<>();

    public boolean sellTickets(Vanzare vanzare){
        boolean locuriDejaVandute = vanzare
                        .getListaLocuri()
                        .stream()
                        .anyMatch(l-> seatsSold
                                .stream()
                                .anyMatch(l2-> l2.equals(l)));
        if(locuriDejaVandute)
            return false;
        vanzare.setSuma(price*vanzare.getNrBilete());
        vanzari.add(vanzare);
        seatsSold.addAll(vanzare.getListaLocuri());
        sold+=vanzare.getSuma();
        return true;
    }

    public Spectacol(String ID,LocalDateTime date,String title,Integer price){
        this.ID = ID;
        this.date=date;
        this.title =title;
        this.price = price;
    }
    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getSold() {
        return sold;
    }

    public void setSold(int sold) {
        this.sold = sold;
    }

    public List<Vanzare> getVanzari() {
        return vanzari;
    }

    public List<Integer> getSeatsSold() {
        return seatsSold;
    }

    @Override
    public String toString() {
        return "Spectacol{" +
                "ID=" + ID +
                ", date=" + date +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", sold=" + sold +
                ", seatsSold=" + seatsSold +
                '}';
    }
}
