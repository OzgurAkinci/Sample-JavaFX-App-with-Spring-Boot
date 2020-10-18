package com.schoolz.schoolz.service;

import com.schoolz.schoolz.entity.SStudent;

import java.util.List;

public interface SStudentService {
    List<SStudent> findAll();
    SStudent save(SStudent sStudent);
    SStudent findById(Integer id);
    List<SStudent> findByStudentNumber(String studentNumber);
}
