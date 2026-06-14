package org.bigearpig.sys.module.websocket.controller;

import lombok.extern.slf4j.Slf4j;
import org.bigearpig.base.security.SecurityUserInfo;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.bigearpig.sys.module.websocket.controller.mo.PrivateMo;
import org.bigearpig.sys.module.websocket.controller.mo.PublicMo;


import javax.annotation.Resource;

@Slf4j
@Controller
public class WebsocketController {

    @Resource
    private SimpMessagingTemplate messagingTemplate;

    // 客户端发送地址：/app/public
    @MessageMapping("/public")
    public void appbroadcast(PublicMo publicMo) {
        // 可在此处理业务逻辑（如存储消息）将返回值广播给订阅 /topic/public 的所有客户端
        messagingTemplate.convertAndSend("/topic"+publicMo.getDestination(),publicMo.getMessage());
    }


    // 客户端发送地址：/app/private
    @MessageMapping("/private")
    // 将返回值广播给订阅 /topic/public 的所有客户端
    public void appprivate(PrivateMo privateMo, SimpMessageHeaderAccessor accessor) {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken)accessor.getUser();
        SecurityUserInfo securityUserInfo = (SecurityUserInfo)token.getPrincipal();
        log.info("SecurityUserInfo: "+securityUserInfo.toString());
        // 判断 当前 user 能发送消息 给 private中的 userId
        // 可在此处理业务逻辑（如存储消息）
        messagingTemplate.convertAndSendToUser(privateMo.getUserId().toString(),"/queue/private",privateMo.getMessage());

    }
}
