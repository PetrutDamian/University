package Main;

import java.time.LocalDateTime;

public class StructuraAnUniversitar {
    private static LocalDateTime weeks[] = new LocalDateTime[14];
    private StructuraAnUniversitar(){};

    public static LocalDateTime[] getWeeks() {
        weeks[0] = LocalDateTime.of(2019,10,6,23,59,59);
        weeks[1] = LocalDateTime.of(2019,10,13,23,59,59);
        weeks[2] = LocalDateTime.of(2019,10,20,23,59,59);
        weeks[3] = LocalDateTime.of(2019,10,27,23,59,59);
        weeks[4] = LocalDateTime.of(2019,11,3,23,59,59);
        weeks[5] = LocalDateTime.of(2019,11,10,23,59,59);
        weeks[6] = LocalDateTime.of(2019,11,17,23,59,59);
        weeks[7] = LocalDateTime.of(2019,11,24,23,59,59);
        weeks[8] = LocalDateTime.of(2019,12,1,23,59,59);
        weeks[9] = LocalDateTime.of(2019,12,8,23,59,59);
        weeks[10] = LocalDateTime.of(2019,12,15,23,59,59);
        weeks[11] = LocalDateTime.of(2019,12,22,23,59,59);
        weeks[12] = LocalDateTime.of(2020,1,12,23,59,59);
        weeks[13] = LocalDateTime.of(2020,1,19,23,59,59);
        return weeks;
    }
    public static int getCorrespondingWeek(LocalDateTime date){
        for(int i=0;i<=13;i++)
            if(date.isBefore(weeks[i]))
                return i+1;
            return 15;
    }
}
