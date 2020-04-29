import Controllers.FxmlController;
import Domain.MembruActiv;
import Repository.RepoMembrii;
import Repository.RepoMesaje;
import Services.Service;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MainApp extends Application {
    public static void main(String args[]){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Service serv = createServ();
        for(MembruActiv m:serv.getAllMembers()){

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/Views/window.fxml"));
            AnchorPane root = loader.load();
            Stage newStage = new Stage();
            Scene scene = new Scene(root,1100,500);
            newStage.setScene(scene);
            newStage.setTitle(m.getName());

            FxmlController ctrl =loader.getController();
            ctrl.setService(serv,m);
            newStage.show();
        }
    }
    private Service createServ(){

        RepoMesaje repoMesage = new RepoMesaje("discutiiCuSefu.txt");
        RepoMembrii repoMembrii = new RepoMembrii("Membrii.txt");
        return new Service(repoMembrii,repoMesage);
    }

}
