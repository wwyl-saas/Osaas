package com.fate.common.config.converter;

import com.fate.common.config.JacksonConfig;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @program: parent
 * @description: LocalDateTime参数转换
 * @author: chenyixin
 * @create: 2019-07-21 18:36
 **/
public class LocalDateTimeConverter implements Converter<String, LocalDateTime>{

    @Override
    public LocalDateTime convert(String source) {
        return LocalDateTime.parse(source, DateTimeFormatter.ofPattern(JacksonConfig.DEFAULT_DATE_TIME_FORMAT));
    }
}
