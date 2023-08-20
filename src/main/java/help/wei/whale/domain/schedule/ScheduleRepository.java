package help.wei.whale.domain.schedule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query("SELECT DISTINCT s.employeeId FROM Schedule s WHERE s.day >= :startDay AND s.day <= :endDay ORDER BY s.employeeId ASC")
    List<String> findDistinctEmployeeIdByDayBetween(LocalDate startDay, LocalDate endDay);

    List<Schedule> findByEmployeeIdAndDayBetweenOrderByDay(String employeeId, LocalDate startDay, LocalDate endDay);

    @Modifying
    @Query("DELETE FROM Schedule s WHERE s.day >= :startDay AND s.day <= :endDay")
    void deleteAllByDay(LocalDate startDay, LocalDate endDay);

    @Query("SELECT s FROM Schedule s " +
            "WHERE s.employeeId = :employeeId " +
            "AND s.day >= :startDate " +
            "ORDER BY s.day DESC")
    List<Schedule> findRecentSchedulesForEmployee(String employeeId, LocalDate startDate);

}
