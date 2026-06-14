package org.bigearpig.sys.module.websocket.config;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Header;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.bigearpig.base.security.SecurityUserInfo;

import java.security.Principal;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Slf4j
public class MyChannelInterceptor implements ChannelInterceptor {

    private StringRedisTemplate stringRedisTemplate;

    public MyChannelInterceptor(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor != null) {
            log.info("SockJS  Command={}, Destination={}", accessor.getCommand(), accessor.getDestination());
            if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                // 从 STOMP CONNECT 帧的 native headers 中获取 token
                List<String> authHeaders = accessor.getNativeHeader(Header.AUTHORIZATION.getValue());
                String jwtToken = null;
                if (authHeaders != null && !authHeaders.isEmpty()) {
                    jwtToken = authHeaders.get(0).substring(7);

                }
                if (StrUtil.isNotBlank(jwtToken)) {
                    String info = stringRedisTemplate.opsForValue().get(jwtToken);
                    if(StrUtil.isBlank(info)){
                        throw new SecurityException("Invalid or missing JWT token");
                    }
                    stringRedisTemplate.opsForValue().set(jwtToken,info,60*60, TimeUnit.SECONDS);
                    SecurityUserInfo securityUserInfo = JSONObject.parseObject(info, SecurityUserInfo.class);
                    if (securityUserInfo != null) {
                        // 构建 Spring Security 用户信息
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(securityUserInfo, null, securityUserInfo.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        // 重要：将认证信息存入 accessor，后续可通过 @DestinationVariable 或 SimpUser 获取
                        accessor.setUser(authentication);
                    } else {
                        throw new SecurityException("Invalid or missing JWT token");
                    }

                } else {
                    throw new SecurityException("Invalid or missing JWT token");
                }
                log.info("连接成功");
            } else if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {

            } else if (StompCommand.UNSUBSCRIBE.equals(accessor.getCommand())) {

            } else if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {
                log.warn("DISCONNECT from session {}, headers: {}", accessor.getSessionId(), accessor.toNativeHeaderMap());
            } else {
                Principal principal = accessor.getUser();
                if (ObjectUtil.isNotNull(principal)) {
                    UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
                    log.info("当前登陆人" + token.getName());
                }
            }
        }
        return message;
    }

    @Override
    public boolean preReceive(MessageChannel channel) {
        return true;
    }
}
