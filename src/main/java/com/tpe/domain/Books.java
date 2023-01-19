package com.tpe.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@NoArgsConstructor
public class Books {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonProperty("bookName")  // json formatındaki çıktıda isminin "bookName" olmasını sağladık. ancak bu sadece görselde önümüze gelir. kalıcı değildir
    private String name;

    //one student many book
    @JsonIgnore // json çıktıda sonsuz döngüye girilmesin diye ekledik

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Student getStudent() {
        return student;
    }
}
