package Controllers;
import java.util.*;
import Domain.*;
import Main.SaptamanaCurenta;
import Observer.Observer;
import Repositories.FileUserRepository;
import Services.Service;
import Services.ServiceException;
import Services.config.ApplicationContext;
import Validation.UserValidator;
import Validation.ValidationException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.*;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import utils.Autentificare;
import utils.DtoStudentNota;
import utils.DtoTemaNota;
import utils.EventTypes;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Properties;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FxmlController implements Observer {
    public Tab stTab;
    public TextField idHm;
    public TextField desHm;
    public TextField deadlineHm;
    public Button addHmBtn;
    public Button deleteHmBtn;
    public Button extendDeadlineBtn;
    public TableView<HomeWork> hmTable;
    public TableView<Grade> grTable;
    public TextField stName;
    public TextField teacher;
    public TextField grade;
    public ComboBox hmComboBox;
    public Label weekLabel;
    public TextArea feedbackTextArea;
    public ComboBox feedbackComboBox;
    public TableView stSearchTable;
    public CheckBox motivatCheckBox;
    public CheckBox intarziatCheckBox;
    public Label nrIntarziereLabel;
    public ComboBox nrSaptamaniCombo;
    public Button addGradeBtn;
    public Button generate1;
    public Button generate2;
    public Button generate3;
    public Button generate4;
    public Label labelCeaMaiGrea;
    public TableView raportTable;
    public Pagination gradePagination;
    public Pagination stPagination;
    public TabPane mainTab;
    public Tab adminTab;
    public Tab statisticsTab;
    public Label hmTabDeadlineLabel;
    public Label hmTabDescriereLabel;
    public Label hmTabIdLabel;
    public Label grTabProfesorLabel;
    public Label grTabNotaLabel;
    public Label grTabTemaLabel;
    public Label grTabFeedbackLabel;
    public Label grTabSituatieLabel;
    public Label grTabAlegeLabel;
    public TableView usersTable;
    public TextField adminTabUserField;
    public TextField adminTabPasswordField;
    public ComboBox<String> adminTabPrivilegeBox;
    public Button adminTabAddBtn;
    public Button adminTabRemoveBtn;
    public Button adminTabModifyBtn;
    private Service serv;
    private ObservableList<Student> stModel = FXCollections.observableArrayList();
    private ObservableList<HomeWork> hmModel = FXCollections.observableArrayList();
    private ObservableList<Grade> grModel = FXCollections.observableArrayList();
    private ObservableList<User> userModel = FXCollections.observableArrayList();
    private ObservableList<DtoStudentNota> raportModel = FXCollections.observableArrayList();
    @FXML
    private TableView<Student> stTable;
    @FXML
    private TextField idSt,numeSt,prenumeSt,grupaSt,emailSt,cadruSt;
    @FXML
    private Button addStBtn,deleteStBtn,updateStBtn;
    private String privilege;
    private FileUserRepository userRepo;
    @FXML
    public void initialize(){
        initListeners();
        feedbackComboBox.setOnAction(v->{
            feedbackTextArea.setText((String)feedbackComboBox.getSelectionModel().getSelectedItem());
        });
        String[] nrSapt = {"0","1","2"};
        nrSaptamaniCombo.setItems(FXCollections.observableArrayList(nrSapt));
        nrSaptamaniCombo.setVisible(false);
        nrIntarziereLabel.setVisible(false);
        stTable.setItems(stModel);
        stSearchTable.setItems(stModel);
        usersTable.setItems(userModel);
        hmTable.setItems(hmModel);
        grTable.setItems(grModel);
        weekLabel.setText("Saptamana curenta: " + SaptamanaCurenta.getCurent());
        hmComboBox.setItems(hmModel);
        String[] feedbackStrings = {"Complet si corect.","Cerinta rezolvata partial","Nu a stiut explica","Programul nu compileaza"};
        feedbackComboBox.setItems(FXCollections.observableArrayList(feedbackStrings));
        hmComboBox.setCellFactory(new Callback<ListView<HomeWork>,ListCell<HomeWork>>(){
           @Override
           public ListCell<HomeWork> call(ListView<HomeWork> param){
               return new ListCell<HomeWork>(){
                   @Override
                   protected void updateItem(HomeWork item,boolean empty){
                       super.updateItem(item,empty);
                       if(item == null || empty)
                           setText(null);
                       else
                           setText(item.getDescriere());
                   }
               };
           }
        });
        hmComboBox.setConverter(new StringConverter<HomeWork>() {
            @Override
            public String toString(HomeWork hm) {
                if(hm==null)return null;
                return hm.getDescriere();
            }

            @Override
            public HomeWork fromString(String string) {
                return null;
            }
        });
    }

    private void initListeners() {
        stTable.getSelectionModel().selectedItemProperty().addListener((obs,oldValue,newValue)->loadStudentTextField(newValue));
        hmComboBox.getSelectionModel().selectedItemProperty().addListener((obs,o,n)->{
            if(n!=null){
                HomeWork hm = (HomeWork)n;
                if(hm.getDeadlineWeek()<SaptamanaCurenta.getCurent())
                    feedbackTextArea.setText("Nota a fost diminuata cu " + (SaptamanaCurenta.getCurent()-hm.getDeadlineWeek())
                            + " puncte datorita intarzierilor");
                else
                    feedbackTextArea.clear();
            }
        });
        intarziatCheckBox.selectedProperty().addListener((obs,o,n)->{
            if(n){
                motivatCheckBox.setSelected(false);
                nrIntarziereLabel.setVisible(true);
                nrSaptamaniCombo.setVisible(true);
                nrSaptamaniCombo.getSelectionModel().selectFirst();
            }
            else{
                nrIntarziereLabel.setVisible(false);
                nrSaptamaniCombo.setVisible(false);
            }
        });
        motivatCheckBox.selectedProperty().addListener((obs,o,n)->{
            if(n){
                if(nrSaptamaniCombo.getSelectionModel().getSelectedIndex()>0){
                    feedbackTextArea.clear();
                }
                HomeWork hm = (HomeWork)hmComboBox.getSelectionModel().getSelectedItem();
                if(hm!=null && hm.getDeadlineWeek()<SaptamanaCurenta.getCurent())
                    feedbackTextArea.clear();
                intarziatCheckBox.setSelected(false);
                nrIntarziereLabel.setVisible(false);
                nrSaptamaniCombo.setVisible(false);
            }
        });
        nrSaptamaniCombo.getSelectionModel().selectedIndexProperty().addListener((obs,o,n)->{
            if(n.intValue()>0)
                feedbackTextArea.setText("Nota a fost diminuata cu "+n + " puncte datorita intarzierilor");
        });
    }

    private void loadStudentTextField(Student newValue) {
        if(newValue==null){
            deleteStBtn.setDisable(true);
            idSt.clear();
            numeSt.clear();
            prenumeSt.clear();
            grupaSt.clear();
            emailSt.clear();
            cadruSt.clear();
        }else{
            deleteStBtn.setDisable(false);
            idSt.setText(newValue.getId().toString());
            numeSt.setText(newValue.getNume());
            prenumeSt.setText(newValue.getPrenume());
            grupaSt.setText(""+newValue.getGrp());
            emailSt.setText(newValue.getEmail());
            cadruSt.setText(newValue.getCadruDidacticIndrumatorLab());
        }
    }

    public void update(EventTypes e){
        switch(e){
            case STUDENT:
                stModel.setAll(new ArrayList<>(serv.getAllStudents()));
                stPagination.setPageCount(stModel.size()/rowsPerPageStudent()+2);
                if(stModel.size()%rowsPerPageStudent()==0)
                    stPagination.setPageCount(stModel.size()/rowsPerPageStudent());
                else
                    stPagination.setPageCount(stModel.size()/rowsPerPageStudent()+1);
                stPagination.setCurrentPageIndex(Math.max(0,stPagination.getCurrentPageIndex()-1));
                break;
            case HOMEWORK:
                hmModel.setAll(new ArrayList<>(serv.getAllHomeWorks()));
                break;
            case GRADE:
                grModel.setAll(new ArrayList<>(serv.getAllGrades()));
                gradePagination.setPageCount(grModel.size()/rowsPerPageGrade()+2);
                if(grModel.size()%rowsPerPageGrade()==0)
                    gradePagination.setPageCount(grModel.size()/rowsPerPageGrade());
                else
                    gradePagination.setPageCount(grModel.size()/rowsPerPageGrade()+1);
                gradePagination.setCurrentPageIndex(Math.max(0,gradePagination.getCurrentPageIndex()-1));
                break;
        }
    }
    private int rowsPerPageGrade(){ return 10; }
    private int rowsPerPageStudent(){return 4;}
    private <E extends Entity> VBox generatePage(int pageIndex, int rowsPerPage, ObservableList<E> model, TableView table){
        model.sort((o1,o2)->{
            if(o1 instanceof Student)
                return ((Student) o1).getId() - ((Student)o2).getId();
            else
            {
                Grade g1 = (Grade) o1;
                Grade g2 = (Grade ) o2;
                if(g1.getIdStudent()<g2.getIdStudent())
                    return -1;
                if(g1.getIdStudent()>g2.getIdStudent())
                    return 1;
                if(g1.getIdHomeWork()<g2.getIdHomeWork())
                    return -1;
                if(g1.getIdHomeWork()>g2.getIdHomeWork())
                    return 1;
                return 0;
            }
        });
            int page = model.size() / rowsPerPage;
            int remainder = model.size() % rowsPerPage;
            if (remainder!=0)
                page++;
            VBox box = new VBox(5);
            if (pageIndex == page - 1) {
                if(remainder == 0)
                    remainder = rowsPerPage;
                table.setItems(FXCollections.observableList(model.subList(pageIndex * rowsPerPage, pageIndex * rowsPerPage + remainder)));
            }
            else
                table.setItems(FXCollections.observableList(model.subList(pageIndex * rowsPerPage, pageIndex * rowsPerPage + rowsPerPage)));
            box.getChildren().add(table);
            return box;
        }
    private void configurePagination(){
        gradePagination.setPageFactory(pageIndex -> {
            int pages = grModel.size()/rowsPerPageGrade();
            if(grModel.size()%rowsPerPageGrade()!=0)
                pages++;
            if(pageIndex>=pages)
                return null;
            else
                return generatePage(pageIndex,rowsPerPageGrade(),grModel,grTable);
        });
        gradePagination.setStyle("-fx-border-color:blue;");
        stPagination.setPageFactory(pageIndex->{
            int pages = stModel.size()/rowsPerPageStudent();
            if(stModel.size()%rowsPerPageStudent()!=0)
                pages++;
            if(pageIndex>=pages)
                return null;
            else
                return generatePage(pageIndex,rowsPerPageStudent(),stModel,stTable);
        });
    }
    public void setService(Service serv,String privilege){
        this.privilege=privilege;
        this.serv = serv;
        serv.addObserver(this);
        init();
        deleteStBtn.setDisable(true);
        FilteredList<Student> fList = new FilteredList<>(stModel,x->true);
        stName.textProperty().addListener((obs,old,newValue)->{
            Predicate<Student> p = st->st.getNume().startsWith(newValue);
            fList.setPredicate(p);
            stSearchTable.setItems(fList);
        });
        configurePagination();
        setForPrivilege();
    }

    private void setForPrivilege() {
        switch (privilege){
            case "student":
                mainTab.getSelectionModel().select(1);
                stTab.setDisable(true);
                adminTab.setDisable(true);
                statisticsTab.setDisable(true);

                hmTabIdLabel.setVisible(false);
                hmTabDeadlineLabel.setVisible(false);
                hmTabDescriereLabel.setVisible(false);
                addHmBtn.setVisible(false);
                deleteHmBtn.setVisible(false);
                extendDeadlineBtn.setVisible(false);
                idHm.setVisible(false);
                desHm.setVisible(false);
                deadlineHm.setVisible(false);

                hmComboBox.setVisible(false);
                teacher.setVisible(false);
                feedbackTextArea.setVisible(false);
                feedbackComboBox.setVisible(false);
                grade.setVisible(false);
                intarziatCheckBox.setVisible(false);
                motivatCheckBox.setVisible(false);
                addGradeBtn.setVisible(false);
                grTabFeedbackLabel.setVisible(false);
                grTabNotaLabel.setVisible(false);
                grTabProfesorLabel.setVisible(false);
                grTabTemaLabel.setVisible(false);
                grTabSituatieLabel.setVisible(false);
                grTabAlegeLabel.setVisible(false);
                stName.setVisible(false);
                stSearchTable.setVisible(false);
                break;
            case "teacher":
                adminTab.setDisable(true);
                break;
            case "administrator":
                try{
                    String location = ApplicationContext.getProperties().getProperty("data.users");
                    userRepo = new FileUserRepository(location,new UserValidator());
                    usersTable.getSelectionModel().selectedItemProperty().addListener((obs,o,n)->{
                        if(n==null){
                            adminTabRemoveBtn.setDisable(true);
                            adminTabModifyBtn.setDisable(true);
                            adminTabUserField.setText("");
                            adminTabPasswordField.setText("");
                        }
                        else{
                            adminTabRemoveBtn.setDisable(false);
                            adminTabModifyBtn.setDisable(false);
                            loadUserTextField((User)n);
                        }
                    });
                }catch (ValidationException ex){ex.printStackTrace();}
                String[] str  = {"student","teacher","administrator"};
                adminTabPrivilegeBox.setItems(FXCollections.observableArrayList(str));
                adminTabPrivilegeBox.getSelectionModel().selectFirst();
                userModel.setAll(new ArrayList<>(userRepo.findAll()));
                adminTabRemoveBtn.setDisable(true);
                adminTabModifyBtn.setDisable(true);
                mainTab.getSelectionModel().select(4);
                break;
        }
    }

    private void loadUserTextField(User n) {
            adminTabUserField.setText(n.getId());
            adminTabPasswordField.setText(n.getPassword());
            adminTabPrivilegeBox.getSelectionModel().select(n.getPrivilege());
    }

    public void init(){
        update(EventTypes.STUDENT);
        update(EventTypes.HOMEWORK);
        update(EventTypes.GRADE);
        hmModel.stream().filter(h->h.getDeadlineWeek()==SaptamanaCurenta.getCurent()).forEach(
                x->hmComboBox.getSelectionModel().select(x));
    }
    public void showErrorMessage(String msg) {
        Alert message = new Alert(Alert.AlertType.ERROR);
        message.setTitle("Mesaj de eroare");
        message.setContentText(msg);
        message.showAndWait();
    }
    public void addStudent(ActionEvent actionEvent) {
        String id = idSt.getText();
        String nume = numeSt.getText();
        String prenume = prenumeSt.getText();
        String grp = grupaSt.getText();
        String email = emailSt.getText();
        String cadru = cadruSt.getText();
        try{
            int id1 = Integer.parseInt(id);
            int grp1 = Integer.parseInt(grp);
            Student st = new Student(id1,nume,prenume,grp1,email,cadru);
            serv.addStudent(id1,grp1,nume,prenume,email,cadru);

            Alert message = new Alert(Alert.AlertType.INFORMATION);
            message.setTitle("Status");
            message.setContentText("Studentul a fost adaugat cu succes!");
            message.showAndWait();

        }catch (NumberFormatException ex){
            showErrorMessage("Format invalid! Se cere format numeric!");
        }catch (ValidationException ex){
            showErrorMessage(ex.getError());
        } catch (ServiceException e) {
            showErrorMessage(e.getError());
        }
    }

    public void deleteStudent(ActionEvent actionEvent) {
        int poz = stTable.getSelectionModel().getSelectedIndex();
        if(poz<0)showErrorMessage("nu e nimic selectat!");
        else{
            try {
                serv.removeStudent(stTable.getItems().get(poz).getId());
            }catch(ServiceException ex){ showErrorMessage(ex.getError()); }
        }
    }

    public void updateStudent(ActionEvent actionEvent) {
        String id = idSt.getText();
        String nume = numeSt.getText();
        String prenume = prenumeSt.getText();
        String grp = grupaSt.getText();
        String email = emailSt.getText();
        String cadru = cadruSt.getText();
        try{
            int id1 = Integer.parseInt(id);
            int grp1 = Integer.parseInt(grp);
            serv.updateStudent(id1,grp1,nume,prenume,email,cadru);
        }catch (NumberFormatException ex){
            showErrorMessage(ex.getMessage());
        }catch (ValidationException ex){
            showErrorMessage(ex.getError());
        } catch (ServiceException e) {
            showErrorMessage(e.getError());
        }
    }

    public void addHomework(ActionEvent actionEvent) {
        String id = idHm.getText();
        String desc = desHm.getText();
        String deadline = deadlineHm.getText();
        int start = SaptamanaCurenta.getCurent();
        try{
            int id1 = Integer.parseInt(id);
            int deadline1 = Integer.parseInt(deadline);
            serv.addHomeWork(id1,desc,deadline1);
            sendMailAll(new HomeWork(id1,desc,SaptamanaCurenta.getCurent(),deadline1));
        }catch (NumberFormatException ex){
            showErrorMessage(ex.getMessage());
        }catch (ValidationException ex){
            showErrorMessage(ex.getError());
        } catch (ServiceException e) {
            showErrorMessage(e.getError());
        }
    }

    private void sendMailAll(HomeWork homeWork) {
        for (Student s : serv.getAllStudents()) {
            String content = "A fost adaugata o noua tema:\nDescriere:"+homeWork.getDescriere()+
                    "\nDeadline: saptamana "+homeWork.getDeadlineWeek();
            String header = "Anunt Tema noua!";
        Autentificare.sendMail(s.getEmail(),header,content);
    }
    }
    private void sendMailAllDeadline(HomeWork homeWork, int oldDeadline) {
        for (Student s : serv.getAllStudents()) {
            String content = "Deadline-ul unei teme a fost modificat!\nTema:" + homeWork.getDescriere() +
                    "\nDeadline: saptamana " + oldDeadline+" ---> "+homeWork.getDeadlineWeek();
            String header = "Anunt modificare deadline!";
            Autentificare.sendMail(s.getEmail(), header, content);
        }
    }

    public void deleteHomework(ActionEvent actionEvent) {
        int poz = hmTable.getSelectionModel().getSelectedIndex();
        if(poz<0)showErrorMessage("nu e nimic selectat!");
        else{
            try {
                serv.removeHomeWork(hmTable.getItems().get(poz).getId());
            }catch(ServiceException ex){ showErrorMessage(ex.getError()); }
        }
    }

    public void extendDeadline(ActionEvent actionEvent) {
        int poz = hmTable.getSelectionModel().getSelectedIndex();
        if(poz<0)showErrorMessage("nu e nimic selectat!");
        else{
            String newDeadLine = deadlineHm.getText();
            try{
                int newDeadLine1 = Integer.parseInt(newDeadLine);
                int oldDeadline = hmTable.getItems().get(poz).getDeadlineWeek();
                serv.modifyDeadline(hmTable.getItems().get(poz).getId(),newDeadLine1);
                sendMailAllDeadline(hmTable.getItems().get(poz),oldDeadline);
            }catch(NumberFormatException ex){ showErrorMessage(ex.getMessage()); }
            catch(ServiceException ex){showErrorMessage(ex.getError());}
            catch(ValidationException ex){showErrorMessage(ex.getError());}
        }
    }


    public void addGrade(ActionEvent actionEvent) {
        int stIndex = stSearchTable.getSelectionModel().getSelectedIndex();
        if (stIndex < 0)
            showErrorMessage("Nu ati selectat un student! Selectati un student din tabelul de studenti.");
        else {
            try {
                float gr = Float.parseFloat(grade.getText());
                    boolean motivat = motivatCheckBox.isSelected();
                    int nrIntarziat=0;
                    if(motivat)
                        nrIntarziat=0;
                    else{
                        if(intarziatCheckBox.isSelected())
                            nrIntarziat=Integer.parseInt((String)nrSaptamaniCombo.getSelectionModel().getSelectedItem());
                        else
                            nrIntarziat=-1;
                    }
                    float[] result = null;
                    try {
                        result = serv.previewAddGrade(((Student) stSearchTable.getItems().get(stIndex)).getId(),
                                ((HomeWork) hmComboBox.getSelectionModel().getSelectedItem()).getId(),
                                teacher.getText(),gr, motivat, nrIntarziat);
                        showPreviewGrade(result,motivat,nrIntarziat);
                    }catch (ServiceException ex){showErrorMessage(ex.getError());}

            } catch (NumberFormatException ex) {
                showErrorMessage("Nota introdusa este invalida! Nota trebuie sa fie numerica!");
            }
        }
    }

    private void showPreviewGrade(float[] result,boolean motivat,int nrr) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/gradePreview.fxml"));
            AnchorPane root = (AnchorPane) loader.load();
            Stage previewStage = new Stage();
            previewStage.setTitle("Grade preview");
            Scene scene = new Scene(root);
            previewStage.setScene(scene);

            GradePreviewController ctrl = loader.getController();
            ctrl.setService(serv, previewStage, (Student) stSearchTable.getSelectionModel().getSelectedItem(),
                    (HomeWork) hmComboBox.getSelectionModel().getSelectedItem(), teacher.getText(), result[0], motivat, (int) result[1],
                    feedbackTextArea.getText(), Float.parseFloat(grade.getText()),nrr);
            previewStage.show();
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public void handleGenerate1(ActionEvent actionEvent) {
        try {
            ArrayList<DtoStudentNota> raport = serv.raportStudentNota();
            raportModel.setAll(raport);
            raportTable.setItems(raportModel);
            raportTable.setVisible(true);

                CategoryAxis xaxis = new CategoryAxis();
                xaxis.setLabel("Media lab");

                NumberAxis yaxis = new NumberAxis();
                yaxis.setLabel("Nr studenti");

                HashMap<Integer,Integer> map = new HashMap<>();
                for(int i=1;i<=10;i++) {
                    int finalI = i;
                    map.put(i, (int) raport.stream().filter(dto->{
                        return dto.getGrade() <= finalI && dto.getGrade() > (finalI - 1);
                    }).count());
                }


                BarChart barchart = new BarChart(xaxis, yaxis);
                XYChart.Series dataseries = new XYChart.Series();

                for(int i=1;i<=10;i++){
                    Integer nr = map.get(i);
                    dataseries.getData().add(new XYChart.Data(""+(i-1)+"-"+i,nr));
                }

                barchart.getData().add(dataseries);

                VBox vbox = new VBox(barchart);
                Scene scena = new Scene(vbox);

                Stage stage = new Stage();
                stage.setScene(scena);
                stage.setHeight(600);
                stage.setWidth(600);
                stage.show();

            PDDocument document = new PDDocument();
            PDPage p = new PDPage();
            document.addPage(p);
            PDPageContentStream stream = new PDPageContentStream(document,p);
            stream.setFont(PDType1Font.TIMES_ROMAN,24);

            stream.beginText();
            stream.newLineAtOffset(25,700);
            stream.showText("Raport cu media la laborator pentru fiecare student.");
            stream.endText();


            int y = 650;
            stream.setFont(PDType1Font.TIMES_BOLD,22);
            stream.beginText();
            stream.newLineAtOffset(25,670);
            stream.showText("ID     Name            MEDIA");
            stream.endText();
            stream.setFont(PDType1Font.TIMES_ROMAN,20);

            for(DtoStudentNota dto:raport){
                stream.beginText();
                stream.newLineAtOffset(25,y);
                String s = dto.getSt().getId().toString()+"        "+dto.getSt().getNume()+" "+dto.getSt().getPrenume();
                s+="      "+dto.getGrade();
                if (dto.getGrade()>=4)
                    s+= "   ELIGIBIL";
                stream.showText(s);
                stream.endText();
                y-=20;
            }
            stream.close();
            document.save(ApplicationContext.getProperties().getProperty("data.rapoarte"));
            document.close();
        }catch (Exception ex){ex.printStackTrace();}

    }

    public void handleGenerate2(ActionEvent actionEvent) {
        try {
            ArrayList<DtoTemaNota> raport = serv.raportTemaNota();
            CategoryAxis xaxis = new CategoryAxis();
            xaxis.setLabel("Tema");

            NumberAxis yaxis = new NumberAxis();
            yaxis.setLabel("Media la tema");


            BarChart barchart = new BarChart(xaxis, yaxis);
            XYChart.Series dataseries = new XYChart.Series();

            for (DtoTemaNota dto : raport)
                dataseries.getData().add(new XYChart.Data(dto.getTema().getDescriere(), dto.getGrade()));

            barchart.getData().add(dataseries);

            VBox vbox = new VBox(barchart);
            Scene scena = new Scene(vbox);

            Stage stage = new Stage();
            stage.setScene(scena);
            stage.setHeight(600);
            stage.setWidth(600);
            stage.show();
            PDDocument document = new PDDocument();
            PDPage p = new PDPage();
            document.addPage(p);
            PDPageContentStream stream = new PDPageContentStream(document, p);
            stream.setFont(PDType1Font.TIMES_ROMAN, 24);

            stream.beginText();
            stream.newLineAtOffset(25, 700);
            stream.showText("Raport cu media notelor la toate temele.");
            stream.endText();


            int y = 650;
            stream.setFont(PDType1Font.TIMES_BOLD, 22);
            stream.beginText();
            stream.newLineAtOffset(25, 670);
            stream.showText("Tema             Nota");
            stream.endText();
            stream.setFont(PDType1Font.TIMES_ROMAN, 20);

            float min = 11;
            for(DtoTemaNota dto:raport)
                if(dto.getGrade()<min)
                    min=dto.getGrade();
            for (DtoTemaNota dto : raport) {
                stream.beginText();
                stream.newLineAtOffset(25, y);

                String s = "Tema LAB "+dto.getTema().getId()+ "      "+dto.getGrade();
                if(dto.getGrade()==min) {
                    labelCeaMaiGrea.setText("Cea mai grea tema:"+dto.getTema().getDescriere());
                    labelCeaMaiGrea.setVisible(true);
                    s += "    Cea mai grea tema!";
                }
                stream.showText(s);
                stream.endText();
                y -= 20;
            }
            stream.close();
            document.save("D:/JavaProjects/MapGui/Rapoarte/raport2.pdf");
            document.close();
        }catch (Exception e){e.printStackTrace();}
    }

    public void handleGenerate3(ActionEvent actionEvent) {
        try {
            ArrayList<DtoStudentNota> raport = serv.raportStudentPredate();

            CategoryAxis xaxis = new CategoryAxis();
            xaxis.setLabel("Nr de teme predate cu intarziere");
            NumberAxis yaxis = new NumberAxis();
            yaxis.setLabel("Nr de studenti");

            BarChart barchart = new BarChart(xaxis, yaxis);
            XYChart.Series dataseries = new XYChart.Series();

            for (int i = 0; i <= 7; i++) {
                int lamI = i;
                Integer nr = (int) raport.stream().filter(dto -> {
                    return dto.getGrade() == lamI;
                }).count();
                dataseries.getData().add(new XYChart.Data("" + i, nr));
            }

            barchart.getData().add(dataseries);

            VBox vbox = new VBox(barchart);
            Scene scena = new Scene(vbox);

            Stage stage = new Stage();
            stage.setScene(scena);
            stage.setHeight(600);
            stage.setWidth(600);
            stage.show();
            PDDocument document = new PDDocument();
            PDPage p = new PDPage();
            document.addPage(p);
            PDPageContentStream stream = new PDPageContentStream(document, p);
            stream.setFont(PDType1Font.TIMES_ROMAN, 24);

            stream.beginText();
            stream.newLineAtOffset(25, 700);
            stream.showText("Raport cu studentii care au predat temele fara intarziere.");
            stream.endText();


            int y = 650;
            stream.setFont(PDType1Font.TIMES_BOLD, 22);
            stream.beginText();
            stream.newLineAtOffset(25, 670);
            stream.showText("Student        Numar de teme predate cu intarziere");
            stream.endText();
            stream.setFont(PDType1Font.TIMES_ROMAN, 20);
            for(DtoStudentNota dto:raport){
                String s = dto.getSt().getNume()+" "+dto.getSt().getPrenume()+
                       "                   "+ dto.getGrade();
                if(dto.getGrade()==0)
                    s+=" LA ZI!";
                stream.beginText();
                stream.newLineAtOffset(25,y);
                stream.showText(s);
                y-=20;
                stream.endText();
            }
            stream.close();
            document.save("D:/JavaProjects/MapGui/Rapoarte/raport3.pdf");
            document.close();
        }catch (Exception e){e.printStackTrace();}
    }

    public void handleAdminRemove(ActionEvent actionEvent) {

        User u = (User) usersTable.getSelectionModel().getSelectedItem();
        userRepo.delete(u.getId());
        usersTable.setItems(FXCollections.observableArrayList(userRepo.findAll()));
    }

    public void handleAdminAdd(ActionEvent actionEvent) {
        try {
            userRepo.save(new User(adminTabUserField.getText(), adminTabPasswordField.getText(), (String) adminTabPrivilegeBox.getSelectionModel().getSelectedItem()));
            usersTable.setItems(FXCollections.observableArrayList(userRepo.findAll()));
        }catch (ValidationException ex){
            showErrorMessage(ex.getError());
        }
    }

    public void handleAdminModify(ActionEvent actionEvent) {
        User u = (User) usersTable.getSelectionModel().getSelectedItem();
        if(u==null)
        showErrorMessage("Nu ati selectat un utilizator!\n");
        try {
            userRepo.update(new User(u.getId(), adminTabPasswordField.getText(), (String) adminTabPrivilegeBox.getSelectionModel().getSelectedItem()));
            usersTable.setItems(FXCollections.observableArrayList(userRepo.findAll()));
        }catch (ValidationException ex){
            showErrorMessage(ex.getError());
        }
    }
}
