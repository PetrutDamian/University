package Domain;

import java.time.LocalDateTime;

public class Mesaj {
    private String from;
    private LocalDateTime date;
    private String content;
    private String time;
    private String to;
    public Mesaj(String from,String to,String content,LocalDateTime date){
        this.to = to;
        this.from=from;
        this.content=content;
        this.date=date;
        this.time = date.getHour()+":"+date.getMinute();
    }
    public String getFrom(){
        return from;
    }
    public String getContent(){
        return content;
    }
    public LocalDateTime getDate(){
        return date;
    }
    public String getTime(){
        return time;
    }
    public String getTo(){
        return to;
    }
}
