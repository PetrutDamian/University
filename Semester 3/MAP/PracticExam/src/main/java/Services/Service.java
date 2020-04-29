package Services;

import Domain.MembruActiv;
import Domain.Mesaj;
import ObserverPattern.EventTypes;
import ObserverPattern.Observable;
import ObserverPattern.Observer;
import Repository.RepoMembrii;
import Repository.RepoMesaje;
import Utils.ActiveType;
import Utils.MemberType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Service implements Observable {
    RepoMembrii repoMembrii;
    RepoMesaje repoMesaje;
    List<Observer> obsList= new ArrayList<Observer>();
    public Service(RepoMembrii repoMembrii,RepoMesaje repoMesaje){
        this.repoMembrii=repoMembrii;
        this.repoMesaje=repoMesaje;
    }
    public void saveAll(String from,String content){
        for(MembruActiv m:repoMembrii.getAll()){
            if(m.getName()!=from){
                save(from,m.getName(),content);
            }
        }
    }
    public void save(String from,String to,String content){
        Mesaj m = new Mesaj(from,to,content, LocalDateTime.now());
        repoMesaje.save(m);
        notifyObservers(EventTypes.MESSAGE);
    }
    public List<Mesaj> getAllMessages(){return repoMesaje.getAll();}
    public List<MembruActiv> getAllMembers(){
        return repoMembrii.getAll();
    }

    @Override
    public void addObserver(Observer obs) {
        obsList.add(obs);
    }
    @Override
    public void removeObserver(Observer obs) {
        obsList.remove(obs);
    }

    public void retrag(String name,boolean value){
        for(MembruActiv m:repoMembrii.getAll())
        {
            if(m.getName().equals(name))
            {
                if(value)
                m.setActiveType(ActiveType.INACTIVE);
                else
                    m.setActiveType(ActiveType.ACTIVE);
            }
        }
        notifyObservers(EventTypes.MEMBER);
    }
    public boolean retrasi(){
        for(MembruActiv m:repoMembrii.getAll())
            if(m.getType().equals(MemberType.MEMBRU) && m.getActiveType().equals(ActiveType.ACTIVE))
                return false;
        return true;
    }
    @Override
    public void notifyObservers(EventTypes e) {
        for(Observer ob:obsList){
            ob.update(e);
        }
    }
}
