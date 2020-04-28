package Repositories;

import Domain.Entity;
import Validation.ValidationException;
import Validation.Validator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilderFactory;

public abstract class AbstractXMLRepository<ID, E extends Entity<ID>> extends AbstractRepository<ID,E> {

    protected String filename;
    protected DocumentBuilderFactory factory;
    public AbstractXMLRepository(String filename, Validator<E> val) throws ValidationException {
        super(val);
        this.filename=filename;
        factory = DocumentBuilderFactory.newInstance();
        loadFromFile(filename);
    }
    protected Node getSubnode(Document doc, String name, String value){
        Element node = doc.createElement(name);
        node.appendChild(doc.createTextNode(value));
        return node;
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
