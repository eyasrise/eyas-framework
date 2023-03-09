//package com.eyas.framework.aspect;
//
//import com.eyas.framework.data.EyasRpcFeignEntity;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
///**
// * @author Created by yixuan on 2019/10/14.
// */
//@Aspect
//@Component
//public class ServiceAspect {
//
//    private final Logger log = LoggerFactory.getLogger(getClass());
//
//
//    @Pointcut("@annotation(com.eyas.framework.annotation.Service)")
//    public void annotationPointCut(){
//    }
//
//    @Around("annotationPointCut()")
//    public Object advice(ProceedingJoinPoint joinPoint){
//        System.out.println("通知之开始");
//        log.info("===" + joinPoint);
//        String methodName = joinPoint.getSignature().getName();
//        Object rstMsg=null;
//        // 前置
//        try {
//            Object[] var1 = joinPoint.getArgs();
//            EyasRpcFeignEntity eyasRpcFeignEntity = (EyasRpcFeignEntity) var1[0];
//            eyasRpcFeignEntity.setMethodName(methodName);
//            long start = System.currentTimeMillis();
//            rstMsg = joinPoint.proceed(var1);
//            long end = System.currentTimeMillis();
//            log.info("ServiceAspect:——"+rstMsg);
//            log.info("ServiceAspect:——" + "runTime--" + joinPoint + "\tUse time : " + (end - start) + " ms!");
//        } catch (Throwable e) {
//            e.printStackTrace();
//        }
//        log.info("通知之结束");
//        return rstMsg;
//    }
//
//}
