package com.example.tovyreactive5.app1;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Gun Lee (gunlee01@gmail.com) on 2017. 2. 8.
 */
@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean getFilterRegistrationBean()
    {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(new HSTSFilter());
        // registrationBean.addUrlPatterns("/*"); // 서블릿 등록 빈 처럼 패턴을 지정해 줄 수 있다.
        return registrationBean;
    }
}
