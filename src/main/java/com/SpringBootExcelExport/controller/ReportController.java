package com.SpringBootExcelExport.controller;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import com.SpringBootExcelExport.model.Course;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.SpringBootExcelExport.helper.helper;
import com.SpringBootExcelExport.service.ReportService;

@RestController
@CrossOrigin("*")
@RequestMapping("/courses")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) {
        if (helper.checkExcelFormat(file)) {
            // true
            this.reportService.save(file);
            
            return ResponseEntity.ok(Map.of("message", "File is uploaded and data saved to db"));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please upload excel file only");
    }

    // @GetMapping("")
    // public List<Course> getallCourses() {
    // return this.reportService.getallCourses();
    // }

    @GetMapping("")
    public ResponseEntity<List<Course>> getallCourses() {
        try {
            List<Course> courses = reportService.getallCourses();
            if (courses.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(courses, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/download")
    public void generateExcelReport(HttpServletResponse response) throws Exception {
        // As method is responsible to download the excel file

        // This line sets the HTTP response's content type to application/octet-stream.
        // This content type is typically used for binary files, including Excel files.
        // It tells the client that the response is a downloadable file.
        response.setContentType("application/octet-stream");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename = courses.xls";
        // . This header instructs the browser to treat the response as an attachment
        // that should be downloaded, and it suggests the default filename for the
        // downloaded file, which is "courses.xls".

        response.setHeader(headerKey, headerValue);
        // This header, when received by the client's browser, prompts it to download
        // the response content as a file with the suggested filename.

        reportService.generateExcel(response);
    }
}

// endpoints for uploading Excel files, retrieving data from the database, and
// generating Excel reports
