package com.SpringBootExcelExport.helper;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import com.SpringBootExcelExport.model.Course;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class helper {

    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    // Chcek if the excel file or not
    public static boolean checkExcelFormat(MultipartFile file) {
        if (!TYPE.equals(file.getContentType())) {
            return false;
        }
        return true;
    }

    // Convert an Excel file (XLSX format) to a List of Course objects
    public static List<Course> convertExceltoListOfCourse(InputStream is) {

        List<Course> list = new ArrayList<>();

        try {

            XSSFWorkbook workbook = new XSSFWorkbook(is);
            XSSFSheet sheet = workbook.getSheet("Course Info");

            int rowNumber = 0;

            // Iterate through each row in the sheet
            Iterator<Row> iterator = sheet.iterator();

            while (iterator.hasNext()) {
                Row row = iterator.next();

                // Skip the first row (header row)
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                // Iterate through each cell in the row
                Iterator<Cell> cells = row.iterator();

                int cid = 0;
                Course course = new Course();

                // Process each cell based on its column index
                while (cells.hasNext()) {
                    Cell cell = cells.next();

                    switch (cid) {
                        case 0:
                            course.setCid((long) cell.getNumericCellValue());
                            break;
                        case 1:
                            course.setCname(cell.getStringCellValue());
                            break;
                        case 2:
                            course.setCfees((double) cell.getNumericCellValue());
                            break;
                        default:
                            break;

                    }
                    cid++;
                }

                list.add(course);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

}

// The helper class provides utility methods for checking the Excel file format
// and converting an Excel file into a list of Course objects.
