package org.ljx.controller;

import org.ljx.entity.Store;
import org.ljx.entity.web.ResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

/**
 * Created by ljx on 2018/5/14.
 */
public class BaseController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    /**得到request对象
     * @return
     */
    public HttpServletRequest getRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request;
    }

    /**
     * 得到response对象
     * @return
     */
    public HttpServletResponse getResponse(){
        HttpServletResponse response = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getResponse();
        return response;
    }

    /**
     * 得到Session对象
     * @return
     */
    public HttpSession getSession(){
        return getRequest().getSession();
    }

    /**
     * 获取当前登陆的账号
     * @return
     */
    public Store getCurrentStore(){
        return (Store)getSession().getAttribute("user");
    }

    public ResponseMessage success(){
        ResponseMessage result = new ResponseMessage();
        result.setCode(200);
        result.setSucceed(true);
        result.setMsg("success");
        return result;
    }

    public ResponseMessage success(Object object){
        ResponseMessage result = new ResponseMessage();
        result.setCode(200);
        result.setSucceed(true);
        result.setMsg("success");
        result.setValue(object);
        return result;
    }

    public ResponseMessage fail(String errorMsg){
        ResponseMessage result = new ResponseMessage();
        result.setCode(500);
        result.setSucceed(false);
        result.setMsg(errorMsg);
        return result;
    }

    public void downFile(byte[] content, String fileName)throws Exception{
        HttpServletResponse response = getResponse();
        fileName = URLEncoder.encode(fileName, "UTF-8");
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        response.addHeader("Content-Length", "" + content.length);
        response.setContentType("application/octet-stream;charset=UTF-8");
        OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
        outputStream.write(content);
        outputStream.flush();
        outputStream.close();
    }
}
