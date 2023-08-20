package help.wei.whale.domain.employee;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDate;

@Embeddable
@EqualsAndHashCode(of = "day")
public class EmployeeDayOff {

    @Getter(AccessLevel.PACKAGE)
    private LocalDate day;

    @Getter
    @Enumerated(EnumType.STRING)
    private DayOffType type;

    // for JPA
    private EmployeeDayOff() {}

    public EmployeeDayOff(LocalDate day, DayOffType type) {
        this.day = day;
        this.type = type;
    }

}
