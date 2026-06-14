package org.bigearpig.common;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;

import java.util.HashMap;
import java.util.Map;

public class JwtUtil {

    public static String generateJwt(String info) {
        // 1. 定义密钥，实际使用时应从配置文件中读取，保证安全
        byte[] key = BaseConstant.JWT_KEY.getBytes();

        // 2. 准备JWT的payload（载荷）数据
        Map<String, Object> payload = new HashMap<>();

        // 设置JWT标准声明
        DateTime now = DateTime.now();
        payload.put("iat", now);           // 签发时间 (Issued At)
        payload.put("exp", now.offsetNew(DateField.HOUR, 2)); // 过期时间 (Expiration Time), 示例为2小时后
        payload.put("nbf", now);           // 生效时间 (Not Before)

        // 设置用户自定义声明
        payload.put("userInfo", info);



        // 3. 生成JWT字符串
        return JWTUtil.createToken(payload, key);
    }

    // 解析并验证 Token，返回 UserInfo
    public static String parseJwt(String token) {
        // 1. 定义密钥，实际使用时应从配置文件中读取，保证安全
        byte[] key = BaseConstant.JWT_KEY.getBytes();
        // 1. 解析 JWT
        JWT jwt = JWTUtil.parseToken(token);

        // 2. 验证签名（必须使用相同的密钥）
        boolean verified = jwt.setKey(key).verify();
        if (!verified) {
            throw new RuntimeException("JWT 签名验证失败");
        }

        // 3. 检查过期时间（Hutool 会自动校验 exp 字段，但我们可以手动判断）
        if (jwt.getPayload("exp") != null) {
            // Hutool 的 JWT 在调用 verify() 时已经校验了 exp 和 nbf，这里可以不重复校验
            // 如果需要手动校验，可以获取 exp 时间戳并与当前时间比较
        }

        // 4. 获取自定义载荷中的 userInfo 字符串，并反序列化为 UserInfo
        String userInfoJson = (String) jwt.getPayload("userInfo");
        if (userInfoJson == null) {
            throw new RuntimeException("JWT 中未包含用户信息");
        }
        return userInfoJson;
    }
}
