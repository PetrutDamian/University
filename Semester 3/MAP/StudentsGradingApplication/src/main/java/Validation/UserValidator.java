package Validation;

import Domain.User;

public class UserValidator implements Validator<User> {

    @Override
    public void validate(User u) throws ValidationException {
        String errors = "";
        if(u.getId().equals(""))
            errors +="Id invalid!\n";
        if(u.getPassword().equals(""))
            errors +="Parola invalida!\n";
        if(!(u.getPrivilege().equals("student") || u.getPrivilege().equals("teacher") ||u.getPrivilege().equals("administrator")))
            errors+="Tipul utilizatorului este invalid!\n";
        if(errors.length()!=0)
            throw new ValidationException(errors);
    }
}
