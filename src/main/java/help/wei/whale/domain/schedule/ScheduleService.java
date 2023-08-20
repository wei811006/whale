package help.wei.whale.domain.schedule;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;

public interface ScheduleService {

    void schedule(LocalDate form);

    void updateSchedule(String employeeId, String projectName, String shift, LocalDate date);

    ByteArrayInputStream downloadExcel(String month);

    String downloadExcelFilename(String month);

}
