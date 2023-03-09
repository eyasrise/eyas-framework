package com.eyas.framework.converter;

import com.eyas.framework.DateUtil;
import com.eyas.framework.EmptyUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

import java.util.Date;


@Configuration
public class DateConverter implements Converter<String, Date> {


    @Override
    public Date convert(String s) {
        if (EmptyUtil.isNotEmpty(s)){
            // 校验时间格式
            if (DateUtil.dateParse(s, DateUtil.DATE_PATTERN.YYYY_MM_DD_HH_MM_SS)){
                // 时间校验成功
                return DateUtil.stringToDate2(s, DateUtil.DATE_PATTERN.YYYY_MM_DD_HH_MM_SS_SSS);
            }else if (DateUtil.dateParse(s, DateUtil.DATE_PATTERN.YYYY_MM_DD)){
                return DateUtil.stringToDate2(s, DateUtil.DATE_PATTERN.YYYY_MM_DD);
            }
        }
        return DateUtil.stringToDate2(s, DateUtil.DATE_PATTERN.YYYY_MM_DD_HH_MM_SS_SSS);
    }
}
