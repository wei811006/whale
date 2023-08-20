package help.wei.whale.domain.project;

import java.time.LocalDate;
import java.util.List;

public class ProjectManager {

    private transient ProjectRepository projectRepository;

    public ProjectManager() {
    }

    public void provideWith(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public void create(String name, LocalDate startDate, LocalDate endDate, String description, Long priority) {
        if (this.priorityDuplicate(priority)) {
            throw new IllegalArgumentException("Priority is duplicate.");
        }
        this.projectRepository.save(new Project(name, startDate, endDate, description, priority));
    }

    public List<Project> findAllActive(LocalDate start, LocalDate end) {
        return this.projectRepository.findActiveProjectByDate(start, end);
    }

    private boolean priorityDuplicate(Long priority) {
        return false;
//        return this.projectRepository.findByPriority(priority).isPresent();
    }

}
