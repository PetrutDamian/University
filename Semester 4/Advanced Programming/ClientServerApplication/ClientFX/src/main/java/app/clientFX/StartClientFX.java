package app.clientFX;

import app.clientFX.Controllers.LoginController;
import app.network.ServicesProxy;
import app.services.IServices;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Properties;

public class StartClientFX extends Application {

    private static int defaultServerPort = 55556;
    private static String defaultServer = "localhost";

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("start client instance");
        Properties clientProperties =new Properties();
        try{
            clientProperties.load(StartClientFX.class.getResourceAsStream("/client.properties"));
        } catch (IOException e) {
            System.out.println("Can't find client properties" + e);
        }

        String serverIP = clientProperties.getProperty("server.host", defaultServer);
        int serverPort =defaultServerPort;

        try {
            serverPort = Integer.parseInt(clientProperties.getProperty("server.port"));
        } catch (NumberFormatException ex) {
            System.err.println("Wrong port number " + ex.getMessage());
            System.out.println("Using default port: " + defaultServerPort);
        }
        System.out.println("Using server IP " + serverIP);
        System.out.println("Using server port " + serverPort);

        //server proxy
        IServices server = new ServicesProxy(serverIP,serverPort);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Views/login.fxml"));
        AnchorPane rootPane = loader.load();

        LoginController ctrl =loader.getController();
        ctrl.setServer(server,primaryStage);

        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(rootPane,500,600));
        primaryStage.show();



    }
}
