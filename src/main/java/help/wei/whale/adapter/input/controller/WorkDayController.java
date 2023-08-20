package help.wei.whale.adapter.input.controller;

import help.wei.whale.adapter.input.controller.command.AddWorkDayCommand;
import help.wei.whale.domain.specialDay.WorkDayService;
import org.springframework.web.bind.annotation.*;

@RestController
public class WorkDayController {

    private final WorkDayService workDayService;

    WorkDayController(WorkDayService workDayService) {
        this.workDayService = workDayService;
    }

    @PostMapping("/workDay")
    public Long newWorkDay(@RequestBody AddWorkDayCommand command) {
        return workDayService.create(command.getDate(), command.isWorkingDay());
    }

    @DeleteMapping("/workDay/{id}")
    public void deleteWorkDay(@PathVariable("id") Long id) {
        workDayService.delete(id);
    }

}
