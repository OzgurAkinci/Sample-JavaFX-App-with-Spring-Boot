package com.schoolz.schoolz.dao;

import com.schoolz.schoolz.entity.SExam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SExamDao extends JpaRepository<SExam, Integer> {
    List<SExam> findByExamCode(String examCode);

}
