package Controllers;

import Domain.HomeWork;
import Domain.Student;
import Services.Service;
import Services.ServiceException;
import Validation.ValidationException;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utils.Autentificare;

public class GradePreviewController {
    public TextField penaltyField;
    public TextField teacherField;
    public TextField gradeField;
    public TableView homeworkTable;
    public TableView studentTable;
    public TextArea feedbackField;
    private Service serv;
    Stage previewStage;
    float grade;
    Student st;
    HomeWork hm;
    String teacher;
    boolean motivat;
    int nrIntarziere;
    int nrr;
    String feedback;
    public void setService(Service serv, Stage stage, Student st, HomeWork hm,String teacher,Float grade,
                           boolean motivat, int nrIntarzieri,String feedback,float oldGrade,int nrr)
    {
        this.nrr =nrr;
        this.st = st;
        this.teacher = teacher;
        this.hm= hm;
        this.motivat = motivat;
        this.nrIntarziere=nrIntarzieri;
        this.feedback = feedback;
        this.serv = serv;
        this.grade = oldGrade;
        previewStage=stage;
        studentTable.getItems().add(st);
        homeworkTable.getItems().add(hm);
        teacherField.setText(teacher);
        gradeField.setText(grade.toString());
        if(motivat)
            penaltyField.setText("Motivat! Nu se aplica penalitati.");
        else{
            if(nrIntarzieri>0) {
                penaltyField.setText("Penalizari de " + nrIntarzieri + " puncte.");
            }
            else
                penaltyField.setText("Nu e cazul.");
        }
        feedbackField.setText(feedback);
    }
    public void handleSave(ActionEvent actionEvent) {
        try {
            serv.addGrade(st.getId(), hm.getId(), teacher, grade, motivat, nrr , feedback);
            Autentificare.sendMail(st.getEmail(),"Nota noua!","Tema:"+
                    hm.getDescriere()+"\nNota primita:"+gradeField.getText()+"\nProfesor:"+
                    teacherField.getText()+"\nFeedback : "+feedbackField.getText());
        }catch (ServiceException| ValidationException ex){showMessage(ex.getMessage());}
        previewStage.close();
        showMessage("Nota a fost adaugata cu succes!");
    }
    public void showMessage(String msg) {
        Alert message = new Alert(Alert.AlertType.INFORMATION);
        message.setTitle("Status");
        message.setContentText(msg);
        message.showAndWait();
    }

    public void handleCancel(ActionEvent actionEvent) {
        previewStage.close();
    }
}
