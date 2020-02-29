package com.fate.common.config.converter;

import com.fate.common.config.JacksonConfig;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @program: parent
 * @description: LocalTime 参数转换
 * @author: chenyixin
 * @create: 2019-07-21 18:37
 **/
public class LocalTimeConverter implements Converter<String, LocalTime> {
    @Override
    public LocalTime convert(String source) {
        return LocalTime.parse(source, DateTimeFormatter.ofPattern(JacksonConfig.DEFAULT_TIME_FORMAT));
    }
}
