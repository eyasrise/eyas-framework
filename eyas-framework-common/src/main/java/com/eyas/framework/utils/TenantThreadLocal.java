package com.eyas.framework.utils;


/**
 * @author feixiansheng
 * 单独的类 来存储 整个请求的 公共的线程数据
 */
public class TenantThreadLocal {

    //创建本地线程变量
    private static final ThreadLocal<Object> threadLocal = new InheritableThreadLocal<>();

    public static Object getSystemUser() {
        return threadLocal.get();
    }

    public static void setSystemUser(Object t) {
        threadLocal.set(t);
    }
}
