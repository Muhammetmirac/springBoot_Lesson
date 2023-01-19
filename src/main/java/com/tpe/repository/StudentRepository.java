package com.tpe.repository;

import com.tpe.domain.Student;
import com.tpe.dto.StudentDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/*
@Repository koymasak bile çalışır çünkü jpaRepository ye extend ettik
 */
/*
Repository interface olmalıdır ve JpaRepository interface ini extend etmelidir
extend edilen interface e de POJO classımız ve onun id data tipini belirtmemiz gerekir
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    boolean existsByEmail(String email);
    // Spring Data JPA içinde existById() var fakat Spring Data JPA bize sondaki eki istediğimiz
// değişken ismi ile değiştirmemize izin veriyor, mevcut metodu bu şekilde türetebiliyoruz.

    List<Student> findByLastName(String lastname);


    //*** JPQL  *******
    @Query("SELECT s from Student s where s.grade = :pGrade")//:pGrade şeklinde yazıldıgında dinamik bir yapı oluşturmus oluruz
                                                              //istediğimiz değeri girebiliriz
    // JPQL sorgusu yapacağımız için @Query anostasyonunu koyduk      Student ---> s olarak kullanacagız
    List<Student> findAllEqualsGrade(@Param("pGrade") Integer grade);


    //native DQL ile
    @Query(value = "select * from Student s where s.grade = :pGrade ", nativeQuery = true) //value yazmayı unutmuyoruz
    List<Student> findAllEqualsGradeWithSQL(@Param("pGrade") Integer grade);  //@Param anostasyonu ile Integer grade değerini al pGrade değerine setle diyoruz

    //jpql
    @Query("SELECT new com.tpe.dto.StudentDTO(s) FROM Student s WHERE s.id=:id ")   // burada StudentDTO(s---> parametreli constructor içerisindeki 'student' ı temsil ediyor )
                                                                    // dto classımızın yerini tanıtıp DB den gelen pojo classımızı dto classımıza setle ve controller e bu bilgiler ile gönder diyoruz
    Optional<StudentDTO> findStudentDTOById(@Param("id") Long id);

}
