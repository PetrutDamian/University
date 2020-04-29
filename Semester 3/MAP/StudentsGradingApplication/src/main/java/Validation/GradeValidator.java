package Validation;
import Domain.Grade;

public class GradeValidator implements Validator<Grade> {
    @Override
    public void validate(Grade gr) throws ValidationException {
        String errors = "";
        if(gr.getGrade()<1 || gr.getGrade()>10)
            errors+= "Nota invalida!\n";
        if(gr.getTeacher().equals(""))
            errors+="Teacher invalid!\n";
        if(errors.length()!=0)
            throw new ValidationException(errors);
    }
}
