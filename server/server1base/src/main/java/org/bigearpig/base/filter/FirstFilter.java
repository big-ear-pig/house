package org.bigearpig.base.filter;

import lombok.extern.slf4j.Slf4j;
import org.bigearpig.base.util.IpUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class FirstFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 1. 获取请求路径（不包含主机和端口，但包含上下文路径）
        String uri = request.getRequestURI();          // 例如：/api/user/login
        String url = request.getRequestURL().toString(); // 例如：http://127.0.0.1/api/user/login
        String queryString = request.getQueryString();    // 例如：id=123&name=test，没有则为 null
        String ip = IpUtil.getIpAddress(request);
        // 2. 打印日志（可以使用不同级别）
        log.info("Request IP: {}", ip);
        log.info("Request URL: {}", url);
        if (queryString != null) {
            log.info("Query String: {}", queryString);
        }

        // 3. 继续执行过滤链
        filterChain.doFilter(request, response);
    }
}
