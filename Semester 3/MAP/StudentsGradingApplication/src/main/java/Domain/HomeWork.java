package Domain;
import Main.SaptamanaCurenta;

public class HomeWork extends Entity<Integer> {
    private String descriere;
    private int startWeek;
    private int deadlineWeek;

    public HomeWork(int id, String descriere, int startWeek, int deadlineWeek) {
        super.setId(id);
        this.descriere = descriere;
        this.startWeek = startWeek;
        this.deadlineWeek = deadlineWeek;
    }

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    public int getStartWeek() {
        return startWeek;
    }

    public void setStartWeek(int startWeek) {
        this.startWeek = startWeek;
    }

    public int getDeadlineWeek() {
        return deadlineWeek;
    }

    public void setDeadlineWeek(int deadlineWeek) throws IllegalArgumentException{
        if(deadlineWeek<SaptamanaCurenta.getCurent())
            throw new IllegalArgumentException();
        this.deadlineWeek = deadlineWeek;
    }
}
