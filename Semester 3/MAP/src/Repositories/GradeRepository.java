package Repositories;

import Domain.Grade;
import Validation.ValidationException;
import Validation.Validator;

import java.util.Collection;

public class GradeRepository extends AbstractRepository<String, Grade> {
    public GradeRepository(Validator<Grade> v) {
        super(v);
    }
}
