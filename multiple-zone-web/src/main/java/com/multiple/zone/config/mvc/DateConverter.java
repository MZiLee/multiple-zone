package com.multiple.zone.config.mvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
* @Description
* @Author zhangliewei
* @Date 2016/7/5
* @Copyright(c) gome inc Gome Co.,LTD
*/
public class DateConverter implements Converter<String, Date> {
    private static final Logger L = LoggerFactory.getLogger(DateConverter.class);

    public static final String YYYY_MM_DD="yyyy-MM-dd";
    public static final String YYYY_MM_DD_HH_MM_SS="yyyy-MM-dd HH:mm:ss";
    public static final String HH_MM ="hh:mm";

    @Override
    public Date convert(String s) {
        if (StringUtils.isEmpty(s))
            return null;

        SimpleDateFormat dateFormat;
        Date date = null;
        try{
            dateFormat = new SimpleDateFormat(YYYY_MM_DD);
            date = dateFormat.parse(s);
        }catch (ParseException e){
        }

        if(null == date) {
            try {
                dateFormat = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
                date = dateFormat.parse(s);
            } catch (ParseException e) {

            }
        }

        if(null == date) {
            try {
                dateFormat = new SimpleDateFormat(HH_MM);
                date = dateFormat.parse(s);
            } catch (ParseException e) {
            }
        }

        return date;
    }
}
