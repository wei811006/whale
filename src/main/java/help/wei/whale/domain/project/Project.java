package help.wei.whale.domain.project;

import help.wei.whale.domain.employee.Employee;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Project {

    @LastModifiedDate
    @Getter
    private LocalDateTime lastModifiedDate;

    @CreatedDate
    @Getter
    private LocalDateTime createdDate;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    private String name;

    @Getter
    private LocalDate startDate;

    @Getter
    private LocalDate endDate;

    @Getter
    private String description;

    @Getter
    private Long priority;

    @ElementCollection
    private Set<ProjectShift> projectShifts = new HashSet<>();

    // for JPA
    public Project() {
    }

    Project(String name, LocalDate startDate, LocalDate endDate, String description, Long priority) {
        setName(name);
        setStartDate(startDate);
        setEndDate(endDate);
        setDescription(description);
        setPriority(priority);
    }

    public void updateProjectShift(Set<ProjectShift> shifts) {
        this.projectShifts.clear();
        this.projectShifts.addAll(shifts);
    }

    public Long getId() {
        return id;
    }

    public List<ProjectShift> getProjectShifts(DayOfWeek dayOfWeek) {
        List<ProjectShift> cloneShifts = projectShifts.stream().filter(projectShift -> projectShift.getShiftDay().equals(dayOfWeek)).collect(Collectors.toList());
        cloneShifts.sort(Comparator.comparingLong(ProjectShift::getPriority));
        return cloneShifts;
    }

    private void setName(String name) {
        this.name = name;
    }

    private void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    private void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    private void setDescription(String description) {
        this.description = description;
    }

    private void setPriority(Long priority) {
        this.priority = priority;
    }
}
