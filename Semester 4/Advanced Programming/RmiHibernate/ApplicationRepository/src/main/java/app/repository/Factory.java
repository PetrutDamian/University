package app.repository;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Factory {
    public static SessionFactory sessionFactory;
    public static void initialize(){
        /*
        System.out.println("I got here 1");
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        System.out.println("I got here 2");
        try {
            sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
            System.out.println("i got here 3");
        }
        catch (Exception e) {
            StandardServiceRegistryBuilder.destroy( registry );
        }
        */

        Configuration x;
        x= new Configuration().configure();
        sessionFactory = x.buildSessionFactory();
    }

    static void close(){
        if ( sessionFactory != null ) {
            sessionFactory.close();
        }

    }
}
