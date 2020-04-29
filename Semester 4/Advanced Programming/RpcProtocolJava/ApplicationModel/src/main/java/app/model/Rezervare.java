package app.model;

public class Rezervare extends Entity<Integer>{
    private Integer idCursa;
    private Integer nrLoc;
    private String client;

    public Rezervare(Integer integer, Integer idCursa, Integer nrLoc, String client) {
        super(integer);
        this.idCursa = idCursa;
        this.nrLoc = nrLoc;
        this.client = client;
    }

    public Integer getIdCursa() {
        return idCursa;
    }

    public Integer getNrLoc() {
        return nrLoc;
    }

    public String getClient() {
        return client;
    }
}
