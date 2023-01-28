package com.tpe.service;

import com.tpe.domain.*;
import com.tpe.domain.enums.*;
import com.tpe.dto.*;
import com.tpe.repository.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.crypto.password.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private RoleService roleService; // User kaydedilirken rolunu setlenmesi için enjekte edildi

    @Autowired
    private PasswordEncoder passwordEncoder; // passwrod encode edileceği  passwordEncoder emjekte edildi

    @Autowired
    private UserRepository userRepository;// katmanlı yapıda service in habeleşeceği katman repository olduğu için enjekte edildi

    public void saveUser(UserRequest userRequest) {
        User myUser = new User(); // Biz dto classı ile kullanıcıdan gelen bilgileri DB göndermek için kendi entity clasımız olan User objesi oluşturuluyor
        myUser.setFirstName(userRequest.getFirstName()); // fieldleri set ediyoruz
        myUser.setLastName(userRequest.getLastName());
        myUser.setUserName(userRequest.getUserName());
        // myUser.setPassword(userRequest.getPassword());  // password encode edilmeden setlenmemeli
        String password = userRequest.getPassword();
        String encodedPassword = passwordEncoder.encode(password);

        myUser.setPassword(encodedPassword); // password encode edilerek setlendi

        // Role setlenmeli
        Role role = roleService.getRoleByType(UserRole.ROLE_ADMIN);
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        myUser.setRoles(roles);

        userRepository.save(myUser);
    }
}


/*

 */