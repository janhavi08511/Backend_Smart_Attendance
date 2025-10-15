<<<<<<< HEAD
package com.example.smart_attendance.service;

import com.example.smart_attendance.model.AttendenceRecord;
import com.example.smart_attendance.model.PresentStudent;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class ExcelExportService {

    public byte[] exportToExcel(AttendenceRecord attendanceRecord) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Attendance Report");

        // Header
        Row headerRow = sheet.createRow(0);
        String[] columns = {"Roll No", "Name", "Marked At"};
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
        }

        // Data
        int rowNum = 1;
        for (PresentStudent student : attendanceRecord.getPresentStudents()) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(student.getRollNo());
            row.createCell(1).setCellValue(student.getName());
            row.createCell(2).setCellValue(student.getMarkedAt().toString());
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        return outputStream.toByteArray();
    }
=======
package com.example.smart_attendance.service;

import com.example.smart_attendance.model.AttendenceRecord;
import com.example.smart_attendance.model.PresentStudent;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class ExcelExportService {

    public byte[] exportToExcel(AttendenceRecord attendanceRecord) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Attendance Report");

        // Header
        Row headerRow = sheet.createRow(0);
        String[] columns = {"Roll No", "Name", "Marked At"};
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
        }

        // Data
        int rowNum = 1;
        for (PresentStudent student : attendanceRecord.getPresentStudents()) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(student.getRollNo());
            row.createCell(1).setCellValue(student.getName());
            row.createCell(2).setCellValue(student.getMarkedAt().toString());
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        return outputStream.toByteArray();
    }
>>>>>>> 3373ec0d1e8d9aadfaab4b21072be139b3ff6bd5
}