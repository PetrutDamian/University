package Repositories;

import Domain.HomeWork;
import Validation.ValidationException;
import Validation.Validator;

import java.io.IOException;
import java.util.Collection;

public class HomeWorkRepository extends AbstractRepository<Integer, HomeWork> {
    public HomeWorkRepository(Validator<HomeWork> validator) {
        super(validator);
    }

}
