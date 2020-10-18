package com.schoolz.schoolz.service;

import com.schoolz.schoolz.entity.SClass;

import java.util.List;

public interface SClassService {
    List<SClass> findAll();
    SClass findById(Integer id);
}
