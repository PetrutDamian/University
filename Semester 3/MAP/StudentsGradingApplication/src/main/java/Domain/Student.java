package Domain;

public class Student extends Entity<Integer> {
    private String nume;
    private String prenume;
    private int grp;
    private String email;
    private String cadruDidacticIndrumatorLab;

    public Student(int id, String nume, String prenume, int grp, String email, String cadruDidacticIndrumatorLab) {
        super.setId(id);
        this.nume = nume;
        this.prenume = prenume;
        this.grp = grp;
        this.email = email;
        this.cadruDidacticIndrumatorLab = cadruDidacticIndrumatorLab;
    }
    public String getNume() {
        return nume;
    }
    public void setNume(String nume) {
        this.nume = nume;
    }
    public String getPrenume() {
        return prenume;
    }
    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }
    public int getGrp() {
        return grp;
    }
    public void setGrp(int grp) {
        this.grp = grp;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getCadruDidacticIndrumatorLab() {
        return cadruDidacticIndrumatorLab;
    }
    public void setCadruDidacticIndrumatorLab(String cadruDidacticIndrumatorLab) {
        this.cadruDidacticIndrumatorLab = cadruDidacticIndrumatorLab;
    }
}

