package Validation;
import Domain.HomeWork;

public class HomeWorkValidator implements Validator<HomeWork> {

    public HomeWorkValidator(){}
    @Override
    public void validate(HomeWork hm) throws ValidationException {
        String errors = "";
        if (hm.getStartWeek() < 1 || hm.getStartWeek() > 14 || hm.getDeadlineWeek() < 1 || hm.getDeadlineWeek() > 14)
            errors+="Saptamana invalida!\n";
        if(hm.getStartWeek() > hm.getDeadlineWeek())
            errors+="Start week > deadline week!\n";
        if(errors.length()>0)
            throw new ValidationException(errors);
    }

}
