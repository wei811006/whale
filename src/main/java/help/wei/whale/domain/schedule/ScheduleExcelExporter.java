package help.wei.whale.domain.schedule;

import help.wei.whale.domain.employee.DayOffType;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

@Slf4j
public class ScheduleExcelExporter {

    private final ScheduleRepository scheduleRepository;

    @Autowired
    public ScheduleExcelExporter(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public static String downloadExcelFilename(String month) {
        return month + ".xlsx";
    }

    public ByteArrayInputStream export(String month) {
        LocalDate firstDayOfMonth = LocalDate.parse(month + "-01");
        LocalDate lastDayOfMonth = firstDayOfMonth.withDayOfMonth(firstDayOfMonth.lengthOfMonth());

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet(month);

            createHeader(workbook, sheet, firstDayOfMonth, lastDayOfMonth);

            List<String> employeeIds = scheduleRepository.findDistinctEmployeeIdByDayBetween(firstDayOfMonth, lastDayOfMonth);
            employeeIds.forEach(employeeId -> {
                log.info("employeeId: {}", employeeId);
                memberRow(firstDayOfMonth, lastDayOfMonth, workbook, sheet, employeeId);
            });

            createFooter(workbook, sheet, firstDayOfMonth, lastDayOfMonth);

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            log.error("IOException: {}", e.getMessage());
            throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
        }
    }

    /**
     * 建立員工排班資料
     * @param firstDayOfMonth
     * @param lastDayOfMonth
     * @param workbook
     * @param sheet
     * @param employeeId
     */
    private void memberRow(LocalDate firstDayOfMonth, LocalDate lastDayOfMonth, Workbook workbook, Sheet sheet, String employeeId) {
        int rowNum = sheet.getLastRowNum() + 1;
        Row row = sheet.createRow(rowNum);
        row.setHeightInPoints(18);

        List<Schedule> schedules = scheduleRepository.findByEmployeeIdAndDayBetweenOrderByDay(employeeId, firstDayOfMonth, lastDayOfMonth);

        // 員工姓名
        Cell memberNameCell = row.createCell(0);
        memberNameCell.setCellValue(employeeId + " " + schedules.get(0).getEmployeeName());
        memberNameCell.setCellStyle(centeredStyle(workbook));

        // 排班內容
        for (Schedule schedule : schedules) {
            Cell cell = row.createCell(schedule.getDay().getDayOfMonth());
            cell.setCellValue(schedule.getProjectName() + " " + schedule.getShift());

            if (schedule.getShift().equals(DayOffType.ROTATING_LEAVE.getChineseName())) {
                // 輪休樣式
                cell.setCellStyle(rotatingLeaveStyle(workbook));
            }
            else if (schedule.getShift().equals(DayOffType.REQUIRED_LEAVE.getChineseName())) {
                // 必休樣式
                cell.setCellStyle(requireLeaveStyle(workbook));
            }
            else {
                // 預設樣式
                cell.setCellStyle(centeredStyle(workbook));
            }

        }

        // 統計天數
        Cell annualLeaveCell = row.createCell(lastDayOfMonth.getDayOfMonth() + 1);
        annualLeaveCell.setCellFormula("COUNTIFS(B"+ (rowNum+1) + ":" + convertToExcelColumn(lastDayOfMonth.getDayOfMonth() + 1) + (rowNum+1) +",\"*特休*\")");

        Cell requiredLeaveCell = row.createCell(lastDayOfMonth.getDayOfMonth() + 2);
        requiredLeaveCell.setCellFormula(
                "COUNTIFS(B"+ (rowNum+1) + ":" + convertToExcelColumn(lastDayOfMonth.getDayOfMonth() + 1) + (rowNum+1) +",\"*輪休*\")" +
                "+COUNTIFS(B"+ (rowNum+1) + ":" + convertToExcelColumn(lastDayOfMonth.getDayOfMonth() + 1) + (rowNum+1) +",\"*必休*\")");

        Cell workDayCell = row.createCell(lastDayOfMonth.getDayOfMonth() + 3);
        workDayCell.setCellFormula("COUNTIFS(B"+ (rowNum+1) + ":" + convertToExcelColumn(lastDayOfMonth.getDayOfMonth() + 1) + (rowNum+1) +",\"<>*休*\")");

        Cell aShiftCell = row.createCell(lastDayOfMonth.getDayOfMonth() + 4);
        aShiftCell.setCellFormula("COUNTIFS(B"+ (rowNum+1) + ":" + convertToExcelColumn(lastDayOfMonth.getDayOfMonth() + 1) + (rowNum+1) +",\"*A\")");

        Cell bShiftCell = row.createCell(lastDayOfMonth.getDayOfMonth() + 5);
        bShiftCell.setCellFormula("COUNTIFS(B"+ (rowNum+1) + ":" + convertToExcelColumn(lastDayOfMonth.getDayOfMonth() + 1) + (rowNum+1) +",\"*B\")");

        Cell cShiftCell = row.createCell(lastDayOfMonth.getDayOfMonth() + 6);
        cShiftCell.setCellFormula("COUNTIFS(B"+ (rowNum+1) + ":" + convertToExcelColumn(lastDayOfMonth.getDayOfMonth() + 1) + (rowNum+1) +",\"*C\")");

        Cell dShiftCell = row.createCell(lastDayOfMonth.getDayOfMonth() + 7);
        dShiftCell.setCellFormula("COUNTIFS(B"+ (rowNum+1) + ":" + convertToExcelColumn(lastDayOfMonth.getDayOfMonth() + 1) + (rowNum+1) +",\"*D\")");

        Cell eShiftCell = row.createCell(lastDayOfMonth.getDayOfMonth() + 8);
        eShiftCell.setCellFormula("COUNTIFS(B"+ (rowNum+1) + ":" + convertToExcelColumn(lastDayOfMonth.getDayOfMonth() + 1) + (rowNum+1) +",\"*E\")");

        Cell fShiftCell = row.createCell(lastDayOfMonth.getDayOfMonth() + 9);
        fShiftCell.setCellFormula("COUNTIFS(B"+ (rowNum+1) + ":" + convertToExcelColumn(lastDayOfMonth.getDayOfMonth() + 1) + (rowNum+1) +",\"*F\")");
    }

