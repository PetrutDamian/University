package app.network.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public class DestinationDateDto implements Serializable {
    String destination;
    LocalDateTime date;

    @Override
    public String toString() {
        return "DestinationDateDto{" +
                "destination='" + destination + '\'' +
                ", date=" + date +
                '}';
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getDestination() {
        return destination;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public DestinationDateDto(String destination, LocalDateTime date) {
        this.destination = destination;
        this.date = date;
    }
}
