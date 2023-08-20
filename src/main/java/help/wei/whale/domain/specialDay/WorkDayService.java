package help.wei.whale.domain.specialDay;

import java.time.LocalDate;

public interface WorkDayService {

    Long create(LocalDate date, boolean workingDay);

    void delete(Long id);

}
