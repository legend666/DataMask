package cn.com.taiji.fsb.security;

import cn.com.taiji.fsb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.*;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.ArrayList;
import java.util.List;

/**
 * @description :  安全配置类
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    @Autowired
    private UserService userService;

    @Autowired
    private MyLogoutHandler myLogoutHandler;

    @Bean
    public UserDetailsService userDetailsService() {
        return userService;
    }

    @Bean
    public UsernamePasswordAuthenticationFilter authenticationFilter(){
        UsernamePasswordAuthenticationFilter filter = new UsernamePasswordAuthenticationFilter();
        filter.setFilterProcessesUrl("/loginCheck");
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler(authenticationSuccessHandler());
        filter.setAuthenticationFailureHandler(authenticationFailureHandler());
        return filter;
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler(){
        AuthenticationSuccessHandler handler = new SimpleUrlAuthenticationSuccessHandler("/loginSuccess");
        return handler;
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler(){
        AuthenticationFailureHandler handler = new SimpleUrlAuthenticationFailureHandler("/login?error");
        return handler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests().antMatchers("/403").permitAll()
                .and().authorizeRequests().antMatchers("/401").permitAll()
                .and().authorizeRequests().antMatchers("/404").permitAll()
                .and().authorizeRequests().antMatchers("/500").permitAll()
                .and().authorizeRequests().antMatchers("/error").permitAll()
                .and().authorizeRequests().antMatchers("/login").permitAll()
                .and().authorizeRequests().antMatchers("/logout").permitAll()
                .and().authorizeRequests().antMatchers("/loginCheck").permitAll()
                .and() .authorizeRequests().anyRequest().authenticated();

        http.headers().frameOptions().disable(); //关闭iframe的验证
        http.sessionManagement().sessionFixation().changeSessionId(); //处理sessionFixation攻击的解决方案
        http.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).addLogoutHandler(myLogoutHandler); //配置logout请求的handler
        //http.authorizeRequests().anyRequest().authenticated();  //所有请求都要进行验证
        http.formLogin().loginPage("/login");
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler());
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        AccessDeniedHandlerImpl accessDeniedHandler = new AccessDeniedHandlerImpl();
        accessDeniedHandler.setErrorPage("/403");
        return accessDeniedHandler;

    }

    @Bean
    public Md5PasswordEncoder passwordEncoder(){
        return new Md5PasswordEncoder();
    }

    /**
     * 配置登录认证方式为userDetailService
     * @return
     */
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder()); //设置密码编码
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        List<AuthenticationProvider> authenticationProviders = new ArrayList<>();
        authenticationProviders.add(authenticationProvider());
        ProviderManager authenticationManager = new ProviderManager(authenticationProviders);
        return authenticationManager;
    }

}
