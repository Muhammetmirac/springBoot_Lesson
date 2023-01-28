package com.tpe.security.service;

import com.tpe.domain.Role;
import com.tpe.domain.User;
import com.tpe.exception.ResourceNotFoundException;
import com.tpe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    //Bu classta amacım : 1- User ----->  USerDetails(User) ve Role ---->> grantedAuthorities(Role) dönüşümünü yapmaktır
    @Autowired
    private UserRepository userRepository;




/*
bu methodda bizim objelerimizi security nin anlayacağı yapıya dönüştürüyoruz

Bizim userName mizi alıyor ve security nin anlayacağı UserDetails in name ine ceviriyor
User Details in oluşması için 3 datayı maplememiz gerekir;
        1-Username--> domainden doğrudan alırız
        2-password--> domainden doğrudan alırız
        3-roles--> roles GrantedAuthority çevrilip maplenmesi gerektiği için bu işlemi ek bir metodlar yapacağız
 */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {  //
       User user = userRepository.findByUserName(username).orElseThrow(()->
               new ResourceNotFoundException("User not found username : "+username));

       if (user != null){
           return new org.
                   springframework.
                   security.
                   core.
                   userdetails.
                   User(user.getUserName(),  // User security kutuphanesinden gelen bizim oluştruduğumuz class değil
                        user.getPassword(),
                   buildGrantedAuthorities(user.getRoles()));  // burası kalabalık olmasın diye methodu aşağıda oluşturup çağırdık
       } else {
           throw new UsernameNotFoundException("user not found username : " + username);
       }

    }
    /*
    bu methodda parametre olarak gelen Role objelerini GrandAuthority ye dönüştürmek
    --bu methodda user objenin iki rolü varsa List yapısında  2 adet GrantedAuthority ye cevirip gönderiyor.
     */
    private static List<SimpleGrantedAuthority> buildGrantedAuthorities(final Set<Role> roles){ // final yazmamızın nedeni setlenmeden kullanılmasını istemediğimizden
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (Role role :roles) {
            authorities.add( new SimpleGrantedAuthority(role.getName().name()));// role.getName().name() yazmamızın nedeni Enums dan gelen yapının ROLE_STUDENT şeklinde gelmesini istememiz

        }

        return authorities;
    }
}