    /**
     * 建立標題列
     * @param workbook
     * @param sheet
     * @param firstDayOfMonth
     * @param lastDayOfMonth
     */
    private void createHeader(Workbook workbook, Sheet sheet, LocalDate firstDayOfMonth, LocalDate lastDayOfMonth) {
        // 日期列
        Row headerForDate = sheet.createRow(0);
        headerForDate.setHeightInPoints(18);

        Row headerDayOfWeek = sheet.createRow(1);
        headerDayOfWeek.setHeightInPoints(18);

        // 顯示組內成員字樣
        Cell member = headerForDate.createCell(0);
        member.setCellValue("組內成員");
        member.setCellStyle(centeredStyle(workbook));
        headerDayOfWeek.createCell(0);
        CellRangeAddress mergedRegion = new CellRangeAddress(0, 1, 0, 0);
        sheet.addMergedRegion(mergedRegion);
        sheet.setColumnWidth(0, 15 * 256);

        // 日期與星期
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d");
        while (!firstDayOfMonth.isAfter(lastDayOfMonth)) {
            int dayOfMonth = firstDayOfMonth.getDayOfMonth();

            // 日期
            Cell dayCell = headerForDate.createCell(dayOfMonth);
            dayCell.setCellValue(firstDayOfMonth.format(formatter));
            dayCell.setCellStyle(centeredStyle(workbook));

            // 星期
            DayOfWeek dayOfWeek = firstDayOfMonth.getDayOfWeek();

            Cell dayOfWeekCell = headerDayOfWeek.createCell(dayOfMonth);
            dayOfWeekCell.setCellValue(dayOfWeek.getDisplayName(TextStyle.FULL_STANDALONE, Locale.CHINESE));
            dayOfWeekCell.setCellStyle(dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY ? dayOfWeekCellCellStyle(workbook): centeredStyle(workbook));

            firstDayOfMonth = firstDayOfMonth.plusDays(1);
        }

        // 統計天數
        int dayCount = lastDayOfMonth.getDayOfMonth();
        shiftCell(workbook, headerDayOfWeek, dayCount + 1, "特休天數");
        shiftCell(workbook, headerDayOfWeek, dayCount + 2, "輪必休天數");
        shiftCell(workbook, headerDayOfWeek, dayCount + 3, "上班天數");
        shiftCell(workbook, headerDayOfWeek, dayCount + 4, "A班");
        shiftCell(workbook, headerDayOfWeek, dayCount + 5, "B班");
        shiftCell(workbook, headerDayOfWeek, dayCount + 6, "C班");
        shiftCell(workbook, headerDayOfWeek, dayCount + 7, "D班");
        shiftCell(workbook, headerDayOfWeek, dayCount + 8, "E班");
        shiftCell(workbook, headerDayOfWeek, dayCount + 9, "F班");
    }

