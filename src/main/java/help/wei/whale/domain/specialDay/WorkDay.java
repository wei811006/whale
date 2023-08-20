package help.wei.whale.domain.specialDay;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "special_day"
        , uniqueConstraints = {
        @UniqueConstraint(columnNames = {"day"})})
@EntityListeners(AuditingEntityListener.class)
public class WorkDay {

    @LastModifiedDate
    private Date lastModifiedDate;

    @CreatedDate
    private Date createdDate;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Getter
    private LocalDate day;

    @Getter
    private boolean workingDay;

    // for JPA
    private WorkDay() {
    }

    public WorkDay(LocalDate day, boolean workingDay) {
        this.day = day;
        this.workingDay = workingDay;
    }

}
