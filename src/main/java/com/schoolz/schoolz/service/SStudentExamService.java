package com.schoolz.schoolz.service;

import com.schoolz.schoolz.entity.SStudentExam;

import java.util.List;

public interface SStudentExamService {
    List<SStudentExam> findAll();
    SStudentExam save(SStudentExam sStudentExam);
}
