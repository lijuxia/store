package org.ljx.interceptor;

import org.ljx.entity.web.ResponseMessage;
import org.ljx.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by ljx on 2018/5/31.
 */
public class MyInterceptor  implements HandlerInterceptor {
    Logger logger = LoggerFactory.getLogger(MyInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // TODO Auto-generated method stub
        //获取session
        HttpSession session = request.getSession(true);
        //判断用户ID是否存在，不存在就跳转到登录界面
        System.out.println(request.getServletPath());
        if(session.getAttribute("user") == null){
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Access-Control-Allow-Origin","*");
            response.setHeader("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");
            response.setHeader("Access-Control-Allow-Method", "POST, GET, OPTIONS, DELETE, PUT");
            ResponseMessage message = new ResponseMessage();
            message.setCode(9000);
            message.setMsg("Logon state failure,Please log in !");
            message.setSucceed(false);
            response.getWriter().write(JsonUtil.writeValuesAsString(message));
            return false;
        }else{
            session.setAttribute("user", session.getAttribute("user"));
            return true;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        // TODO Auto-generated method stub

    }

}
