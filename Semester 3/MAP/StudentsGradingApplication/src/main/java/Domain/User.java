package Domain;

public class User extends Entity<String> {
    private String password;
    private String privilege;
    public User(String username,String password,String privilege){
        super.setId(username);
        this.password=password;
        this.privilege=privilege;
    }
    public String getPassword(){return password;}
    public String getPrivilege(){return privilege;}
}
