package help.wei.whale.domain.schedule;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.Date;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Schedule {

    @LastModifiedDate
    @Getter
    private Date lastModifiedDate;

    @CreatedDate
    @Getter
    private Date createdDate;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Getter
    private LocalDate day;

    @Getter
    private String employeeId;

    @Getter
    private String employeeName;

    @Getter
    private String projectName;

    @Getter
    private String shift;

    // for JPA
    private Schedule() {
    }

    public Schedule(String employeeId, String employeeName, LocalDate date, String projectName, String shift) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.day = date;
        this.projectName = projectName;
        this.shift = shift;
    }

    public void updateShift(String projectName, String shift) {
        this.projectName = projectName;
        this.shift = shift;
    }
}
