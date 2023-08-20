package help.wei.whale.adapter.input.controller;

import help.wei.whale.adapter.input.controller.command.AddEmployeeCommand;
import help.wei.whale.adapter.input.controller.command.UpdateEmployeeCommand;
import help.wei.whale.adapter.input.controller.command.UpdateEmployeeDayOffCommand;
import help.wei.whale.adapter.input.controller.command.UpdateEmployeeShiftCommand;
import help.wei.whale.adapter.input.controller.resource.EmployeeResource;
import help.wei.whale.domain.employee.Employee;
import help.wei.whale.domain.employee.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/employee/{id}")
    public EmployeeResource getEmployee(@PathVariable("id") String id) {
        return new EmployeeResource(employeeService.getEmployee(id));
    }

    @GetMapping("/employee")
    public List<EmployeeResource> getAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        return employees.stream().map(EmployeeResource::new).collect(Collectors.toList());
    }

    @PostMapping("/employee")
    public void newEmployee(@NotNull @RequestBody AddEmployeeCommand command) {
        log.info("newEmployee: command={}", command);
        employeeService.newEmployee(command.getEmployeeID(), command.getName(), command.getLevel(), command.getOnboardingDate(), command.getOffboardingDate());
    }

    @PutMapping("/employee/{id}")
    public void updateEmployee(@PathVariable("id") String id, @NotNull @RequestBody UpdateEmployeeCommand command) {
        log.info("updateEmployee: id={}, command={}", id, command);
        employeeService.updateEmployee(id, command.getName(), command.getOnboardingDate(), command.getOffboardingDate());
    }

    @PostMapping("/employee/{id}/dayOff")
    public void updateEmployeeDayOff(@PathVariable("id") String id, @NotNull @RequestBody List<UpdateEmployeeDayOffCommand> command) {
        log.info("updateEmployeeDayOff: id={}, command={}", id, command);
        employeeService.updateEmployeeDayOff(id, command.stream().map(UpdateEmployeeDayOffCommand::toEmployeeDayOff).collect(Collectors.toSet()));
    }

    @PostMapping("/employee/{id}/shift")
    public void updateEmployeeShift(@PathVariable("id") String id, @NotNull @RequestBody List<UpdateEmployeeShiftCommand> command) {
        log.info("updateEmployeeShift: id={}, command={}", id, command);
        employeeService.updateEmployeeShift(id, command.stream().map(UpdateEmployeeShiftCommand::toEmployeeShift).collect(Collectors.toSet()));
    }

    @DeleteMapping("/employee/{id}")
    public void deleteEmployee(@PathVariable("id") String id) {
        employeeService.deleteEmployee(id);
    }

}
