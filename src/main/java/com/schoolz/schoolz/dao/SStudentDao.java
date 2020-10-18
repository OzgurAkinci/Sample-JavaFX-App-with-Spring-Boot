package com.schoolz.schoolz.dao;

import com.schoolz.schoolz.entity.SStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SStudentDao extends JpaRepository<SStudent, Integer> {

    List<SStudent> findByStudentNumber(String studentNumber);

}
