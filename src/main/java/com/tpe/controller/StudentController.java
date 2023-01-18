package com.tpe.controller;

import com.tpe.domain.Student;
import com.tpe.dto.StudentDTO;
import com.tpe.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController  // Restful API yapısını kullandığımızı belirtmiş oluyoruz
@RequestMapping("/students") //http://localhost:8080/studens
public class StudentController {

//loglama ile ilgili objemizi oluşturuyoruz
  Logger logger =  LoggerFactory.getLogger(StudentController.class); //logger objesi olusturduk ve hangi classlar ilgili yapacagıızı class ismini yazarak tanımladık





    /*
 end pointlerimizi oluşturuyoruz
  */


    @Autowired // basit yapıda ilerlediğimiz için  field injection kullandık
    private StudentService studentService;


    //bütün öğrenciler gelsin

    @GetMapping() //http://localhost:8080/studens +GET()
    public ResponseEntity<List<Student>> getAll() {  // respons yani dönen datalar ; student verileri ve http status kodları olur bunu da ResponseEntity ile sağlıypruz
        List<Student> students = studentService.getAll();
        return ResponseEntity.ok(students);// ok--> 200 kodunu http status kodu olarak gönderir
    }


    // Student objesi oluşturalım
    /*
    yeni birşey create edeceksem post() kullanırız. güncellemek istiyorsak put() kullanılır
     */
    @PostMapping
    // //http://localhost:8080/studens +POST + JSON  anostasyonlardan dolayı POST olduğunu, Student objesi oluşacağı için json formatında Student fieldleri gelmesi lazım
    public ResponseEntity<Map<String, String>> createStudent(@Valid @RequestBody Student student) {  //@RequestBody ile json dataları Student student objeme maple diyoruz
        //@Valid ----> null mu, email formatına uygun mu gibi  belirtmiş olduğum yapılara uygun mu kontrolu yapmaya yarar
        // @Valid : parametreler valid mi kontrol eder, bu örenekte Student
        //objesi oluşturmak için  gönderilen fieldlar yani
        //name gibi özellikler düzgün set edilmiş mi ona bakar.
        // @RequestBody = gelen parametreyi, requestin bodysindeki bilgisi  ki burada JSON formatında geliyor
        //Student objesine map edilmesini sağlıyor.
        studentService.createStudent(student);

        Map<String, String> map = new HashMap<>(); // spring mantıgında newlememek vardı ancak burada sadece bu scope içerisinde 1 defa kullanacagımız için newledik.
        map.put("message", "Student is created successfuly");
        map.put("status", "true");

        return new ResponseEntity<>(map, HttpStatus.CREATED);// HttpStatus.CREATED--> 201  kodu oluyor
    }


    //belli id ile öğrenci getirelim @RequestParam ile

    @GetMapping("/query") //http://localhost:8080/studens/query?id=1 end pointimiz
    public ResponseEntity<Student> getStudent(@RequestParam("id") Long id) { // burada sadece id üzerinden sorgu yazacağımız için @RequestParam("id") method içerisine id yazmasakta olur
        Student student = studentService.findStudent(id);
        return ResponseEntity.ok(student);

        /*
        @request param ve @path variable aynı işlemi yapar.
        Ancak birden fazla field ile işlem yapılacaksa @requestParam tercih edilir.
        Çünkü adres satırı(endPoint) incelendiğinde @requestParam da hangi field ile eşleştiği görülür.
        Ancak @pathvariable da sadece id bilgisi ya da hangi field ise o görülür
         */


    }

    // !!! Id ile öğrenci getirelim @PathVariable ile
    @GetMapping("{id}") // http://localhost:8080/students/1
    public ResponseEntity<Student> getStudentWithPath(@PathVariable("id") Long id) {
        Student student = studentService.findStudent(id);
        return ResponseEntity.ok(student);

    }


