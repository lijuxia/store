package org.ljx.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * web工具
 * Created by zhengke on 2015/1/26.
 */
public class WebUtil {
    private static final String HEAD_JWT_KEY = "x-access-token";
    private static final String HEAD_USER_AGENT = "User-Agent";
    private static final String HEAD_USER_AGENT_WECHAT = "MicroMessenger";

    private static final String[] HEADERS_LIST = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"
    };

    /**
     * 获取http request
     *
     * @return
     */
    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    /**
     * 获取项目绝对路径
     *
     * @return
     */
    public static String getProjectPath() {
        return getRequest().getSession().getServletContext().getRealPath("/");
    }

    /**
     * 获取头里面的json web token
     *
     * @return
     */
    public static String getJsonWebToken() {
        return getRequest().getHeader(HEAD_JWT_KEY);
    }

    /**
     * 获取浏览器类型
     *
     * @return
     */
    public static String getHeadUserAgent() {
        return getRequest().getHeader(HEAD_USER_AGENT);
    }

    /**
     * 是否为微信客户端访问
     *
     * @return
     */
    public static boolean isWechatClient() {
        if (getHeadUserAgent().contains(HEAD_USER_AGENT_WECHAT)) {
            return true;
        }
        return false;
    }

    public static String getClientIp() {
        for (String header : HEADERS_LIST) {
            String ip = getRequest().getHeader(header);
            if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
                if (ip.contains(",")) {
                    return ip.trim().split(",")[0];
                } else if (ip.contains("，")) {
                    return ip.trim().split("，")[0];
                }
                return ip;
            }
        }
        return getRequest().getRemoteAddr();
    }
}