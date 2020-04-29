import Controllers.FxmlController;
import Controllers.LoginController;
import Repositories.XmlGradeRepository;
import Repositories.XmlHomeWorkRepository;
import Repositories.XmlStudentRepository;
import Services.Service;
import Services.config.ApplicationContext;
import Validation.GradeValidator;
import Validation.HomeWorkValidator;
import Validation.StudentValidator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.util.Properties;

public class MainApp extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws  Exception {
        /*
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Views/gui.fxml"));
        TabPane root = loader.load();

        FxmlController ctrl = loader.getController();
        Service serv = createServ();
        ctrl.setService(serv);
        Scene scene = new Scene(root,700,500);
        primaryStage.setScene(scene);
        primaryStage.show();
        */
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Views/login.fxml"));
        AnchorPane root = loader.load();
        LoginController ctrl = loader.getController();
        ctrl.setService(createServ(),primaryStage);
        Scene scene = new Scene(root,700,500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    private Service createServ() {
        try {
            Properties properties = ApplicationContext.getProperties();
            String stdFile = properties.getProperty("data.students");
            String hmFile = properties.getProperty("data.homeworks");
            String grFile = properties.getProperty("data.grades");
            StudentValidator stVal = new StudentValidator();
            HomeWorkValidator hmVal = new HomeWorkValidator();
            GradeValidator grVal = new GradeValidator();
            XmlStudentRepository studRepo = new XmlStudentRepository(stdFile, stVal);
            XmlHomeWorkRepository homeRepo = new XmlHomeWorkRepository(hmFile, hmVal);
            XmlGradeRepository gradeRepo = new XmlGradeRepository(grFile, grVal);

            return new Service(studRepo, homeRepo, gradeRepo);
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }
}
