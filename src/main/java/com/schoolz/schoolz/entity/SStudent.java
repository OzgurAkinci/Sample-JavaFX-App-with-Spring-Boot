package com.schoolz.schoolz.entity;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name="S_STUDENT")
public class SStudent {

    @Id
    @GenericGenerator(name = "s_student_id_seq", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {@org.hibernate.annotations.Parameter(name = "hibernate_sequence", value = "s_student_id_seq"),
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "s_student_id_seq")}
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "s_student_id_seq")
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(name = "student_number", nullable = false)
    private String studentNumber;

    @Column(name = "created_on", nullable = false)
    private Date createdOn;

    @Column(name = "birthday")
    @Type(type="date")
    private Date birthday;

    @Column(name = "gender")
    private String gender;

    @Column(name = "address")
    private String address;

    @OneToOne(cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "s_class_id", referencedColumnName = "id")
    private SClass sClass;

    @OneToMany(mappedBy = "sStudent", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    private Set<SStudentExam> sStudentExams;

    @Formula("(select avg(sse.score) from s_student e left join s_student_exam sse on e.id = sse.student_id where e.id = ID)")
    private Integer avgScore;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public SClass getsClass() {
        return sClass;
    }

    public void setsClass(SClass sClass) {
        this.sClass = sClass;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Set<SStudentExam> getsStudentExams() {
        return sStudentExams;
    }

    public void setsStudentExams(Set<SStudentExam> sStudentExams) {
        this.sStudentExams = sStudentExams;
    }

    public Integer getAvgScore() {
        return avgScore;
    }

    public void setAvgScore(Integer avgScore) {
        this.avgScore = avgScore;
    }
}
