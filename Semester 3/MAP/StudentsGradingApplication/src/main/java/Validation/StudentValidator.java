package Validation;

import Domain.Student;

public class StudentValidator implements Validator<Student> {

    public StudentValidator(){ }
    public void validate(Student st) throws ValidationException {
        String errors = "";
        if (st.getId() <=0)
            throw new ValidationException("Id invalid!\n");
    }
}
