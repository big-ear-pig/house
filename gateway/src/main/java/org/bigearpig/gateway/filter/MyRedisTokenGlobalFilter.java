package org.bigearpig.gateway.filter;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Header;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.bigearpig.common.UserInfo;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import org.bigearpig.common.JwtUtil;
import org.bigearpig.common.BaseConstant;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@Order(-1)
public class MyRedisTokenGlobalFilter implements GlobalFilter {

    @Resource
    private ReactiveRedisTemplate<String, String> reactiveRedisTemplate;


    static AntPathMatcher matcher = new AntPathMatcher();
    static List<String> patterns = new ArrayList<>();

    static {
        patterns.add("/user/login");
        patterns.add("/ws/**");
        patterns.add("/ws-native");
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String requestPath = exchange.getRequest().getPath().pathWithinApplication().value();
        log.info("Request ID: {}, path: {}", exchange.getRequest().getId(), requestPath);

        // 白名单  直接放入匿名用户信息
        if (patterns.stream().anyMatch(pattern -> matcher.match(pattern, requestPath))) {

            UserInfo userInfo = UserInfo.getNoAuthUser();
            String jwt = JwtUtil.generateJwt(JSONObject.toJSONString(userInfo));

            ServerWebExchange mutatedExchange = exchange.mutate()
                    .request(r -> r.header(BaseConstant.BASE_TOKEN_KEY, jwt))
                    .build();
            return chain.filter(mutatedExchange);
        }else{
            // 校验 Authorization
            String token = exchange.getRequest().getHeaders().getFirst(Header.AUTHORIZATION.getValue());
            if (StrUtil.isBlank(token) || !token.startsWith("Bearer ")) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
            String redisKey = token.substring(7);
            log.info("Query Redis with key: {}", redisKey);
            return reactiveRedisTemplate.opsForValue().get(redisKey)
                    .defaultIfEmpty("")   // 将 Redis 中不存在的 key 转为空字符串
                    .flatMap(loginTokenJson -> {
                        if (StrUtil.isEmpty(loginTokenJson)) {
                            log.warn("Redis key not found or empty: {}", redisKey);
                            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                            return exchange.getResponse().setComplete();
                        }

                        return reactiveRedisTemplate.expire(redisKey, java.time.Duration.ofMinutes(60))
                                .onErrorResume(e -> {
                                    log.error("Failed to refresh token expiration for key: {}", redisKey, e);
                                    return Mono.just(false);
                                })
                                .then(Mono.defer(() -> {
                                    String jwt = JwtUtil.generateJwt(loginTokenJson);
                                    ServerWebExchange mutatedExchange = exchange.mutate()
                                            .request(r -> r.header(BaseConstant.BASE_TOKEN_KEY, jwt))
                                            .build();
                                    return chain.filter(mutatedExchange);
                                }));

                    });
        }






    }

}