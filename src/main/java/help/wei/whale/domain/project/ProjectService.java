package help.wei.whale.domain.project;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface ProjectService {

    void createProject(String name, LocalDate startDate, LocalDate endDate, String description, Long priority);

    void updateProject(Long projectId, String name, LocalDate startDate, LocalDate endDate, String description);

    void deleteProject(Long projectId);

    Project getProject(Long projectId);

    List<Project> getAllProjects();

    void updateShift(Long projectId, Set<ProjectShift> shifts);
}
