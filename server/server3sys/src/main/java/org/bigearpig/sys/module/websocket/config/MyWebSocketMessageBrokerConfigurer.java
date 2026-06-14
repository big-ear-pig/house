package org.bigearpig.sys.module.websocket.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import javax.annotation.Resource;


@Slf4j
@Configuration
@EnableWebSocketMessageBroker
public class MyWebSocketMessageBrokerConfigurer implements WebSocketMessageBrokerConfigurer {

    @Resource
    private RabbitProperties rabbitProperties;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
         registry.addEndpoint("/ws")
                 .addInterceptors(new MyHandshakeInterceptor())
                 .setAllowedOriginPatterns("*")
                 .withSockJS();
        // 👇 为 Android 原生客户端新增一个不使用 SockJS 的 endpoint
        registry.addEndpoint("/ws-native")
                .addInterceptors(new MyHandshakeInterceptor())
                .setAllowedOriginPatterns("*");
    }


    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new MyChannelInterceptor(stringRedisTemplate));
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 前端绑定个人通道
        registry.setUserDestinationPrefix("/user");
        // 前端使用ws发送消息的时候
        registry.setApplicationDestinationPrefixes("/app");
        // 前端绑定stomp广播路径
        registry.enableStompBrokerRelay("/topic","/queue")
                .setVirtualHost(rabbitProperties.getVirtualHost())
                .setRelayHost(rabbitProperties.getHost())
                .setClientLogin(rabbitProperties.getUsername())
                .setClientPasscode(rabbitProperties.getPassword())
                .setSystemLogin(rabbitProperties.getUsername())
                .setSystemPasscode(rabbitProperties.getPassword())
                .setSystemHeartbeatSendInterval(60000)
                .setSystemHeartbeatReceiveInterval(60000);

    }
}
