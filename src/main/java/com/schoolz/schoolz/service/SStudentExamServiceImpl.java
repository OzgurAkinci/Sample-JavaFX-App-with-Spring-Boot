package com.schoolz.schoolz.service;


import com.schoolz.schoolz.dao.SStudentExamDao;
import com.schoolz.schoolz.entity.SStudentExam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SStudentExamServiceImpl implements SStudentExamService {
    @Autowired
    SStudentExamDao sStudentExamDao;

    @Override
    public List<SStudentExam> findAll() {
        return sStudentExamDao.findAll();
    }

    @Override
    public SStudentExam save(SStudentExam sStudentExam) {
        return sStudentExamDao.save(sStudentExam);
    }
}
