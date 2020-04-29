package Repositories;

import Domain.Entity;
import Validation.ValidationException;
import Validation.Validator;


public abstract class AbstractFileRepository<ID,E extends Entity<ID>> extends AbstractRepository<ID,E> {
    protected String filename;
    public AbstractFileRepository(String filename, Validator<E> val) throws  ValidationException {
        super(val);
        this.filename=filename;
        loadFromFile(filename);
    }
    protected abstract void writeToFile(String filename);
    protected abstract void loadFromFile(String filename)throws  ValidationException;
    @Override
    public E save(E entity) throws IllegalArgumentException, ValidationException {
        E result = super.save(entity);
        if(result == null)
            this.writeToFile(filename);
        return result;
    }
    public E saveNoWrite(E entity) throws ValidationException {
        return super.save(entity);
    }
    @Override
    public E delete(ID key) throws IllegalArgumentException{
        E result = super.delete(key);
        this.writeToFile(filename);
        return result;
    }

    @Override
    public E update(E entity) throws IllegalArgumentException, ValidationException {
        E result = super.update(entity);
        if(result == null)
            this.writeToFile(filename);
        return result;
    }
}
