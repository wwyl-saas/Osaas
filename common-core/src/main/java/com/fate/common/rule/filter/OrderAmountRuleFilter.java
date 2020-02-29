package com.fate.common.rule.filter;

import com.fate.common.enums.RuleRelationType;
import com.fate.common.rule.RuleContext;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 * @program: parent
 * @description: 订单金额
 * @author: chenyixin
 * @create: 2019-09-24 20:13
 **/
@Slf4j
@AllArgsConstructor
public class OrderAmountRuleFilter implements RuleFilter {

    private RuleRelationType relation;

    private String ruleValue;

    @Override
    public void match(RuleContext ruleContext) {
        boolean result = false;
        Object value = ruleContext.getValue("orderAmount");
        if (value != null) {
            BigDecimal orderAmount = (BigDecimal) value;
            String[] array = ruleValue.split(",");
            switch (relation) {
                case LESS:
                    result = orderAmount.compareTo(BigDecimal.valueOf(Double.parseDouble(array[0]))) < 0;
                    break;
                case EQUAL:
                    result = orderAmount.compareTo(BigDecimal.valueOf(Double.parseDouble(array[0]))) == 0;
                    break;
                case GREATER:
                    result = orderAmount.compareTo(BigDecimal.valueOf(Double.parseDouble(array[0]))) > 0;
                    break;
                case LESS_EQUAL:
                    result = orderAmount.compareTo(BigDecimal.valueOf(Double.parseDouble(array[0]))) <= 0;
                    break;
                case GREATER_EQUAL:
                    result = orderAmount.compareTo(BigDecimal.valueOf(Double.parseDouble(array[0]))) >= 0;
                    break;
                case BETWEEN:
                    if (array.length > 1) {
                        result = orderAmount.compareTo(BigDecimal.valueOf(Double.parseDouble(array[0]))) >= 0 && orderAmount.compareTo(BigDecimal.valueOf(Double.parseDouble(array[1]))) <= 0;
                    }
                    break;
            }
        }
        if (!result) {
            ruleContext.addReason("订单金额须" + relation.getDesc() + ruleValue+"元");
        }
        log.info("订单金额匹配结果{}", result);
        ruleContext.addResult(result);
    }
}
