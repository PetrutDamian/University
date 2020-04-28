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
}
