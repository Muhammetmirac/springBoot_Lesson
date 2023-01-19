package com.tpe.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/*
domain package in içerisinde entity classlarımızı oluştururuz

 */
@Entity
@Getter //--- burada yazarsak class base(seviye) uygulama olur
@Setter
@AllArgsConstructor // dolu constructorları yazma zahmetinden kurtuluyoruz
//@RequiredArgsConstructor()      // fieldlerin önüne final yazarak hangi constructorlar istediğimizi belirtmiş oluyoruz
@NoArgsConstructor // parametresiz cons yazmıyoruz
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE) // setlemenin önüne geçmiş oluyoruz
    //@Getter field seviye kullanmış oluyoruz
    private Long id;

    //    @NotNull(message = "First name can not be null")//sadece null olmasın,"" olabilir," " olabilir.
    //    @NotBlank(message = "First name can not be space")//null olamaz,empty olamaz,boşluk olamaz
    //    @NotEmpty//null olamaz,empty olamaz,boşluk olabilir
    @NotNull(message = "first name cannot be null") //isim null olmasın
    @NotBlank(message = "first name cannot be white space") //Kullanıcı isimde boşluk bırakamasın
    @Size(min = 2 , max = 25,
            message = "first name '${validatedValue}' must be between {min} and {max} long") // kullanıcıya isim sınırlandırması getiriyoruz. uymaması durumunda mesaj yazıyoruz
  @Column(nullable = false, length = 25) // normalde burada yukarıda yaptıgımız işlemleri(validation işlemler) yapıyoruz ancak son katmana gitmeden çift kontrol olsun diye belirtiyoruz
   /*final*/ private  String name;

    //@Getter field seviye getter sadece bu field için getter olusturmus olduk
    @Column(nullable = false, length = 25)             //---> bu anostasyon opsiyonel içinde birşey doldurmayacaksak yazmasak da tabloda column oluşur
    /*final*/ private String lastName;

    /*final*/  private  Integer grade;

    @Column(nullable = false, length = 50, unique = true)
    @Email(message = "Provide valid email")// geçerli bir mail adresi giriniz xxx@yyy.zzz yapısını kontrol eder
    /*final*/  private String email;

    /*final*/ private  String phoneNumber;

    @Setter(AccessLevel.NONE) // bu anostasyon ile setlemenın önüne geçiyoruz
    private LocalDateTime createDate= LocalDateTime.now();


    @OneToMany(mappedBy = "student")

    private List<Books> books = new ArrayList<>();


    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;





}
