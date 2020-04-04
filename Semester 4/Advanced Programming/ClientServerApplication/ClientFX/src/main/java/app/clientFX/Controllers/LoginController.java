package app.clientFX.Controllers;

import app.model.User;
import app.clientFX.Utils.Utils;
import app.model.User;
import app.services.IServices;
import app.services.ServiceException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    public TextField usernameField;
    public PasswordField passwordField;
    private IServices server;
    private Stage myStage;
    private MainController parentCtrl;
    private Pane parentPane;
    private User user;

    public void handleLogin(ActionEvent actionEvent) {
        String username  = usernameField.getText();
        String password = passwordField.getText();
        user = new User(username,password);
        try{
            server.login(user,parentCtrl);
            Stage stage = new Stage();
            stage.setTitle("User logged in: "+user.getId());
            stage.setScene(new Scene(parentPane,850,600));
            parentCtrl.setStage(stage);
            parentCtrl.setUser(user);
            myStage.close();
            parentCtrl.prepareToShow();
            stage.show();
        }catch (ServiceException ex){
            Utils.displayMessage("Login Failed!\nIncorrect password or username!", Alert.AlertType.ERROR);
            passwordField.setText("");
        }
    }

    public void setServer(IServices serv, Stage stage){
        this.server = serv;
        this.myStage = stage;
        createMainController();
    }

    private void createMainController() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Views/main_window.fxml"));
        try {
            AnchorPane pane = loader.load();
            MainController ctrl = loader.getController();
            ctrl.setService(this.server);
            parentCtrl = ctrl;
            parentPane = pane;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
