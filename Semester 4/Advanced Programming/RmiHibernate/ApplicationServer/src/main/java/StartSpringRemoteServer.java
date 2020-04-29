import app.repository.Factory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class StartSpringRemoteServer {
    public static void main(String[] args) {
        Factory.initialize();
        ApplicationContext factory = new ClassPathXmlApplicationContext("classpath:springConfig.xml");
        int i =1;
    }
}