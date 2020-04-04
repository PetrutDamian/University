package app.repository;

import app.model.Rezervare;

import java.util.List;

public interface IRezervareRepo extends ICrudRepository<Integer, Rezervare> {

    List<Rezervare> findByIdCursa(Integer idCursa);
}

