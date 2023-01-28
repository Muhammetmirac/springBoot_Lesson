package com.tpe.controller;

import com.tpe.dto.UserRequest;
import com.tpe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController // RestFulAPI yapısı oldugunu bildirmiş oluyoruz
public class UserController {


    @Autowired
    private UserService userService;

    @PostMapping
    @RequestMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody UserRequest userRequest){ // User classının içerisinde "id" bilgisi vardr.
                                                        //Register işlemi için id dışındaki bilgiler gereklidir.
                                                        //O yüzden DTO classı olan UserRequest classını oluşturduk ve lazım olan fieldleri tanımladık
                                                        //DTO classların sonuna DTO her zaman eklenmez.
        userService.saveUser(userRequest);

        String myResponse = "Kullanıcı kaydı başarıyla gerçekleşmiştir";
        return ResponseEntity.ok(myResponse); // ya da new ResponseEntity<>(myResponse,HttpStatus.CREATED) de olur

    }
}
