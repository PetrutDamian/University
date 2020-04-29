package app.model;

import java.io.Serializable;

public class User extends Entity<String> {
    private String password;

    public User(String username, String password) {
        super(username);
        this.password = password;
    }
    public String getPassword(){
        return password;
    }
}
