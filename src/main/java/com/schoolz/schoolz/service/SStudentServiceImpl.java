package com.schoolz.schoolz.service;


import com.schoolz.schoolz.dao.SStudentDao;
import com.schoolz.schoolz.entity.SStudent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SStudentServiceImpl implements SStudentService {
    @Autowired
    SStudentDao sStudentDao;

    @Override
    public List<SStudent> findAll() {
        return sStudentDao.findAll();
    }

    @Override
    public SStudent save(SStudent sStudent) {
        return sStudentDao.save(sStudent);
    }

    @Override
    public SStudent findById(Integer id) {
        return sStudentDao.findById(id).get();
    }

    @Override
    public List<SStudent> findByStudentNumber(String studentNumber) {
        return sStudentDao.findByStudentNumber(studentNumber);
    }
}
