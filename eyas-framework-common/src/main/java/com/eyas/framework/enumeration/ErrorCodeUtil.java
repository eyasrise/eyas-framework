package com.eyas.framework.enumeration;


/**
 * @author Created by yixuan on 2018/12/27.
 */
public class ErrorCodeUtil {

    public static String getErrorCode(String app, String type, String source, String errorCode) {
        return app + "#" + type + "#" + source + "#" + errorCode;
    }

    public static String getErrorCode(String type, String source, String errorCode) {
        String projectName = System.getProperty("project.name");
        if (null == projectName || "".equals(projectName)) {
            projectName = "UNKOWN";
        }
        return getErrorCode(projectName, type, source, errorCode);
    }
}
