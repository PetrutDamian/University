package Domain;

import Utils.ActiveType;
import Utils.MemberType;

public class MembruActiv extends Membru {
    private ActiveType activeType;
    public MembruActiv(String name, MemberType type, ActiveType activeType) {
        super(name, type);
        this.activeType=activeType;
    }
    public ActiveType getActiveType(){
        return activeType;
    }
    public void setActiveType(ActiveType type){
        activeType=type;
    }

}
