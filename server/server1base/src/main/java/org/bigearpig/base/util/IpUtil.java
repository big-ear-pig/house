package org.bigearpig.base.util;

import javax.servlet.http.HttpServletRequest;

public class IpUtil {

    public static String getIpAddress(HttpServletRequest request) {
        // 尝试获取“x-forwarded-for”头，这是最常用的代理头字段。
        String ip = request.getHeader("X-Forwarded-For");
        // 检查“x-forwarded-for”头是否有效，并提取第一个IP地址。
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if (ip.indexOf(",") != -1) {
                ip = ip.split(",")[0];
            }
        }
        // 如果“x-forwarded-for”头无效，尝试其他不那么常见的代理头字段。
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        // 如果所有代理头字段都无效，回退到使用请求的远程地址作为客户端IP。
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 返回获取到的IP地址，无论它是通过代理头还是直接从请求中获取。
        return ip;

    }


}
