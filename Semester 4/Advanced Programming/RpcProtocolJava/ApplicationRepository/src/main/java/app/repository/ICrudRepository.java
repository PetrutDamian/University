package app.repository;

import app.model.Entity;

import java.util.List;

public interface ICrudRepository<ID, E extends Entity<ID>> {
    void save(E entity);
    void delete(ID id);
    void update(E entity);
    E findOne(ID id);
    List<E> findAll();
}