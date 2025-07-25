import com.imooc.miaoshaproject.util.TestUnit;
import org.junit.Test;

/**
 * @Description
 * @Author wuqiusheng
 * @Version 1.0
 * @Date 2025/7/15
 */
public class AmountTest {

    public static volatile String temp = "1";

    @Test
    public void amountTest1() {
        amountShow(50, 400, 12, 1.02);
    }

    @Test
    public void amountTest2() {
        amountShow(50, 350, 10, 1.02);
    }

    @Test
    public void amountTest3() {
        amountShow(50, 40, 5, 1.02);
    }

    @Test
    public void amountTest4() {
        HttpUtils.doPostZhiHu();
    }



    public static void amountShow(int year, double total, double cost, double var) {
        for (int i = 1; i <= year; i++) {
            total -= cost;
            total *= var;
            StringBuilder sb = new StringBuilder();
            sb.append(":year:"+i);
            sb.append(":total:"+total);
            System.out.println(sb);
        }
    }

    @Test
    public void timeTest1() {
        timeShow(WorkType.TWO_WEEKEND);
        timeShow(WorkType.TWO_HELF_WEEKEND);
        timeShow(WorkType.ONE_WEEKEND);
    }

    public static void timeShow(WorkType workType) {
        int totalHoliday;
        if (WorkType.TWO_WEEKEND.equals(workType)) {
            totalHoliday = 13 + 104 + 5;
        } else if (WorkType.TWO_HELF_WEEKEND.equals(workType)) {
            totalHoliday = 13 + 78 + 5;
        } else {
            totalHoliday = 13 + 52 + 5;
        }
        int totalWorkday = 365 - totalHoliday;
        int restDayRate = (totalHoliday * 10000) / 365;
        int dayHours = 16;
        int workDayHours = 14;
        int totalHours = 365 * dayHours;
        int totalWorkDayHours = totalWorkday * workDayHours;
        int totalRestHours = totalHours - totalWorkDayHours;
        int restHoursRate = (totalRestHours * 10000) / totalHours;
        StringBuilder sb = new StringBuilder();
        sb.append(":totalHoliday:"+totalHoliday);
        sb.append(":totalWorkday:"+totalWorkday);
        sb.append(":restDayRate:"+restDayRate);
        sb.append("======:totalHours:"+totalHours);
        sb.append(":totalWorkDayHours:"+totalWorkDayHours);
        sb.append(":totalRestHours:"+totalRestHours);
        sb.append(":restHoursRate:"+restHoursRate);
        System.out.println(sb);
    }

     enum WorkType {
        TWO_WEEKEND,
        TWO_HELF_WEEKEND,
        ONE_WEEKEND;
    }
}
