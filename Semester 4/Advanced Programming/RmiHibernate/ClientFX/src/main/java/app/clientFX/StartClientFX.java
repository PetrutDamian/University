package app.clientFX;

import app.clientFX.Controllers.LoginController;
import app.services.IServices;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.util.Properties;

public class StartClientFX extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        try{
            ApplicationContext factory =new ClassPathXmlApplicationContext("classpath:springClient.xml");
            IServices server =(IServices)factory.getBean("service");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/Views/login.fxml"));
            AnchorPane rootPane = loader.load();

            LoginController ctrl =loader.getController();
            ctrl.setServer(server,primaryStage);

            primaryStage.setTitle("Login");
            primaryStage.setScene(new Scene(rootPane,500,600));
            primaryStage.show();

        }catch (Exception e) {
            System.out.println("Starting client exception :" + e);
        }

    }
}
