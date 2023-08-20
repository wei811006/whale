package help.wei.whale.domain.specialDay;

import java.time.LocalDate;
import java.util.Optional;

import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;

public class WorkDayManager {

    private transient WorkDayRepository workDayRepository;

    public WorkDayManager(WorkDayRepository workDayRepository) {
        this.workDayRepository = workDayRepository;
    }

    public boolean isWorkDay(LocalDate date) {
        Optional<WorkDay> workDay = workDayRepository.findByDay(date);

        if (workDay.isPresent()) {
            return workDay.get().isWorkingDay();
        }
        else {
            return date.getDayOfWeek() != SATURDAY && date.getDayOfWeek() != SUNDAY;
        }
    }

}
