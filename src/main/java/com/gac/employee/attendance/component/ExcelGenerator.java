package com.gac.employee.attendance.component;

import com.gac.employee.attendance.model.AttendanceRecordModel;
import com.gac.employee.attendance.repo.EmployeeRepository;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class ExcelGenerator {
    private List<AttendanceRecordModel> AttendanceList;
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    @Autowired
    private EmployeeRepository employeeRepository;

    public ExcelGenerator(List<AttendanceRecordModel> AttendanceList) {
        this.AttendanceList = AttendanceList;
        workbook = new XSSFWorkbook();
    }

    private void writeHeader() {
        sheet = workbook.createSheet("Student");
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);

        createCell(row, 0, "ID", style);
        createCell(row, 1, "user SN", style);
        createCell(row, 2, "user ID", style);
        createCell(row, 3, "Name", style);
        createCell(row, 4, "verify Type", style);
        createCell(row, 5, "record Time", style);
        createCell(row, 6, "verify State", style);

    }

    private void createCell(Row row, int columnCount, Object valueOfCell, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (valueOfCell instanceof Integer) {
            cell.setCellValue((Integer) valueOfCell);
        } else if (valueOfCell instanceof Long) {
            cell.setCellValue((Long) valueOfCell);
        } else if (valueOfCell instanceof String) {
            cell.setCellValue((String) valueOfCell);
        } else {
            cell.setCellValue((Boolean) valueOfCell);
        }
        cell.setCellStyle(style);
    }

    private void write() {
        int rowCount = 1;
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);
        for (AttendanceRecordModel record : AttendanceList) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++, record.getRecordId(), style);
            createCell(row, columnCount++, record.getUserSN(), style);
            createCell(row, columnCount++, record.getUserID(), style);
            createCell(row, columnCount++, employeeRepository.findByEmployeeId(Integer.parseInt(record.getUserID())), style);
            createCell(row, columnCount++, record.getVerifyType(), style);
            createCell(row, columnCount++, record.getRecordTime(), style);
            createCell(row, columnCount++, record.getVerifyState(), style);

        }
    }

    public void generateExcelFile(HttpServletResponse response) throws IOException {
        writeHeader();
        write();
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}