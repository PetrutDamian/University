package Repositories;

import Domain.Entity;
import Validation.ValidationException;
import Validation.Validator;

import java.util.Collection;
import java.util.HashMap;

public abstract class AbstractRepository<ID,E extends Entity<ID>> implements CrudRepository <ID,E> {

    private HashMap<ID,E> elems;
    private Validator<E> validator;
    public AbstractRepository(Validator<E> v){
        elems = new HashMap<ID,E>();
        validator = v;
    }

    @Override
    public E findOne(ID key)throws IllegalArgumentException {
        if (key == null)
            throw new IllegalArgumentException();
        return elems.get(key);
    }

    @Override
    public Collection<E> findAll() {
        return elems.values();
    }

    @Override
    public E save(E entity) throws IllegalArgumentException, ValidationException{
        if (entity == null)
            throw new IllegalArgumentException();
        if(elems.containsKey(entity.getId()))
            return entity;
        validator.validate(entity);
        elems.put(entity.getId(),entity);
        return null;
    }

    @Override
    public E delete(ID key)throws IllegalArgumentException {
        if(key == null)
            throw new IllegalArgumentException();
        return elems.remove(key);
    }

    @Override
    public E update(E entity)throws IllegalArgumentException,ValidationException {
        if(entity == null)
            throw new IllegalArgumentException();
        validator.validate(entity);
        if(elems.containsKey(entity.getId())){
            elems.put(entity.getId(),entity);
            return null;
        }
        return entity;
    }
    public int size(){
        return elems.size();
    }
}
