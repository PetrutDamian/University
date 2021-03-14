import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.rmi.RemoteException;

public class Main {

    public static void main(String[] args) throws RemoteException {
        ApplicationContext factory =new ClassPathXmlApplicationContext("classpath:spring.xml");
        IServices server =(IServices)factory.getBean("server");
        Client client = new Client();
        client.setServer(server);
    }

}
