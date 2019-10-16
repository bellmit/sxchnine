package com.project.configuration;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class RequestInterceptor implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;

        MDC.put("requestUri", request.getRequestURI());
        filterChain.doFilter(servletRequest, servletResponse);
        MDC.put("statusCode", String.valueOf(response.getStatus()));
        log.info("user controller::call {} with status {}", request.getRequestURI(), response.getStatus());
    }
}
