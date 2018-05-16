package org.ljx.controller;

import org.ljx.entity.web.ResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ljx on 2018/5/14.
 */
public class BaseController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

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
}
