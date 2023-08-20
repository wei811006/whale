package help.wei.whale.service;

import help.wei.whale.domain.employee.EmployeeService;
import help.wei.whale.domain.project.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    private final EmployeeService employeeService;

    ProjectServiceImpl(ProjectRepository projectRepository, EmployeeService employeeService) {
        this.projectRepository = projectRepository;
        this.employeeService = employeeService;
    }

    @Override
    public void createProject(String name, LocalDate startDate, LocalDate endDate, String description, Long priority) {
        ProjectManager projectManager = new ProjectManager();
        projectManager.provideWith(this.projectRepository);
        projectManager.create(name, startDate, endDate, description, priority);
    }

    @Override
    public void updateProject(Long id, String name, LocalDate startDate, LocalDate endDate, String description) {
        Optional<Project> optionalProject = projectRepository.findById(id);

        if (optionalProject.isEmpty()) {
            throw new RuntimeException("Project not found");
        }
        else {
//            Project project = optionalProject.get();
//            project.update(name, startDate, endDate, description);
//            projectRepository.save(project);
        }
    }

    @Override
    public void deleteProject(Long id) {
        Optional<Project> optionalProject = projectRepository.findById(id);

        if (optionalProject.isEmpty()) {
            throw new RuntimeException("Project not found");
        }
        else {
            projectRepository.delete(optionalProject.get());
        }
    }

    @Override
    public Project getProject(Long id) {
        Optional<Project> optionalProject = projectRepository.findById(id);

        if (optionalProject.isEmpty()) {
            throw new RuntimeException("Project not found");
        }
        else {
            return optionalProject.get();
        }
    }

    @Override
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    /**
     * Add shift to project
     */
    @Override
    public void updateShift(Long projectId, Set<ProjectShift> shifts) {
        Optional<Project> optionalProject = projectRepository.findById(projectId);

        if (optionalProject.isEmpty()) {
            throw new RuntimeException("Project not found");
        }
        else {
            Project project = optionalProject.get();
            project.updateProjectShift(shifts);
            this.projectRepository.save(project);
        }
    }

}
