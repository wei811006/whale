package help.wei.whale.adapter.input.controller;

import help.wei.whale.adapter.input.controller.command.ScheduleCommand;
import help.wei.whale.adapter.input.controller.command.UpdateScheduleCommand;
import help.wei.whale.domain.schedule.ScheduleService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping("/schedule")
    public void schedule(@RequestBody ScheduleCommand command) {
        log.info("Schedule for month: {} ", command.getFrom());
        scheduleService.schedule(command.getFrom());
    }

    @PostMapping("/schedule/shift")
    public void scheduleShift(@RequestBody UpdateScheduleCommand command) {
        log.info("Update Schedule shift : {} ", command);
        scheduleService.updateSchedule(command.getEmployeeID(), command.getProjectName(), command.getShift(), command.getDate());
    }

    @GetMapping("/schedule/download/{month}")
    public ResponseEntity<Resource> downloadSchedule(@PathVariable("month") String month, HttpServletResponse response) {
        log.info("Download schedule for month: {}", month);

        InputStreamResource file = new InputStreamResource(scheduleService.downloadExcel(month));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + scheduleService.downloadExcelFilename(month))
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
    }

}
