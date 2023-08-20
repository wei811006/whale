package help.wei.whale.service;

import help.wei.whale.domain.specialDay.WorkDay;
import help.wei.whale.domain.specialDay.WorkDayRepository;
import help.wei.whale.domain.specialDay.WorkDayService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class WorkDayServiceImpl implements WorkDayService {

    private final WorkDayRepository workDayRepository;

    public WorkDayServiceImpl(WorkDayRepository workDayRepository) {
        this.workDayRepository = workDayRepository;
    }


    @Override
    public Long create(LocalDate date, boolean workingDay) {
        WorkDay specialDay = new WorkDay(date, workingDay);
        return workDayRepository.save(specialDay).getId();
    }

    @Override
    public void delete(Long id) {
        workDayRepository.deleteById(id);
    }
}
