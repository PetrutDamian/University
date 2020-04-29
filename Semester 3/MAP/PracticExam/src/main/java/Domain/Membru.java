package Domain;

import Utils.MemberType;

public class Membru {
    protected String name;
    protected MemberType type;
    public Membru(String name,MemberType type){
        this.name= name;
        this.type = type;
    }
    public String getName(){
        return name;
    }
    public MemberType getType(){
        return type;
    }
}

