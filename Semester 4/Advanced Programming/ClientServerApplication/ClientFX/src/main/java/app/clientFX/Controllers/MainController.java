package app.clientFX.Controllers;

import app.clientFX.Utils.Utils;
import app.model.Cursa;
import app.model.Rezervare;
import app.model.User;
import app.services.IObserver;
import app.services.IServices;
import app.services.ServiceException;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import sun.nio.ch.Util;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MainController implements IObserver {
    public TableView tableCurse;
    public TableColumn tableColumnDate;
    public TableColumn tableColumnSeats;
    public TableColumn tableColumnDestination;
    public TextField textFieldDestination;
    public TextField textFieldDate;
    public Button buttonSearch;
    public TableView tableSeats;
    public Button buttonLogout;
    public Label labelDestinationObserver;
    public Label labelDateObserver;
    public TextField textFieldNumberOfSeats;
    public Button buttonMakeBooking;
    public TableColumn tableColumnSeat;
    public TableColumn tableColumnClient;
    public TextField textFieldClientName;
    private IServices server;
    private Stage stage;
    private ObservableList<Cursa> modelCurse = FXCollections.observableArrayList();
    private ObservableList<Rezervare> modelRezervare = FXCollections.observableArrayList();
    private Cursa selectedCursa = null;
    private User user;

    public void setService(IServices server){
        this.server = server;
    }
    public void setStage(Stage stage) {
        this.stage = stage;
        this.stage.setOnCloseRequest(event -> {
            logout();
        });
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void prepareToShow() {
        loadAllCurse();
    }
    private void loadAllCurse() {
        try {
            modelCurse.setAll(server.getAllCurse());
        } catch (ServiceException e) {
            Utils.displayMessage(e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void setTableCellValueFactories(){
        tableColumnDestination.setCellValueFactory(new PropertyValueFactory<Cursa,String>("destinatie"));
        tableColumnDate.setCellValueFactory((Callback<TableColumn.CellDataFeatures<Cursa, String>, ObservableValue<String>>) p -> {
            LocalDateTime date = p.getValue().getDate();
            return new SimpleStringProperty(Utils.dateToString(date));
        });
        tableColumnSeats.setCellValueFactory(new PropertyValueFactory<Cursa,String>("locuriDisponibile"));
        tableColumnSeat.setCellValueFactory(new PropertyValueFactory<Rezervare, String>("nrLoc"));
        tableColumnClient.setCellValueFactory((Callback<TableColumn.CellDataFeatures<Rezervare,String>, ObservableValue<String>>) p->{
            String client = p.getValue().getClient();
            if (client == null)
                return new SimpleStringProperty("-");
            else
                return new SimpleStringProperty(client);
        });
    }
    @FXML
    public void initialize(){
        setTableCellValueFactories();
        setTableModel();
        setVisible(false);
        setListeners();
    }

    private void setListeners() {

    }
    private void completeSeats(){
        for (int i=modelRezervare.size()+1;i<=18;i++){
            modelRezervare.add(new Rezervare(null,null,i,null));
        }
    }

    private void loadLabels(Cursa newValue) {
        labelDestinationObserver.setText(newValue.getDestinatie());
        labelDateObserver.setText(Utils.dateToString(newValue.getDate()));
    }

    private void setVisible(boolean value) {
        labelDateObserver.setVisible(value);
        labelDestinationObserver.setVisible(value);
        tableSeats.setVisible(value);
        textFieldNumberOfSeats.setVisible(value);
        textFieldClientName.setVisible(value);
        buttonMakeBooking.setVisible(value);
    }

    private void setTableModel() {
        tableCurse.setItems(modelCurse);
        tableSeats.setItems(modelRezervare);
    }
    private void logout(){
        try {
            server.logout(this);
            Utils.displayMessage("Logging out", Alert.AlertType.INFORMATION);
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/Views/login.fxml"));
            AnchorPane pane = loader.load();
            LoginController ctrl = loader.getController();
            Stage newStage = new Stage();
            Scene scene = new Scene(pane,700,500);
            ctrl.setServer(server,newStage);
            stage.close();
            newStage.setScene(scene);
            newStage.show();
        }catch (Exception ex){
            Utils.displayMessage(ex.getMessage(), Alert.AlertType.ERROR);
        }
    }
    public void handleLogout(ActionEvent actionEvent) {
        logout();
    }

    public void handleSearch(ActionEvent actionEvent) {
        String destination = textFieldDestination.getText();
        String dateString = textFieldDate.getText();

        LocalDateTime date = Utils.dateFromString(dateString);
        Cursa c = null;
        try {
            c = server.findByDestinationAndDate(destination, date);
        } catch (ServiceException ex) {
            Utils.displayMessage(ex.getMessage(), Alert.AlertType.ERROR);
            return;
        }
        if (c == null)
            Utils.displayMessage("No transport found with specified input", Alert.AlertType.ERROR);
        else {
            setVisible(true);
            loadLabels(c);
            selectedCursa = c;
            try {
                modelRezervare.setAll(server.getAllBookings(c.getId()));
                completeSeats();
            } catch (ServiceException ex) {
                Utils.displayMessage(ex.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    public void handleBooking(ActionEvent actionEvent) {
            String nrString = textFieldNumberOfSeats.getText();
            int nr = Integer.parseInt(nrString);
            int count = (int) modelRezervare.stream().filter(x->x.getId()==null).count();
            if (nr<=0 || nr>count)
                Utils.displayMessage("Number of seats invalid!Must be between 1 and "+ (count), Alert.AlertType.ERROR);
            else{
                String username = textFieldClientName.getText();
                List<Rezervare> rezervari = new ArrayList<>();
                for (int i = 18-count+1;nr>0;i++,nr--)
                    rezervari.add(new Rezervare(i,selectedCursa.getId(),i,username));
                try {
                    server.makeBooking(rezervari);
                }catch (ServiceException ex){
                    Utils.displayMessage(ex.getMessage(), Alert.AlertType.ERROR);
                }
            }
    }

    @Override
    public void seatsDecremented(Cursa cursa) {
        Platform.runLater(()->{
            for(int i=0;i<modelCurse.size();i++)
                if(modelCurse.get(i).getId()==cursa.getId())
                {
                    modelCurse.set(i,cursa);
                    break;
                }
        });

    }

    @Override
    public void newBookings(List<Rezervare> rezervari) {
        Platform.runLater(()->{
            if (selectedCursa==null || !selectedCursa.getId().equals(rezervari.get(0).getIdCursa()))
                return;
            int nrLoc = rezervari.get(0).getNrLoc();
            for(int i=nrLoc-1;i<nrLoc-1+rezervari.size();i++){
                modelRezervare.set(i,rezervari.get(i-nrLoc+1));
            }
        });

    }
}
