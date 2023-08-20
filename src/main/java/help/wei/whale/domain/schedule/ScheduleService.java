package help.wei.whale.domain.schedule;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;

public interface ScheduleService {

    void schedule(LocalDate form);

    ByteArrayInputStream downloadExcel(String month);

    String downloadExcelFilename(String month);

}
