package com.tpe.dto;

import com.tpe.domain.Student;
import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter //--- burada yazarsak class base(seviye) uygulama olur
@Setter
@AllArgsConstructor // dolu constructorları yazma zahmetinden kurtuluyoruz
@NoArgsConstructor // parametresiz cons yazmıyoruz
public class StudentDTO {
    private Long id;

    @NotNull(message = "first name cannot be null")
    @NotBlank(message = "first name cannot be white space")
    @Size(min = 2 , max = 25,
            message = "first name '${validatedValue}' must be between {min} and {max} long")

    private  String firstName;

     private String lastName;

    private  Integer grade;

    @Email(message = "Provide valid email")// geçerli bir mail adresi giriniz xxx@yyy.zzz yapısını kontrol eder
   private String email;

    private  String phoneNumber;


    private LocalDateTime createDate= LocalDateTime.now();



    public StudentDTO(Student student){
        this.id=student.getId();
        this.firstName=student.getName();
        this.lastName = student.getLastName();
        this.email= student.getEmail();
        this.grade=student.getGrade();
        this.phoneNumber=student.getPhoneNumber();
        this.createDate=student.getCreateDate();
    }

}
