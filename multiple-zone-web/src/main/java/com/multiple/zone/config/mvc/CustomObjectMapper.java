package com.multiple.zone.config.mvc;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
* 自定义JSON转换器
* @author lichao
*/
public class CustomObjectMapper extends ObjectMapper {
    public CustomObjectMapper() {
        super();
        // 转换前台日期格式
        getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
                jgen.writeString("");
            }
        });
        SimpleDateFormat formatter = new SimpleDateFormat(DateConverter.YYYY_MM_DD_HH_MM_SS);
        this.setDateFormat(formatter);

    }
}