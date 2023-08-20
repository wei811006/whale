package help.wei.whale.service;

import help.wei.whale.domain.employee.EmployeeRepository;
import help.wei.whale.domain.employee.*;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final EmployeeManager employeeManager;

    @Autowired
    EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
        this.employeeManager = new EmployeeManager(employeeRepository);
    }

    @Override
    public void newEmployee(String id, String name, String level, LocalDate onboardingDate, LocalDate offBoardingDate) {
        Employee employee = Employee.builder()
                .employeeId(id)
                .name(name)
                .level(level)
                .onboardingDate(onboardingDate)
                .offBoardingDate(offBoardingDate)
                .build();
        employeeRepository.save(employee);
    }

    @Override
    public void updateEmployee(String id, String name, LocalDate onboardingDate, LocalDate offboardingDate) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if (optionalEmployee.isEmpty()) {
            throw new RuntimeException("Employee not found");
        }
        else {
            Employee employee = optionalEmployee.get();
            employee.setName(name);
            employee.setOnboardingDate(onboardingDate);
            employee.setOffBoardingDate(offboardingDate);
            employeeRepository.save(employee);
        }
    }

    @Override
    public void updateEmployeeDayOff(String id, Set<EmployeeDayOff> dayOffs) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if (optionalEmployee.isEmpty()) {
            throw new RuntimeException("Employee not found");
        }
        else {
            Employee employee = optionalEmployee.get();
            employee.dayOff(dayOffs);
            employeeRepository.save(employee);
        }
    }

    @Override
    public void updateEmployeeShift(String employeeId, Set<EmployeeShift> shifts) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
        if (optionalEmployee.isEmpty()) {
            throw new RuntimeException("Employee not found");
        }
        else {
            Employee employee = optionalEmployee.get();
            employee.updateShift(shifts);
            employeeRepository.save(employee);
        }
    }

    @Override
    public void deleteEmployee(String employeeId) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
        if (optionalEmployee.isEmpty()) {
            throw new RuntimeException("Employee not found");
        }
        else {
            employeeRepository.delete(optionalEmployee.get());
        }
    }

    @Override
    public Employee getEmployee(@NonNull String employeeId) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
        if (optionalEmployee.isEmpty()) {
            throw new RuntimeException("Employee not found");
        }
        else {
            return optionalEmployee.get();
        }
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }
}
