package com.SpringBootExcelExport.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.SpringBootExcelExport.helper.helper;
import com.SpringBootExcelExport.model.Course;
import com.SpringBootExcelExport.repository.CourseRepository;

@Service
public class ReportService {

    @Autowired
    private CourseRepository courseRepo;

    // Save data from an uploaded Excel file to the database
    public void save(MultipartFile file) {
        try {
            List<Course> courses = helper.convertExceltoListOfCourse(file.getInputStream());
            this.courseRepo.saveAll(courses);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Retrieve all courses from the database
    public List<Course> getallCourses() {
        return this.courseRepo.findAll();
    }

    // Generate an Excel report and write it to the HTTP response
    public void generateExcel(HttpServletResponse response) {
        try {
            List<Course> courses = courseRepo.findAll();

            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("Course Info");
            HSSFRow row = sheet.createRow(0);

            row.createCell(0).setCellValue("ID");
            row.createCell(1).setCellValue("NAME");
            row.createCell(2).setCellValue("FEES");

            int dataRowIndex = 1;

            for (Course c : courses) {
                HSSFRow dataRow = sheet.createRow(dataRowIndex);
                dataRow.createCell(0).setCellValue(c.getCid());
                dataRow.createCell(1).setCellValue(c.getCname());
                dataRow.createCell(2).setCellValue(c.getCfees());

                dataRowIndex++;
            }

            ServletOutputStream ops = response.getOutputStream();
            workbook.write(ops);
            workbook.close();
            ops.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
