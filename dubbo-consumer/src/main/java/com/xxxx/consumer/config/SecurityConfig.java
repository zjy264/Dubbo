package com.xxxx.consumer.config;


import com.xxxx.consumer.exception.JWTAccessDeniedHandler;
import com.xxxx.consumer.exception.JWTAuthenticationEntryPoint;
import com.xxxx.consumer.filter.JWTAuthenticationFilter;
import com.xxxx.consumer.filter.JWTAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    // 因为UserDetailsService的实现类实在太多啦，这里设置一下我们要注入的实现类//    @Qualifier("UserDetailsServiceImpl")
    @Qualifier("userDetailsServiceImpl")
    @Autowired(required = false)
    private UserDetailsService userDetailsService;

    // 加密密码的，安全第一嘛~
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }

    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .authorizeRequests()
                // 测试用资源，需要验证了的用户才能访问
                // 需要角色为ADMIN才能删除该资源
//                .antMatchers( "/tasks/**").hasRole("Tourist")
//                .antMatchers( "/controller/**").hasRole("ADMIN")
//                .antMatchers("/person/**","/shopping/**").authenticated()
//                .antMatchers("/collect/**","/personaddress/**").authenticated()
                // 其他都放行了
                .anyRequest().permitAll()
                .and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager()))
                .addFilter(new JWTAuthorizationFilter(authenticationManager()))
                // 不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // 加一句这个
                .exceptionHandling().authenticationEntryPoint(new JWTAuthenticationEntryPoint())
                .and()
                .exceptionHandling().accessDeniedHandler(new JWTAccessDeniedHandler());
    }


    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }
}

