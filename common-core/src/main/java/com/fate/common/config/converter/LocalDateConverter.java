package com.fate.common.config.converter;

import com.fate.common.config.JacksonConfig;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @program: parent
 * @description: LocalDate 参数转换
 * @author: chenyixin
 * @create: 2019-07-21 18:33
 **/
public class LocalDateConverter implements Converter<String, LocalDate> {
    @Override
    public LocalDate convert(String source) {
        return LocalDate.parse(source, DateTimeFormatter.ofPattern(JacksonConfig.DEFAULT_DATE_FORMAT));
    }
}
