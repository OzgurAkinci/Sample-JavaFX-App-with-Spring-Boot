package com.schoolz.schoolz.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;


@Entity
@Table(name="S_STUDENT_EXAM")
public class SStudentExam {

    @Id
    @GenericGenerator(name = "s_student_exam_id_seq", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {@org.hibernate.annotations.Parameter(name = "hibernate_sequence", value = "s_student_exam_id_seq"),
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "s_student_exam_id_seq")}
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "s_student_exam_id_seq")
    @Column(name = "id")
    private Integer id;

    @Column(name = "score")
    private Integer score;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private SStudent sStudent;

    @ManyToOne
    @JoinColumn(name = "exam_id", nullable = false)
    private SExam sExam;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public SStudent getsStudent() {
        return sStudent;
    }

    public void setsStudent(SStudent sStudent) {
        this.sStudent = sStudent;
    }

    public SExam getsExam() {
        return sExam;
    }

    public void setsExam(SExam sExam) {
        this.sExam = sExam;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
