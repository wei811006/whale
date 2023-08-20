package help.wei.whale.adapter.input.controller.resource;

import help.wei.whale.domain.project.Project;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class ProjectResource {

    private Long id;

    private String name;

    private String startDate;

    private String endDate;

    private String description;

    private LocalDateTime createdDate;

    private LocalDateTime lastModifiedDate;

    public ProjectResource(Project project) {
        this.id = project.getId();
        this.name = project.getName();
        this.startDate = project.getStartDate().toString();
        this.endDate = project.getEndDate().toString();
        this.description = project.getDescription();
        this.createdDate = project.getCreatedDate();
        this.lastModifiedDate = project.getLastModifiedDate();
    }

}
