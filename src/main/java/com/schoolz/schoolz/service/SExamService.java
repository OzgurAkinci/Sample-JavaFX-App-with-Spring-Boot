package com.schoolz.schoolz.service;

import com.schoolz.schoolz.entity.SExam;

import java.util.List;

public interface SExamService {
    List<SExam> findAll();
    SExam save(SExam sExam);
    SExam findById(Integer id);
    List<SExam> findByExamCode(String examCode);
}
