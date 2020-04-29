package Repositories;

import Domain.HomeWork;
import Validation.Validator;

public class HomeWorkRepository extends AbstractRepository<Integer, HomeWork> {
    public HomeWorkRepository(Validator<HomeWork> validator) {
        super(validator);
    }

}
