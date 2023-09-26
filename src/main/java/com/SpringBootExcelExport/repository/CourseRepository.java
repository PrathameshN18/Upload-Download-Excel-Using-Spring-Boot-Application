package com.SpringBootExcelExport.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.SpringBootExcelExport.model.Course;

public interface CourseRepository extends JpaRepository<Course, Serializable> {

}
