package cn.lq.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author : <a href="mailto:liuqi@ebnew.com">liuqi</a>
 * @version : v1.0
 * @date :  2020-11-04 11:22
 * @description :
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    PasswordEncoder passwordEncoder(){
        //返回不需要加密密码的实例
        return NoOpPasswordEncoder.getInstance();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //覆盖配置文件的账号密码 withUser:用户名   password:密码   roles:身份
        //如果需要配置多个用户  用and连接
        auth.inMemoryAuthentication().withUser("liuqi").password("qwe123").roles("admin");
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        //web.ignoring() 用来配置忽略掉的 URL 地址，一般对于静态文件，我们可以采用此操作。
        web.ignoring().antMatchers("/js/**", "/css/**","/images/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /**
         * 如果我们使用 XML 来配置 Spring Security ，里边会有一个重要的标签 <http>，HttpSecurity 提供的配置方法 都对应了该标签。
         * authorizeRequests 对应了 <intercept-url>。
         * formLogin 对应了 <formlogin>。
         * and 方法表示结束当前标签，上下文回到HttpSecurity，开启新一轮的配置。
         * permitAll 表示登录相关的页面/接口不要被拦截。
         * 最后记得关闭 csrf
         *
         * 注:
         * 1.如果不配置loginProcessingUrl("/doLogin")则SpringSecurity默认表单提交接口也是"/login.html"
         * 追踪FormLoginConfigurer的父类AbstractAuthenticationFilterConfigurer的构造方法和init方法
         * 2.usernameParameter和passwordParameter的默认值是username和password
         * 追踪UsernamePasswordAuthenticationFilter
         * 3.关于登录成功重定向URL的方法有两个:defaultSuccessUrl 和 successForwardUrl
         *      (1)defaultSuccessUrl 有一个重载的方法，我们先说一个参数的 defaultSuccessUrl 方法。如果我们在 defaultSuccessUrl
         *      中指定登录成功的跳转页面为 /index，此时分两种情况，如果你是直接在浏览器中输入的登录地址，登录成功后，就直接跳转到 /index，
         *      如果你是在浏览器中输入了其他地址，例如 http://localhost:8080/hello，结果因为没有登录，又重定向到登录页面，此时登录成功后，
         *      就不会来到 /index ，而是来到 /hello 页面。
         *      (2)defaultSuccessUrl 还有一个重载的方法，第二个参数如果不设置默认为 false，也就是我们上面的的情况，
         *      如果手动设置第二个参数为 true，则 defaultSuccessUrl 的效果和 successForwardUrl 一致。
         *      (3)successForwardUrl 表示不管你是从哪里来的，登录后一律跳转到 successForwardUrl 指定的地址。例如 successForwardUrl 指定的地址为 /index ，
         *      你在浏览器地址栏输入 http://localhost:8080/hello，结果因为没有登录，重定向到登录页面，当你登录成功之后，就会服务端跳转到 /index 页面；
         *      或者你直接就在浏览器输入了登录页面地址，登录成功后也是来到 /index。
         */
        //前后端不分离
        http.authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/login.html")
                .loginProcessingUrl("/doLogin")
                .usernameParameter("name")
                .passwordParameter("passwd")
                .permitAll()
                .and()
                .csrf()
                .disable();
        //前后端分离
    }
}
