package app.clientFX.Utils;

import javafx.scene.control.Alert;
import jdk.vm.ci.meta.Local;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Utils {
    public static void displayMessage(String text, Alert.AlertType alertType){
        Alert message = new Alert(alertType);
        message.setContentText(text);
        if (alertType.equals(Alert.AlertType.ERROR)){
            message.setTitle("Error message");
        }
        else if (alertType.equals(Alert.AlertType.INFORMATION)){
            message.setTitle("Information message");
        }
        message.showAndWait();
    }
    public static String dateToString(LocalDateTime date){
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
    public static LocalDateTime dateFromString(String date){
        return LocalDateTime.parse(date,DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
}