    // !!! Delete
    @DeleteMapping("/{id}") // http://localhost:8080/students/1  + DELETE
    public ResponseEntity<Map<String, String>> deleteStudent(@PathVariable("id") Long id) {  // süslü parantez içerisinde field belirtilmişse buradan @PathVariable kullanılacağını anlarız

        studentService.deleteStudent(id);

        Map<String, String> map = new HashMap<>();
        map.put("message", "Student is deleted successfuly");
        map.put("status", "true");

        return new ResponseEntity<>(map, HttpStatus.OK); // return ResponseEntity.ok(map);
        /*
         return dan sonra yazılan her iki data tipide aynı işi yapar
         spring mühendisleri HttpStatus.OK yapısı çok kullanıldığı için ve "return new ResponseEntity<>(map, HttpStatus.OK)" yapısında newleme olduğu için
         bunun yerine //return ResponseEntity.ok(map); yapısını oluşturmuşlardır.
         */
    }


    //  update
    @PutMapping("{id}") // http://localhost:8080/students/1  + PUT + JSON
    public ResponseEntity<Map<String, String>> updateStudent(@PathVariable("id") Long id,
                                                             @Valid
                                                             @RequestBody StudentDTO studentDTO) {  // json datalar student objesine mapleniyor
        studentService.updateStudent(id, studentDTO);

        Map<String, String> map = new HashMap<>();
        map.put("message", "Student is updated successfuly");
        map.put("status", "true");

        return new ResponseEntity<>(map, HttpStatus.OK); // return ResponseEntity.ok(map);
    }
/*
verilen bilgiler ile update işleminde art niyetli ya da bilmeden de olabilir farklı id update ederek başkalarının dataları değiştirilebilir.
Güvenliği sağlamak ve hız için  service katmanından ikiye ayırırz.
service katmanını controller ile haberleşmesi  DTO ile  repository ile POJO clasları ile haberlesir

                                controller  <------------------- service ------------>repository
                                 DTO(data transfer object)                          POJO classlar
 */


    //PAGEABLE
    @GetMapping("/page")          // page yapısıyla bütün dataları al
    public ResponseEntity<Page<Student>> getAllWithPAge(@RequestParam("page") int page, // hangi page gönderilecek ... 0 dan başlar
                                                        @RequestParam("size") int size,  // page başı kaç student olacak
                                                        @RequestParam("sort") String prop,      // sıralama(sort) hangi fielda göre yapılacak
                                                        @RequestParam("direction") Sort.Direction direction) {   // dpğaş sıralı mı yoksa özel mi
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, prop));
        // bana pageable objesi lazım  ve pagereques.of methodunu çağırıyorum
        //

        Page<Student> studentPage = studentService.getAllWithPage(pageable);
        return ResponseEntity.ok(studentPage);
    }


    // get by lastName

    @GetMapping("/querylastname")
    public ResponseEntity<List<Student>> getStudentByLastName(@RequestParam("lastName") String lastname) {
        List<Student> list = studentService.findByLastName(lastname);

        return ResponseEntity.ok(list);

    }


    //get All student by grade JPQL (Java Persistence Query Language) ile
    @GetMapping("/grade/{grade}") // http://localhost:8080/students/grade/75  + GET
    public ResponseEntity<List<Student>> getStudentsEqualsGrade(@PathVariable("grade") Integer grade) {
        List<Student> list = studentService.findAllEqualsGrade(grade);
    return ResponseEntity.ok(list);
    }




    //DB den direk DTo olarak data alabilir miyim ?
    @GetMapping("/query/dto") // http://localhost:8080/students/query/dto?id=1
    public ResponseEntity<StudentDTO> getStudentDTO(@RequestParam("id") Long id){
      StudentDTO studentDTO =  studentService.findStudentDTOById(id);
      return ResponseEntity.ok(studentDTO);
    }


    @GetMapping("/welcome") //http://localhost:8080/students/welcome + GET
    public String welocame (HttpServletRequest request){    //HttpServletRequest ile requeste ulaştım
        logger.warn("--------------------------------- Welcome {}",request.getServletPath());
        return  "Student Controller a Hoş Geldiniz";
    }


}
