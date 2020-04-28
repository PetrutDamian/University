package Repositories;
import java.util.ArrayList;
import java.util.Collection;

import Domain.Student;
import Validation.StudentValidator;
import Validation.ValidationException;
import Validation.Validator;

public class StudentRepository extends AbstractRepository<Integer,Student>{
    public StudentRepository(Validator<Student> validator) {
        super(validator);
    }

}
