package help.wei.whale.adapter.input.controller;


import help.wei.whale.adapter.input.controller.command.AddProjectCommand;
import help.wei.whale.adapter.input.controller.command.AddProjectShiftCommand;
import help.wei.whale.adapter.input.controller.resource.ProjectResource;
import help.wei.whale.domain.project.ProjectService;
import jakarta.websocket.server.PathParam;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ProjectController {

    private final ProjectService projectService;

    ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/project/{id}")
    public ProjectResource getProject(@PathParam("id") Long id) {
        return new ProjectResource(projectService.getProject(id));
    }

    @GetMapping("/project")
    public List<ProjectResource> getAllProjects() {
        return projectService.getAllProjects().stream().map(ProjectResource::new).collect(Collectors.toList());
    }

    @PostMapping("/project")
    public void newProject(@RequestBody AddProjectCommand command) {
        projectService.createProject(command.getName(), command.getStartDate(), command.getEndDate(), command.getDescription(), command.getPriority());
    }

    // TODO
    @DeleteMapping("/project/{id}")
    public void deleteProject(@PathVariable("id") String id) {

    }

    @PostMapping("/project/{id}/shift")
    public void addShift(@PathVariable("id") Long id, @RequestBody List<AddProjectShiftCommand> command) {
        projectService.updateShift(id, command.stream().map(AddProjectShiftCommand::toProjectShift).collect(Collectors.toSet()));
    }

}
