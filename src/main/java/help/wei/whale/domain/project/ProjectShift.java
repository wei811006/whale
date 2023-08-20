package help.wei.whale.domain.project;

import help.wei.whale.domain.employee.Employee;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.DayOfWeek;

@Embeddable
@EqualsAndHashCode(of = {"shift", "shiftDay", "priority"})
public class ProjectShift {

    @Getter
    private String shift;

    @Getter
    private DayOfWeek shiftDay;

    @Getter
    private Long priority;

    // for JPA
    private ProjectShift() {
    }

    public ProjectShift(String shift, DayOfWeek shiftDay, Long priority) {
        this.shift = shift;
        this.shiftDay = shiftDay;
        this.priority = priority;
    }

}
