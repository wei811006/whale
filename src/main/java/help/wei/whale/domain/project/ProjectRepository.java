package help.wei.whale.domain.project;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query("SELECT p FROM Project p " +
            "WHERE p.startDate <= :startDate " +
            "AND p.endDate >= :endDate " +
            "ORDER BY p.priority ASC")
    List<Project> findActiveProjectByDate(LocalDate startDate, LocalDate endDate);

}
