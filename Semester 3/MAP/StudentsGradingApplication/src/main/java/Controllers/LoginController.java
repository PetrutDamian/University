package Controllers;

import Services.Service;
import Services.config.ApplicationContext;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class LoginController {
    public PasswordField passwordField;
    public TextField usernameField;
    private Service serv;
    private Stage stage;
    public void setService(Service serv,Stage stage) {
        this.serv = serv;
        this.stage = stage;
    }

    public void handleLogin(ActionEvent actionEvent) {
        String location = ApplicationContext.getProperties().getProperty("data.users");
        try(BufferedReader csvReader = new BufferedReader(new FileReader(location))){
            String user = usernameField.getText();
            String row;
            Boolean found=false;
            String passwd = null;
            String privilege = null;
            while(true){
                if((row=csvReader.readLine())==null)break;
                String[] splitted = row.split(",");
                if(splitted[0].equals(user))
                {
                    found = true;
                    passwd=splitted[1];
                    privilege = splitted[2];
                    break;
                }
            }
            if(!found)
            {
                Alert message = new Alert(Alert.AlertType.ERROR);
                message.setTitle("Login Error Message");
                message.setContentText("No user was found with the given username!");
                message.showAndWait();
            }
            else{
                if(!passwd.equals(passwordField.getText()))
                {
                    Alert message1 = new Alert(Alert.AlertType.ERROR);
                    message1.setTitle("Login Error Message");
                    message1.setContentText("Wrong password!");
                    message1.showAndWait();
                }
                else {
                    stage.close();
                    Alert message2 = new Alert(Alert.AlertType.INFORMATION);
                    message2.setTitle("Login Message");
                    message2.setContentText("Login successful! Privileges : "+privilege);
                    message2.showAndWait();
                    try {
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource("/Views/gui.fxml"));
                        TabPane root = loader.load();
                        Stage newStage = new Stage();
                        Scene scene = new Scene(root, 700, 500);
                        newStage.setScene(scene);
                        FxmlController ctrl = loader.getController();
                        ctrl.setService(serv,privilege);
                        newStage.show();
                        String x = "dsa";

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }catch (Exception ex){ ex.printStackTrace(); }
    }
}