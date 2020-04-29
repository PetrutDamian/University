package app.network.dto;

import java.time.LocalDateTime;

public class DtoUtils {
    public static DestinationDateDto createDestinationDateDto(String destination, LocalDateTime date){
        return new DestinationDateDto(destination,date);
    }
}
