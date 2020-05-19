package rest.client;

import app.model.Cursa;
import app.model.Cursa2;
import app.services.MyList;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public class CurseClient {
    public static final String URL = "http://localhost:8080/mpp/curse";
    private RestTemplate restTemplate = new RestTemplate();
    private <T> T execute(Callable<T> callable) {
        try {
            return callable.call();
        } catch (Exception e) { //
            System.out.println("Error encountered in execute" + e.getMessage());
        }
        return null;
    }
    public List<Cursa> findAll(){
        return execute(()->restTemplate.getForObject(URL, MyList.class)).curse.stream().
                map(Cursa::new).collect(Collectors.toList());
    }
    public Cursa findOne(Integer key){
        Cursa2 found = execute(()->restTemplate.getForObject(String.format("%s/%s",URL,key), Cursa2.class));
        if (found==null)
            return null;
        else
            return new Cursa(found);
    }
    public void update(Cursa cursa){
        execute(()->{
            restTemplate.put(String.format("%s/%s",URL,cursa.getId().toString()),new Cursa2(cursa));
            return null;
        });
    }
    public void delete(Integer key){
        execute(()->{
            restTemplate.delete(String.format("%s/%s",URL,key));
            return null;
        });
    }
    public Cursa2 save(Cursa cursa){
        return execute(()->
            restTemplate.postForObject(URL,new Cursa2(cursa),Cursa2.class)
        );
    }
}
