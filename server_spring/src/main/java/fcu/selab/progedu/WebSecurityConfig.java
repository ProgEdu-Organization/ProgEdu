package fcu.selab.progedu;

import fcu.selab.progedu.db.UserDbManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Bean
  PasswordEncoder passwordEncoder() { // Todo 一定要這一個 bean , 我也不知道為什麼
    return NoOpPasswordEncoder.getInstance();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(new MyUserDetailsService());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues());

    http.cors().and().csrf().disable().authorizeRequests()
            .antMatchers(HttpMethod.POST, "/user/new").hasRole("TEACHER")
            .antMatchers(HttpMethod.POST, "/user/upload").hasRole("TEACHER")
            .antMatchers(HttpMethod.POST, "/assigment/**").hasRole("TEACHER")
            .antMatchers(HttpMethod.POST, "/login").permitAll()
            .anyRequest().authenticated()
            .and()
            .addFilterBefore(new CorsFilterConfig(), ChannelProcessingFilter.class)
            .addFilterBefore(new JwtLoginFilter("/login",authenticationManager()), UsernamePasswordAuthenticationFilter.class)
            .addFilter(new JwtFilter(authenticationManager()))
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring().antMatchers("/publicApi/**", "/assignment/getAssignmentFile",
            "/assignment/getMvnAssignmentFile", "/assignment/getJavaAssignmentFile",
            "/assignment/getAndroidAssignmentFile", "/assignment/getWebAssignmentFile",
            "/assignment/getPythonAssignmentFile",
            "/swagger-ui.html", "/webjars/**", "/swagger-resources/**", "/v2/api-docs", "/user/getUserCsvFile",
            "/score/getScoreCsvFile");
//    web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

    CorsConfiguration corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
    source.registerCorsConfiguration("/**", corsConfiguration);

    return source;
  }



}
