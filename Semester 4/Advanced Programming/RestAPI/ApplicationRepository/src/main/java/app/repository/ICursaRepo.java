package app.repository;

import app.model.Cursa;

import java.time.LocalDateTime;
import java.util.List;

public interface ICursaRepo extends ICrudRepository<Integer, Cursa> {
    List<Cursa> findByDestinationAndDate(String Destination, LocalDateTime date);

}
