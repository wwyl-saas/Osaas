package com.fate.common.util;

import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalQuery;

/**
 * @program: parent
 * @description:
 * @author: chenyixin
 * @create: 2019-08-26 15:35
 **/
public class QuarterOfYearQuery implements TemporalQuery<Integer> {
    @Override
    public Integer queryFrom(TemporalAccessor temporal) {
        LocalDate now = LocalDate.from(temporal);

        if (now.isBefore(now.with(Month.APRIL).withDayOfMonth(1))) {
            return 1;
        } else if (now.isBefore(now.with(Month.JULY).withDayOfMonth(1))) {
            return 2;
        } else if (now.isBefore(now.with(Month.NOVEMBER).withDayOfMonth(1))) {
            return 3;
        } else {
            return 4;
        }
    }
}