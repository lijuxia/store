package org.ljx.controller.advice;

import org.ljx.entity.web.ResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ljx on 2018/5/16.
 */
@ControllerAdvice
public class MyControllerAdvice {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * 应用到所有@RequestMapping注解方法，在其执行之前初始化数据绑定器
     * @param binder
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(format,true));
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:S");
        binder.registerCustomEditor(Timestamp.class, new CustomDateEditor(dateFormat, true));
    }
//
//    /**
//     * 把值绑定到Model中，使全局@RequestMapping可以获取到该值
//     * @param model
//     */
//    @ModelAttribute
//    public void addAttributes(Model model) {
//        model.addAttribute("author", "Magical Sam");
//    }

    /**
     * 全局异常捕捉处理
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ResponseMessage errorHandler(Exception e) {
        logger.error("",e);
        ResponseMessage result = new ResponseMessage();
        result.setMsg(e.getMessage());
        if (e instanceof org.springframework.web.servlet.NoHandlerFoundException) {
            result.setCode(404);
        } else {
            result.setCode(500);
        }
        result.setSucceed(false);
        return result;
    }


}
