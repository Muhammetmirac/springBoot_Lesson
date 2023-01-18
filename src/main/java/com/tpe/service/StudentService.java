package com.tpe.service;

import com.tpe.domain.Student;
import com.tpe.dto.StudentDTO;
import com.tpe.exception.ConflictException;
import com.tpe.exception.ResourceNotFoundException;
import com.tpe.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService { // mavc dersinde interface kullanmıştık ancak burada aradaki farkı görnek için inteface kullanmadık.

    @Autowired
    private StudentRepository studentRepository;

    public List<Student> getAll() {
        return studentRepository.findAll(); // findAll() arka planda "select * from Student"
    }

    public void createStudent(Student student) {
        if (studentRepository.existsByEmail(student.getEmail())) {  // existBY...() methodu üzerinde srediğimiz field ile manupulasyon yapabiliriz
            // burada da ...Email ekledik ve methodumuzu kullanılabilir hale getirdik
            throw new ConflictException("Email is already exist");   // kendimiz exception oluşturduk email kullanılıyorsa hatası için
        }
        studentRepository.save(student);
    }

    public Student findStudent(Long id) {
        return studentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Student not found with id : " + id));

    }

    public void deleteStudent(Long id) {
        Student student = findStudent(id);
        studentRepository.delete(student);
    }

    /*
    service katmanı controller ve repository arasında guvenliği sağlamak adına DTO claslarına ihtiyaç duyar
    controller ile DTO classları ile haberleşir
    repository ile ise POJO classımızlar haberleşir
    aşağıdaki methodda studentDTO.getEmail()-->> controller katmanına gelen kullanıcının girmiş olduğu datadır
                       student.getEmail()----->>> bizim pojo classında oluşturmuş olduğumuz ve repository üzerinden DB den gelen datadır
     */
    public void updateStudent(Long id, StudentDTO studentDTO) {
        boolean existEmail = studentRepository.existsByEmail(studentDTO.getEmail());
        Student student = findStudent(id);

        if (existEmail && !studentDTO.getEmail().equals(student.getEmail())) {  // && operatoru mukemmeliyeci oldugundan true&&true olması lazım "!" dikkat etmek gerekir
            throw new ConflictException("Email is already exist ");
            /*
                             POJO    DTO
            1. kendi email : mrc ,   mrc  -->  TRUE && FALSE  ( UPDATE OLUR )
            2. kendi email : mrc ,  ahmt ve DB de zaten var --> TRUE && TRUE
            3. kendi email : mrc,   mhmt ama DB de yok  ---> FALSE && TRUE

             */
        }
        student.setName(studentDTO.getFirstName());
        student.setLastName(studentDTO.getLastName());
        student.setGrade(studentDTO.getGrade());
        student.setEmail(studentDTO.getEmail());
        student.setPhoneNumber(studentDTO.getPhoneNumber());

        studentRepository.save(student);

    }

    public Page<Student> getAllWithPage(Pageable pageable) {
        return studentRepository.findAll(pageable);
    }

    public List<Student> findByLastName(String lastname) {
        return studentRepository.findByLastName(lastname);
    }

    public List<Student> findAllEqualsGrade(Integer grade) {
        return studentRepository.findAllEqualsGrade(grade);
    }

    public StudentDTO findStudentDTOById(Long id) {
        return studentRepository.findStudentDTOById(id).orElseThrow(() ->
                new ResourceNotFoundException("Student not found with id : " + id));
    }

}