package help.wei.whale.service;

import help.wei.whale.domain.employee.EmployeeManager;
import help.wei.whale.domain.employee.EmployeeRepository;
import help.wei.whale.domain.project.ProjectManager;
import help.wei.whale.domain.project.ProjectRepository;
import help.wei.whale.domain.schedule.ScheduleExcelExporter;
import help.wei.whale.domain.schedule.ScheduleManager;
import help.wei.whale.domain.schedule.ScheduleRepository;
import help.wei.whale.domain.schedule.ScheduleService;
import help.wei.whale.domain.specialDay.WorkDayManager;
import help.wei.whale.domain.specialDay.WorkDayRepository;
import jakarta.transaction.Transactional;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;

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
    public ByteArrayInputStream downloadExcel(String month) {
        ScheduleExcelExporter excelExporter = new ScheduleExcelExporter(scheduleRepository);
        return excelExporter.export(month);
    }

    @Override
    public String downloadExcelFilename(String month) {
        return ScheduleExcelExporter.downloadExcelFilename(month);
    }
}
