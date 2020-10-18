package com.schoolz.schoolz.entity;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="S_EXAM")
public class SExam {
    @Id
    @GenericGenerator(name = "s_exam_id_seq", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {@org.hibernate.annotations.Parameter(name = "hibernate_sequence", value = "s_exam_id_seq"),
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "s_exam_id_seq")}
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "s_exam_id_seq")
    @Column(name = "id")
    private Integer id;

    @Column(name = "exam_code", nullable = false)
    private String examCode;

    @Column(name = "exam_date", nullable = false)
    private Date examDate;

    @Column(name = "exam_location")
    private String examLocation;

    @Column(name = "exam_time")
    private Integer examTime;

    @OneToMany(mappedBy = "sExam", cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private Set<SStudentExam> sStudentExams;

    /*@ManyToMany(mappedBy = "sExams", cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    private Set<SClass> sClasses = new HashSet<>();*/

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "S_EXAM_CLASS",
            joinColumns = { @JoinColumn(name = "exam_id") },
            inverseJoinColumns = { @JoinColumn(name = "class_id") }
    )
    Set<SClass> sClasses = new HashSet<>();

    @Formula("(select avg(sse.score) from s_exam e left join s_student_exam sse on e.id = sse.exam_id where e.id = ID)")
    private Integer avgScore;

    @Formula("(select count(*) from s_exam e left join s_student_exam sse on e.id = sse.exam_id where e.id = ID)")
    private Integer studentCount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getExamCode() {
        return examCode;
    }

    public void setExamCode(String examCode) {
        this.examCode = examCode;
    }

    public Date getExamDate() {
        return examDate;
    }

    public void setExamDate(Date examDate) {
        this.examDate = examDate;
    }

    public String getExamLocation() {
        return examLocation;
    }

    public void setExamLocation(String examLocation) {
        this.examLocation = examLocation;
    }

    public Integer getExamTime() {
        return examTime;
    }

    public void setExamTime(Integer examTime) {
        this.examTime = examTime;
    }

    public Set<SStudentExam> getsStudentExams() {
        return sStudentExams;
    }

    public void setsStudentExams(Set<SStudentExam> sStudentExams) {
        this.sStudentExams = sStudentExams;
    }

    public Set<SClass> getsClasses() {
        return sClasses;
    }

    public void setsClasses(Set<SClass> sClasses) {
        this.sClasses = sClasses;
    }

    public Integer getAvgScore() {
        return avgScore;
    }

    public Integer getStudentCount() {
        return studentCount;
    }

}
