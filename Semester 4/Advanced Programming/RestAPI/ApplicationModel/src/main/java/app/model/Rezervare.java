package app.model;

import java.io.Serializable;

public class Rezervare implements Entity<Integer>, Serializable {
    private Integer id;
    private Integer idCursa;
    private Integer nrLoc;
    private String client;
    public Rezervare(){

    }
    public void setId(Integer id){
        this.id = id;
    }
    public Rezervare(Integer integer, Integer idCursa, Integer nrLoc, String client) {
        id = integer;
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

    public void setIdCursa(Integer idCursa) {
        this.idCursa = idCursa;
    }

    public void setNrLoc(Integer nrLoc) {
        this.nrLoc = nrLoc;
    }

    public void setClient(String client) {
        this.client = client;
    }

    @Override
    public Integer getId() {
        return id;
    }
}
