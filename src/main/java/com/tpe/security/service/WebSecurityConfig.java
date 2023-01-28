package com.tpe.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
// method seviyesinde security katmanın çalıştırmak istiyorum.  örneğin bazı metodları admin kullanabilsin

public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailService;


    /*
    csrf ---> browserları kullanırken art niyetli yazılımlar ile aynı anda acık sekmelerde erişim sağlayıp o esnada banka işlemleri gibi açık olan sayfalarda
    işlem yapılması
    --> bu güvenlik açığından dolayı REstFull API da default da açık iken update işlemi gerçekleşmiyor. bizde disable hale getiriyoruz
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().// csrf i etkisizleştir
                authorizeHttpRequests(). // Gelen requesti yetki kontolu yap
                antMatchers("/", "index.html", "/css/*", "/js/*","/register"). // muaf tutulmasını istediğimiz requestleri belirttim
                permitAll().    // path bazlı spring securtiy istisnası tanımladık. ust satırdaki pathler ile request geldiğinde şifre istenmeyecek
               and().
                authorizeRequests().antMatchers("/students/**").hasRole("ADMIN").
                anyRequest(). //hangisi gelirse gelsin
                authenticated(). //authenticat et
                and().
                httpBasic();// Basic Auth old bildiriyoruz
    }

    @Bean
    public PasswordEncoder passwordEncoder() { //PasswordEncoder security nin kendi classıdır
        return new BCryptPasswordEncoder(10);// parantez içerisindeki değer(4-31 arası) ne kadar yüksekse şifre zorlaşıyor encode süresi uzuyor. 15 üstü oluşturması uzun sürer. 10-11-12 iyidir

    }


    /*
     ilgili pdf dosyasında 4. ve 9. adımda gösterildiği gibi
     provider e 2 şeyi tantırız
    1- UserDetailsServici---->
    2- PasswordEncoder --> bir üst metodda tanımladık 62. satırda tanıtıyoruz

     */
    @Bean
    public DaoAuthenticationProvider authProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider());

    }
}
