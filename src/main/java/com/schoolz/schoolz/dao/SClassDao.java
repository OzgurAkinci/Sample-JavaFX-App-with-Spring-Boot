package com.schoolz.schoolz.dao;

import com.schoolz.schoolz.entity.SClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SClassDao extends JpaRepository<SClass, Integer> {

}
