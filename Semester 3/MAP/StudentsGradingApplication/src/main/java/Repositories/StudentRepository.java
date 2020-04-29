package Repositories;

import Domain.Student;
import Validation.Validator;

public class StudentRepository extends AbstractRepository<Integer,Student> {
    public StudentRepository(Validator<Student> validator) {
        super(validator);
    }

}
