package com.schoolz.schoolz.service;


import com.schoolz.schoolz.dao.SExamDao;
import com.schoolz.schoolz.entity.SExam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SExamServiceImpl implements SExamService {
    @Autowired
    SExamDao sExamDao;

    @Override
    public List<SExam> findAll() {
        return sExamDao.findAll();
    }

    @Override
    public SExam save(SExam sExam) {
        return sExamDao.save(sExam);
    }

    @Override
    public SExam findById(Integer id) {
        return sExamDao.findById(id).get();
    }

    @Override
    public List<SExam> findByExamCode(String examCode) {
        return sExamDao.findByExamCode(examCode);
    }
}
