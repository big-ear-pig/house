package org.bigearpig.base.security;

import cn.hutool.core.util.StrUtil;
import cn.hutool.jwt.JWTException;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.bigearpig.common.BaseConstant;
import org.bigearpig.common.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class TokenFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 1 获取路径
        String requestPath = request.getRequestURI();
        log.info("-----------"+requestPath);
        // 2. 获取 token，缺失则返回 401
        String token = request.getHeader(BaseConstant.BASE_TOKEN_KEY);
        if (StrUtil.isBlank(token)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("Missing token");
            return;
        }
        // 3. 解析 JWT，异常则返回 401
        SecurityUserInfo userInfo;
        try {
            String info = JwtUtil.parseJwt(token);
            userInfo = JSONObject.parseObject(info,SecurityUserInfo.class);
        } catch (JWTException | ClassCastException e) {
            log.warn("Invalid JWT token: {}", e.getMessage());
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("Invalid token");
            return;
        }
        SecurityUtil.setUserDetails(userInfo);

        // 放行请求
        filterChain.doFilter(request, response);

    }
}
