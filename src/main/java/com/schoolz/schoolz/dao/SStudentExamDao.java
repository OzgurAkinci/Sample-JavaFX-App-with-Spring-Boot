package com.schoolz.schoolz.dao;

import com.schoolz.schoolz.entity.SStudentExam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SStudentExamDao extends JpaRepository<SStudentExam, Integer> {

}
