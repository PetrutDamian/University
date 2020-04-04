import app.network.server.AbstractServer;
import app.network.server.RemoteConcurrentServer;
import app.server.Service;
import app.services.IServices;
import app.services.ServiceException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.util.Properties;

public class StartRpcServer {
    private static int defaultPort =55556;
    public static void main(String[] args) {
        Properties props = new Properties();
        try {
            props.load(StartRpcServer.class.getResourceAsStream("/server.properties"));
        } catch (IOException e) {
            System.out.println("Error: Can't find server properties "+e);
        }
        ApplicationContext factory  = new ClassPathXmlApplicationContext("springConfig.xml");
        IServices service = factory.getBean(Service.class);
        int serverPort = defaultPort;
        try{
            serverPort = Integer.parseInt(props.getProperty("server.port"));
        }catch (NumberFormatException ex){
            System.err.println("Wrong  Port Number"+ex.getMessage());
            System.err.println("Using default port "+defaultPort);
        }
        System.out.println("Starting server on port: "+serverPort);
        AbstractServer server =new RemoteConcurrentServer(serverPort,service);
        try{
            server.start();
        }catch (ServiceException ex){
            System.out.println("Error starting the server "+ex.getMessage());
        }finally {
            try{
                server.stop();
            }catch (ServiceException ex){
                System.out.println("Error stopping the server "+ex.getMessage());
            }
        }
    }
}
