package com.fate.common.rule;

import com.fate.common.entity.CouponEffectRule;
import com.fate.common.rule.filter.*;

/**
 * @program: parent
 * @description:
 * @author: chenyixin
 * @create: 2019-09-24 21:48
 **/
public class RuleFilterFactory {

    public static RuleFilter createFilter(CouponEffectRule effectRule){
        switch (effectRule.getRuleType()){
            case ORDER_AMOUNT:
                return new OrderAmountRuleFilter(effectRule.getRelation(),effectRule.getRuleValue());
            case CONSUME_DATE:
                return new ConsumeDateRuleFilter(effectRule.getRelation(),effectRule.getRuleValue());
            case ORDER_GOODS_NUM:
                return new GoodNumRuleFilter(effectRule.getRelation(),effectRule.getRuleValue());
            case GOODS_AGGREGATION:
                return new GoodRuleFilter(effectRule.getRelation(),effectRule.getRuleValue());
        }
        return null;
    }
}
