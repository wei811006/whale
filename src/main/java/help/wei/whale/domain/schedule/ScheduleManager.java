package help.wei.whale.domain.schedule;

import help.wei.whale.domain.employee.DayOffType;
import help.wei.whale.domain.employee.Employee;
import help.wei.whale.domain.employee.EmployeeDayOff;
import help.wei.whale.domain.employee.EmployeeManager;
import help.wei.whale.domain.project.Project;
import help.wei.whale.domain.project.ProjectManager;
import help.wei.whale.domain.project.ProjectShift;
import help.wei.whale.domain.specialDay.WorkDayManager;
import lombok.extern.slf4j.Slf4j;

import javax.swing.text.html.Option;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;

@Slf4j
public class ScheduleManager {

    private transient ScheduleRepository scheduleRepository;

    private transient EmployeeManager employeeManager;

    private transient ProjectManager projectManager;

    private transient WorkDayManager workDayManager;

    public ScheduleManager(ScheduleRepository scheduleRepository, EmployeeManager employeeManager, ProjectManager projectManager, WorkDayManager workDayManager) {
        this.scheduleRepository = scheduleRepository;
        this.employeeManager = employeeManager;
        this.projectManager = projectManager;
        this.workDayManager = workDayManager;
    }

    public void schedule(LocalDate from) {
        LocalDate lastDayOfMonth = from.withDayOfMonth(from.lengthOfMonth());

        long totalWorkerDays = getWorkDays(from);

        LocalDate today = from;
        cleanSchedule(from, lastDayOfMonth);

        // 在此處處理每一天的邏輯
        while (!today.isAfter(lastDayOfMonth)) {
            log.info("Start creating a schedule on {}", today);
            LocalDate finalToday = today;

            // 清除當天的排班

            Set<Schedule> schedules = new HashSet<>();

            // 取得當天所有的員工
            Set<Employee> employees = this.employeeManager.findAllEmployed(today, today);


            // 先安排當天不能上班的員工
            Set<Employee> employeesNeedDayOff = new HashSet<>();
            employees.forEach(
                    employee -> {
                        log.info("Check if the employee is available for work today: {}", employee.getName());
                        // 請假的員工
                        Optional<Schedule> dayOffSchedule = employeeDayOff(employee, finalToday);
                        if (dayOffSchedule.isPresent()) {
                            log.info("Employee {} is day off on {}", employee.getName(), finalToday);
                            schedules.add(dayOffSchedule.get());
                            employeesNeedDayOff.add(employee);
                            return;
                        }

                        // 排除連續上班六天的員工
                        continuesWorkDays(employee, finalToday).ifPresent(schedule -> {
                            log.info("Employee {} has been working continuously for too many days", employee.getName());
                            schedules.add(schedule);
                            employeesNeedDayOff.add(employee);
                        });
                    });
            employees.removeAll(employeesNeedDayOff);
            log.info("People available for work today is {}", employees.size());


            // 取得所有專案
            List<Project> projects = this.projectManager.findAllActive(today, today);
            projects.forEach(project -> log.info("Project: {}", project.getName()));

            // 是否為上班日
            boolean isWorkDay = this.workDayManager.isWorkDay(today);


            while(!employees.isEmpty()) {
                int employeeCount = employees.size();
                // 依照專案分配員工
                for (Project project: projects) {
                    List<ProjectShift> projectShifts = project.getProjectShifts();
                    for (ProjectShift projectShift: projectShifts) {

                        Set<Employee> employeesForProject = new HashSet<>();
                        employees.forEach(employee -> {
                            if (employee.haveShift(project.getName(), projectShift.getShift())) {
                                log.info("Assign employee {} to work on project {} with shift {}", employee.getName(), project.getName(), projectShift.getShift());
                                schedules.add(new Schedule(employee.getEmployeeId(), employee.getName(), finalToday, project.getName(), projectShift.getShift()));
                                employeesForProject.add(employee);
                            }
                        });
                        employees.removeAll(employeesForProject);
                    }
                }

                // 如果不是上班日，則將剩下的員工排入休假
                if (!isWorkDay) {
                    Set<Employee> employeesForDayOff = new HashSet<>();
                    employees.forEach(employee -> {
                        log.info("Assign employee {} to {}", employee.getName(), DayOffType.REQUIRED_LEAVE.getChineseName());
                        schedules.add(new Schedule(employee.getEmployeeId(), employee.getName(), finalToday, "", DayOffType.REQUIRED_LEAVE.name()));
                        employeesForDayOff.add(employee);
                    });
                    employees.removeAll(employeesForDayOff);
                }

                // 如果員工數量沒有變化，代表無法排班
                if (employeeCount == employees.size()) {
                    Set<Employee> employeesForError = new HashSet<>();
                    employees.forEach(employee -> {
                        log.error("Assign employee {} to {}", employee.getName(), "ERROR", "ERROR");
                        schedules.add(new Schedule(employee.getEmployeeId(), employee.getName(), finalToday, "", "ERROR"));
                        employeesForError.add(employee);
                    });
                    employees.removeAll(employeesForError);
                    break;
                }
            }

            // 保存當天排班
            if (schedules != null && !schedules.isEmpty()) {
                this.scheduleRepository.saveAll(schedules);
            }

            // 進入下一天
            today = today.plusDays(1);
        }

    }

    /**
     * 清除當天排班
     * @param startDay
     * @param endDay
     */
    private void cleanSchedule(LocalDate startDay, LocalDate endDay) {
        log.info("Delete all old schedules on this month");
        this.scheduleRepository.deleteAllByDay(startDay, endDay);
    }

    /**
     * 安排員工休假
     * @param employee
     * @param today
     * @return
     */
    private Optional<Schedule> employeeDayOff(Employee employee, LocalDate today) {
        Optional<String> employeeDayOff = employee.isDayOff(today);
        if (employeeDayOff.isPresent()) {
            return Optional.of(new Schedule(employee.getEmployeeId(), employee.getName(), today, "", employeeDayOff.get()));
        }
        return Optional.empty();
    }

    /**
     * 取得連續上班五天的員工
     * @param employee
     * @param today
     * @return
     */
    private Optional<Schedule> continuesWorkDays(Employee employee, LocalDate today) {
        List<Schedule> schedules = this.scheduleRepository.findRecentSchedulesForEmployee(employee.getEmployeeId(), today.minusDays(5));
        int count = 0;
        for (Schedule schedule: schedules) {
            if (!(schedule.getShift().equals(DayOffType.REQUIRED_LEAVE.getChineseName()) || schedule.getShift().equals(DayOffType.ANNUAL_LEAVE.getChineseName()))) {
                count++;
            }
        }

        if (count == 5) {
            return Optional.of(new Schedule(employee.getEmployeeId(), employee.getName(), today, "", DayOffType.REQUIRED_LEAVE.getChineseName()));
        }
        return Optional.empty();
    }

    /**
     * 取得當月上班天數
     * @param from
     * @return
     */
    private long getWorkDays(LocalDate from) {
        LocalDate startDate = from.withDayOfMonth(1);
        LocalDate endDate = from.withDayOfMonth(from.lengthOfMonth());
        long count = IntStream.range(0, (int) startDate.until(endDate).getDays())
                .mapToObj(startDate::plusDays)
                .filter(date -> date.getDayOfWeek() != DayOfWeek.SATURDAY && date.getDayOfWeek() != DayOfWeek.SUNDAY)
                .count();

        log.info("Work days: {}", count);
        return count;
    }

}
