package std.ach.studyolle.infra.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import lombok.RequiredArgsConstructor;
import std.ach.studyolle.infra.config.handlr.UserAuthenticationSuccessHandler;
import std.ach.studyolle.modules.user.UserService;

@RequiredArgsConstructor
@EnableWebSecurity // 1
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter { // 2

  private final UserService userService; // 3
/*
  @Autowired
  PasswordEncoder passwordEncoder;
*/
  @Bean
  public PasswordEncoder passwordEncoder() { // 4
    return new BCryptPasswordEncoder();
  }

  @Override
  public void configure(WebSecurity web) { // 5
    web.ignoring().antMatchers("/css/**", "/js/**", "/img/**");
  }


  @Bean
  public AuthenticationSuccessHandler successHandler() {
    return new UserAuthenticationSuccessHandler("/");
  }


  @Override
  protected void configure(HttpSecurity http) throws Exception { // 6
    http
          .authorizeRequests() // 7
            .antMatchers("/login", "/signup", "/user").permitAll() // 누구나 접근 허용
            .antMatchers("/").hasRole("USER") // USER, ADMIN만 접근 가능
            .antMatchers("/admin").hasRole("ADMIN") // ADMIN만 접근 가능
            .anyRequest().authenticated() // 나머지 요청들은 권한의 종류에 상관 없이 권한이 있어야 접근 가능
        .and()
          .formLogin() // 8
          	.successHandler(successHandler())
            .loginPage("/login") // 로그인 페이지 링크
            //.defaultSuccessUrl("/") // 로그인 성공 후 리다이렉트 주소
        .and()
          .logout() // 9
            .logoutSuccessUrl("/login") // 로그아웃 성공시 리다이렉트 주소
	    .invalidateHttpSession(true) // 세션 날리기
    ;
  }

  @Override
  public void configure(AuthenticationManagerBuilder auth) throws Exception { // 10
    auth.userDetailsService(userService).passwordEncoder(passwordEncoder()); // 해당 서비스(userService)에서는 UserDetailsService를 implements해서 loadUserByUsername() 구현해야함 (서비스 참고)
  }
}