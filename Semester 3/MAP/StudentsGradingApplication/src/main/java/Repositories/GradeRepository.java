package Repositories;

import Domain.Grade;
import Validation.Validator;

public class GradeRepository extends AbstractRepository<String, Grade> {
    public GradeRepository(Validator<Grade> v) {
        super(v);
    }
}
