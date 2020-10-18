package com.schoolz.schoolz.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="S_CLASS")
public class SClass implements Serializable {
    @Id
    @GenericGenerator(name = "s_class_id_seq", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {@org.hibernate.annotations.Parameter(name = "hibernate_sequence", value = "s_class_id_seq"),
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "s_class_id_seq")}
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "s_class_id_seq")
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

/*    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "S_EXAM_CLASS",
            joinColumns = { @JoinColumn(name = "exam_id") },
            inverseJoinColumns = { @JoinColumn(name = "class_id") }
    )
    Set<SExam> sExams = new HashSet<>();*/
    @ManyToMany(mappedBy = "sClasses", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.EAGER)
    private Set<SExam> sExams = new HashSet<>();

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

    @Override
    public String toString() {
        return name;
    }

    public Set<SExam> getsExams() {
        return sExams;
    }

    public void setsExams(Set<SExam> sExams) {
        this.sExams = sExams;
    }
}
