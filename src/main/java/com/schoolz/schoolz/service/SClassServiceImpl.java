package com.schoolz.schoolz.service;


import com.schoolz.schoolz.dao.SClassDao;
import com.schoolz.schoolz.entity.SClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SClassServiceImpl implements SClassService {
    @Autowired
    SClassDao sClassDao;

    @Override
    public List<SClass> findAll() {
        return sClassDao.findAll();
    }

    @Override
    public SClass findById(Integer id) {
        return sClassDao.findById(id).get();
    }

    @Override
    public SClass save(SClass sClass) {
        return sClassDao.save(sClass);
    }
}
