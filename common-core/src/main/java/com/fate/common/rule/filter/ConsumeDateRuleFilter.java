package com.fate.common.rule.filter;

import com.fate.common.enums.RuleRelationType;
import com.fate.common.rule.RuleContext;
import com.fate.common.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

/**
 * @program: parent
 * @description: 消费日期
 * @author: chenyixin
 * @create: 2019-09-24 20:41
 **/
@Slf4j
@AllArgsConstructor
public class ConsumeDateRuleFilter implements RuleFilter{

    private RuleRelationType relation;

    private String ruleValue;

    @Override
    public void match(RuleContext ruleContext) {
        boolean result=false;
        LocalDate now=LocalDate.now();
        String[] array = ruleValue.split(",");
        LocalDate day= DateUtil.getLocalDate(array[0]);
        switch (relation) {
            case LESS:
                result= now.isBefore(day);
                break;
            case EQUAL:
                result= now.isEqual(day);
                break;
            case GREATER:
                result= now.isAfter(day);
                break;
            case LESS_EQUAL:
                result= now.isBefore(day) || now.isEqual(day);
                break;
            case GREATER_EQUAL:
                result= now.isAfter(day) || now.isEqual(day);
                break;
            case BETWEEN:
                if (array.length > 1) {
                    result= !now.isAfter(DateUtil.getLocalDate(array[1])) && !now.isBefore(day);
                }
                break;
        }
        if (!result) {
            ruleContext.addReason("消费日期须" + relation.getDesc() + ruleValue);
        }
        log.info("消费日期匹配{}",result);
        ruleContext.addResult(result);
    }
}
