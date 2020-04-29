package Main;

import java.time.LocalDateTime;

public class SaptamanaCurenta {
    private static int curent=-1;
    private SaptamanaCurenta(){}
    public static int getCurent(){
        if(curent==-1) {
            curent=1;
            LocalDateTime[] weeks = StructuraAnUniversitar.getWeeks();
            LocalDateTime now = LocalDateTime.now();
            for (LocalDateTime week : weeks) {
                if(now.isAfter(week))
                    curent++;
            }
        }
        return curent;
    }

    public static LocalDateTime getDate(int week) {
        LocalDateTime[] weeks = new LocalDateTime[14];
        weeks[0] = LocalDateTime.of(2019,10,1,10,30,0);
        weeks[1] = LocalDateTime.of(2019,10,7,10,30,0);
        weeks[2] = LocalDateTime.of(2019,10,14,10,30,0);
        weeks[3] = LocalDateTime.of(2019,10,21,10,30,0);
        weeks[4] = LocalDateTime.of(2019,10,28,10,30,0);
        weeks[5] = LocalDateTime.of(2019,11,4,10,30,0);
        weeks[6] = LocalDateTime.of(2019,11,11,10,30,0);
        weeks[7] = LocalDateTime.of(2019,11,18,10,30,0);
        weeks[8] = LocalDateTime.of(2019,11,25,10,30,0);
        weeks[9] = LocalDateTime.of(2019,12,2,10,30,0);
        weeks[10] = LocalDateTime.of(2019,12,9,10,30,0);
        weeks[11] = LocalDateTime.of(2019,12,16,10,30,0);
        weeks[12] = LocalDateTime.of(2020,1,6,10,30,0);
        weeks[13] = LocalDateTime.of(2020,1,13,10,30,0);
        return weeks[week-1];
    }
}
