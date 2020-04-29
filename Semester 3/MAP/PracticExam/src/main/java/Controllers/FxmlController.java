package Controllers;

import Domain.MembruActiv;
import Domain.Mesaj;
import ObserverPattern.EventTypes;
import ObserverPattern.Observer;
import Services.Service;
import Utils.ActiveType;
import Utils.MemberType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class FxmlController implements Observer {
    private List<Mesaj> pierdute = new ArrayList<>();
    public TableView<MembruActiv> tableActivi;
    public Label youLabel;
    public TextField fieldMesaj;
    public TableView<Mesaj> tableMesage;
    private ObservableList<Mesaj> mesajModel = FXCollections.observableArrayList();
    private ObservableList<MembruActiv> activiModel = FXCollections.observableArrayList();
    public Button sendToAllBtn;
    public Button sendToBtn;
    public Button maRetragBtn;
    public Button revinBtn;
    public Button pierduteBtn;
    private  FilteredList<MembruActiv> fList;
    private FilteredList<Mesaj> fList2;
    Service serv;
    private MembruActiv member;
    private boolean inactiv = false;
    @FXML
    public void initialize(){
    }
    private void setup(){
        revinBtn.setDisable(true);
        switch (member.getType()){
            case MEMBRU:
                sendToBtn.setVisible(false);
        }
    }

    public void setService(Service serv,MembruActiv member){
        this.serv=serv;
        serv.addObserver(this);
        this.member = member;
        fList = new FilteredList<>(activiModel,x->x.getActiveType().equals(ActiveType.ACTIVE));
        fList2 = new FilteredList<>(mesajModel,x->x.getTo().equals(member.getName()));
        tableMesage.setItems(fList2);
        tableActivi.setItems(fList);
        update(EventTypes.MESSAGE);
        update(EventTypes.MEMBER);
        setup();
    }
    private void reloadMembers(){
        activiModel.setAll(serv.getAllMembers());
    }
    private void reloadMessages(){
        if(member.getActiveType().equals(ActiveType.INACTIVE)) {
            List<Mesaj> toate = serv.getAllMessages();
            Predicate<Mesaj> apare = x -> {
                for (Mesaj m : fList2)
                    if (x == m)
                        return true;
                return false;
            };
            for (Mesaj m : toate) {
                if (!apare.test(m))
                    pierdute.add(m);
            }
        }
        else {
            Predicate<Mesaj> ePierdut = x -> {
                for (Mesaj m : pierdute)
                    if (x == m)
                        return true;
                return false;
            };
            mesajModel.setAll(FXCollections.observableArrayList());
            serv.getAllMessages().stream().filter(ePierdut.negate()).forEach(x->{
                mesajModel.add(x);
            });
        }
    }
    @Override
    public void update(EventTypes e) {
        switch (e){
            case MEMBER:
                reloadMembers();
                break;
            case MESSAGE:
                reloadMessages();
                break;
        }
    }

    public void sendToOne(ActionEvent actionEvent) {
        String content = fieldMesaj.getText();
        int index = tableActivi.getSelectionModel().getSelectedIndex();
        if(index<0)
            showMessage("Nu e nimeni selectat");
        else
            serv.save(member.getName(),tableActivi.getSelectionModel().getSelectedItem().getName(),content);
    }
    private void showMessage(String str){
        Alert message = new Alert(Alert.AlertType.ERROR);
        message.setTitle("Eroare");
        message.setContentText(str);
        message.showAndWait();
    }

    public void sendToAll(ActionEvent actionEvent) {
        String content = fieldMesaj.getText();
        serv.saveAll(member.getName(),content);
    }

    public void retrag(ActionEvent actionEvent) {
        inactiv=true;
        if(member.getType().equals(MemberType.SEF))
        {
           if(serv.retrasi()) {
               serv.retrag(member.getName(),true);
               maRetragBtn.setDisable(true);
               revinBtn.setDisable(false);
               pierduteBtn.setDisable(true);
           }
           else
               showMessage("NU TE POTI RETRAGE INCA");
        }
        else {
            serv.retrag(member.getName(),true);
            maRetragBtn.setDisable(true);
            revinBtn.setDisable(false);
            pierduteBtn.setDisable(true);
        }
    }

    public void revin(ActionEvent actionEvent) {
        serv.retrag(member.getName(),false);
        revinBtn.setDisable(true);
        maRetragBtn.setDisable(false);
        pierduteBtn.setDisable(false);
    }

    public void pierdute(ActionEvent actionEvent) {
        if (!inactiv)
            showMessage("NU AI FOST INACTIV");
        else {
            if (pierdute.size() == 0)
                showMessage("NU AI PIERDUT NIMIC");
            else {
                pierdute = new ArrayList<>();
                update(EventTypes.MESSAGE);
            }
        }
    }
}
