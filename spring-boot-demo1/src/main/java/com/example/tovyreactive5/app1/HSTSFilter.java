package com.example.tovyreactive5.app1;

import javax.servlet.*;
import javax.servlet.FilterConfig;
import java.io.IOException;

/**
 * @author Gun Lee (gunlee01@gmail.com) on 2017. 2. 8.
 */
public class HSTSFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("[ServletFilter] initialized !!!");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("[ServletFilter] HSTSFilter null Filter Test !!!");
    }

    @Override
    public void destroy() {

    }
}
