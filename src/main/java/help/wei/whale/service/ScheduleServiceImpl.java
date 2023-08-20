package help.wei.whale.service;

import help.wei.whale.domain.employee.Employee;
import help.wei.whale.domain.employee.EmployeeManager;
import help.wei.whale.domain.employee.EmployeeRepository;
import help.wei.whale.domain.project.Project;
import help.wei.whale.domain.project.ProjectManager;
import help.wei.whale.domain.project.ProjectRepository;
import help.wei.whale.domain.schedule.*;
import help.wei.whale.domain.specialDay.WorkDayManager;
import help.wei.whale.domain.specialDay.WorkDayRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;

    private final ProjectRepository projectRepository;

    private final EmployeeRepository employeeRepository;

    private final WorkDayRepository workDayRepository;

    ScheduleServiceImpl(
            ScheduleRepository scheduleRepository,
            ProjectRepository projectRepository,
            EmployeeRepository employeeRepository,
            WorkDayRepository workDayRepository
    ) {
        this.scheduleRepository = scheduleRepository;
        this.projectRepository = projectRepository;
        this.employeeRepository = employeeRepository;
        this.workDayRepository = workDayRepository;
    }

    @Transactional
    @Override
    public void schedule(LocalDate form) {
        ProjectManager projectManager = new ProjectManager();
        projectManager.provideWith(this.projectRepository);

        EmployeeManager employeeManager = new EmployeeManager(this.employeeRepository);

        WorkDayManager workDayManager = new WorkDayManager(workDayRepository);

        ScheduleManager scheduleManager = new ScheduleManager(scheduleRepository, employeeManager, projectManager, workDayManager);
        scheduleManager.schedule(form);
    }

    @Override
    public void updateSchedule(String employeeId, String projectName, String shift, LocalDate date) {
        // check if employeeId, projectId, shift, date are valid
        Employee employee = employeeRepository.findById(employeeId).orElseThrow();

        Optional<Schedule> schedule = scheduleRepository.findByEmployeeIdAndDay(employeeId, date);
        if (schedule.isEmpty()) {
            schedule = Optional.of(new Schedule(employeeId, employee.getName(), date, projectName, shift));
        }
        else {
            schedule.get().updateShift(projectName, shift);
        }
        this.scheduleRepository.save(schedule.get());
    }

    @Override
    public ByteArrayInputStream downloadExcel(String month) {
        ScheduleExcelExporter excelExporter = new ScheduleExcelExporter(scheduleRepository);
        return excelExporter.export(month);
    }

    @Override
    public String downloadExcelFilename(String month) {
        return ScheduleExcelExporter.downloadExcelFilename(month);
    }
}
