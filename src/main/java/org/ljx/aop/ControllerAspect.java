package org.ljx.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.ljx.common.MvcThreadLocal;
import org.ljx.util.JsonUtil;
import org.ljx.util.WebUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.UUID;

/**
 * Created by ljx on 2018/5/15.
 */
@Aspect
@Component
public class ControllerAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
//
//    /**
//     * 前置通知：目标方法执行之前执行以下方法体的内容
//     * @param jp
//     */
//    @Before("execution(* org.ljx.controller.*.*(..))")
//    public void beforeMethod(JoinPoint jp){
////        String uuid = UUID.randomUUID().toString();
////        String methodName = jp.getSignature().getName();
////        Object[] args = jp.getArgs();
////        Object[] canJsonArgs = new Object[args.length];
////        logger.info(uuid + "=>" + WebUtil.getClientIp() + "=>" + WebUtil.getJsonWebToken() + "=>" + WebUtil.getRequest().getRequestURI() + "[" + JSONObject.toJSONString(Arrays.asList(canJsonArgs)) + "]");
////        System.out.println("【前置通知】the method 【" + methodName + "】 begins with " + JSONObject.toJSONString(Arrays.asList(canJsonArgs)));
//    }
//
//    /**
//     * 返回通知：目标方法正常执行完毕时执行以下代码
//     * @param jp
//     * @param result
//     */
//    @AfterReturning(value="execution(* org.ljx.controller.*.*(..))",returning="result")
//    public void afterReturningMethod(JoinPoint jp, Object result){
////        String methodName = jp.getSignature().getName();
////        System.out.println("【返回通知】the method 【" + methodName + "】 ends with 【" + JSONObject.toJSONString(result) + "】");
//    }

    @Around("execution(* org.ljx.controller..*.*(..))")
    public Object aroundMethod(ProceedingJoinPoint pjd) throws Throwable {
        String uuid = UUID.randomUUID().toString();
        MvcThreadLocal.BIZ_CONTROLLER.set(uuid);

        long start = System.currentTimeMillis();

        MethodSignature methodSignature = (MethodSignature) pjd.getSignature();
        Object[] args = pjd.getArgs();
        Method method = methodSignature.getMethod();

        if(method.getName().equals("initBinder")){
            return pjd.proceed();
        }
        //请求参数
        String ip = WebUtil.getClientIp();
        Object[] canJsonArgs = new Object[args.length];
        int j = 0;
        for (Object arg : args) {
            if (arg instanceof MultipartFile) {
                j++;
                continue;
            }
            if (arg instanceof HttpServletResponse) {
                j++;
                continue;
            }
            canJsonArgs[j] = arg;
            j++;
        }

        logger.info(uuid + "=>" + ip + "=>" + WebUtil.getJsonWebToken() + "=>" + WebUtil.getRequest().getRequestURI() + "[" + JsonUtil.writeValuesAsStringExcludeNull(Arrays.asList(canJsonArgs)) + "]");

        if (null != method.getAnnotation(ResponseBody.class) || null != pjd.getTarget().getClass().getAnnotation(RestController.class)) {
            //执行目标方法
            Object result = pjd.proceed();
            logger.info(uuid + "=>response[" + (System.currentTimeMillis() - start) + "ms]");
            logger.info(uuid + "=>response[" + JsonUtil.writeValuesAsStringExcludeNull(result) + "]");
            //返回通知
            return result;
        } else {
            return pjd.proceed();
        }
    }
}
