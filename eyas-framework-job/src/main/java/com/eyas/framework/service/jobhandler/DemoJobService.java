package com.eyas.framework.service.jobhandler;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @author Created by yixuan on 2019/6/26.
 */
@JobHandler(value = "demoJobService")
@Component
public class DemoJobService extends IJobHandler {
    @Override
    public ReturnT<String> execute(String s) throws Exception {
        XxlJobLogger.log("hello xxlJob");
        return ReturnT.SUCCESS;
    }
}