    /**
     * 建立統計列
     * @param workbook
     * @param sheet
     * @param firstDayOfMonth
     * @param lastDayOfMonth
     */
    private void createFooter(Workbook workbook, Sheet sheet, LocalDate firstDayOfMonth, LocalDate lastDayOfMonth) {
        int rowNum = sheet.getLastRowNum() + 1;

        // 人數統計列
        Row aShiftRow = countTitle(sheet, "A班人數", workbook);
        Row bShiftRow = countTitle(sheet, "B班人數", workbook);
        Row cShiftRow = countTitle(sheet, "C班人數", workbook);
        Row dShiftRow = countTitle(sheet, "D班人數", workbook);
        Row eShiftRow = countTitle(sheet, "E班人數", workbook);
        Row fShiftRow = countTitle(sheet, "F班人數", workbook);
        Row leaveRow = countTitle(sheet, "休假人數", workbook);

        while (!firstDayOfMonth.isAfter(lastDayOfMonth)) {

            // 計算A班人數
            countDetail(aShiftRow, firstDayOfMonth, rowNum, ",\"*A\")", workbook);
            countDetail(bShiftRow, firstDayOfMonth, rowNum, ",\"*B\")", workbook);
            countDetail(cShiftRow, firstDayOfMonth, rowNum, ",\"*C\")", workbook);
            countDetail(dShiftRow, firstDayOfMonth, rowNum, ",\"*D\")", workbook);
            countDetail(eShiftRow, firstDayOfMonth, rowNum, ",\"*E\")", workbook);
            countDetail(fShiftRow, firstDayOfMonth, rowNum, ",\"*F\")", workbook);
            countDetail(leaveRow, firstDayOfMonth, rowNum, ",\"*休\")", workbook);

            firstDayOfMonth = firstDayOfMonth.plusDays(1);
        }
    }

    /**
     * 建立統計標題列
     * @param sheet
     * @param title
     * @param workbook
     * @return
     */
    private static Row countTitle(Sheet sheet, String title, Workbook workbook) {
        Row row = sheet.createRow(sheet.getLastRowNum() + 1);
        row.setHeightInPoints(18);
        Cell aShiftTitleCell = row.createCell(0);
        aShiftTitleCell.setCellValue(title);
        aShiftTitleCell.setCellStyle(centeredStyle(workbook));
        return row;
    }

    /**
     * 計算班別人數
     * @param leaveRow
     * @param firstDayOfMonth
     * @param rowNum
     * @param x
     * @param workbook
     */
    private static void countDetail(Row leaveRow, LocalDate firstDayOfMonth, int rowNum, String x, Workbook workbook) {
        Cell aCell = leaveRow.createCell(firstDayOfMonth.getDayOfMonth());
        aCell.setCellFormula("COUNTIFS(" + convertToExcelColumn(firstDayOfMonth.getDayOfMonth() + 1) + "2:" + convertToExcelColumn(firstDayOfMonth.getDayOfMonth() + 1) + rowNum + x);
        aCell.setCellStyle(centeredStyle(workbook));
    }

    private void shiftCell(Workbook workbook, Row row, int dayOfMonth, String shift) {
        Cell cell = row.createCell(dayOfMonth);
        cell.setCellValue(shift);
        cell.setCellStyle(centeredStyle(workbook));
    }

    /**
     * 建立置中樣式
     * @param workbook
     * @return
     */
    private static CellStyle centeredStyle(Workbook workbook) {
        CellStyle centeredStyle = workbook.createCellStyle();
        centeredStyle.setAlignment(HorizontalAlignment.CENTER);
        centeredStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        return centeredStyle;
    }

    /**
     * 建立輪休樣式
     * @param workbook
     * @return
     */
    private static CellStyle rotatingLeaveStyle(Workbook workbook) {
        CellStyle rotatingLeaveStyle = workbook.createCellStyle();
        rotatingLeaveStyle.setAlignment(HorizontalAlignment.CENTER);
        rotatingLeaveStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        Font font = workbook.createFont();
        font.setColor(IndexedColors.RED.getIndex());
        rotatingLeaveStyle.setFont(font);

        return rotatingLeaveStyle;
    }

    /**
     * 建立必休樣式
     * @param workbook
     * @return
     */
    private static CellStyle requireLeaveStyle(Workbook workbook) {
        CellStyle requireLeaveStyle = workbook.createCellStyle();
        requireLeaveStyle.setAlignment(HorizontalAlignment.CENTER);
        requireLeaveStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        requireLeaveStyle.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
        requireLeaveStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        return requireLeaveStyle;
    }

    /**
     * 建立星期樣式
     * @param workbook
     * @return
     */
    private static CellStyle dayOfWeekCellCellStyle(Workbook workbook) {
        CellStyle dayOfWeekCellCellStyle = workbook.createCellStyle();

        dayOfWeekCellCellStyle.setAlignment(HorizontalAlignment.CENTER);
        dayOfWeekCellCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        dayOfWeekCellCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        dayOfWeekCellCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        Font font = workbook.createFont();
        font.setColor(IndexedColors.RED.getIndex());
        dayOfWeekCellCellStyle.setFont(font);

        return dayOfWeekCellCellStyle;
    }

    private static String convertToExcelColumn(int number) {
        StringBuilder column = new StringBuilder();

        while (number > 0) {
            int remainder = (number - 1) % 26;
            char columnChar = (char) ('A' + remainder);
            column.insert(0, columnChar);
            number = (number - 1) / 26;
        }

        return column.toString();
    }
}
