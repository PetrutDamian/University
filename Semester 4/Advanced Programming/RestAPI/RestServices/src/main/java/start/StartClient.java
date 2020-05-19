package start;

import app.model.Cursa;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import rest.client.CurseClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StartClient {
    private final static CurseClient curseClient =new CurseClient();

    private static void showMessage(){
        System.out.println("1:findAll\n2:FindOne(id)\n3:DeleteOne(id)"+
                "\n4:Save(Destinatie)\n5:Update(Id,Destinatie(yyyy-...)|Date|Locuri");
    }
    public static void main(String[] args) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while(true){
            showMessage();
            Integer cmd = Integer.parseInt(reader.readLine());
            switch (cmd){
                case 0:
                    break;
                case 1:
                    show(()->{
                        curseClient.findAll().forEach(System.out::println);
                    });
                    break;
                case 2:
                    System.out.println("id=");
                    Integer id = Integer.parseInt(reader.readLine());
                    show(()->{
                        Cursa c = curseClient.findOne(id);
                        if(c!=null)
                            System.out.println(c);
                        else
                            System.out.println("Id not found");
                    });
                    break;
                case 3:
                    System.out.println("id=");
                    Integer id2 = Integer.parseInt(reader.readLine());
                    show(()->{
                        curseClient.delete(id2);
                    });
                    break;
                case 4:
                    System.out.println("Destinatie:");
                    String dest = reader.readLine();
                    show(()->{
                        System.out.println(curseClient.save(new Cursa(dest, LocalDateTime.now(),18)));
                    });
                    break;
                case 5:
                    System.out.println("id/Destinatie/Date/Locuri");
                    String[] arg = reader.readLine().split("/");
                    Integer id3 = Integer.parseInt(arg[0]);
                    String dest2 = arg[1];
                    LocalDateTime date = LocalDateTime.parse(arg[2], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                    Integer locuri = Integer.parseInt(arg[3]);
                    show(()->{
                        curseClient.update(new Cursa(id3,dest2,date,locuri));
                    });
                    break;
            }

        }
    }

    private static void show(Runnable task){
        task.run();
    }

}
