package app.model;

import java.io.Serializable;

public class User implements Entity<String>, Serializable {
    private String id;
    private String password;
    public User(){

    }
    public User(String username, String password) {
        id = username;
        this.password = password;
    }
    public String getPassword(){
        return password;
    }
    public void setId(String id){
        this.id = id;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getId() {
        return id;
    }
}
