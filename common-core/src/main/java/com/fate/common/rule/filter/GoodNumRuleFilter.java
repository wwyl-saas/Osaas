package com.fate.common.rule.filter;

import com.fate.common.enums.RuleRelationType;
import com.fate.common.rule.RuleContext;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @program: parent
 * @description:
 * @author: chenyixin
 * @create: 2019-09-24 21:17
 **/
@Slf4j
@AllArgsConstructor
public class GoodNumRuleFilter implements RuleFilter{
    private RuleRelationType relation;

    private String ruleValue;

    @Override
    public void match(RuleContext ruleContext) {
        boolean result=false;
        Object object=ruleContext.getValue("totalNum");
        if (object!=null && object instanceof Integer){
            Integer totalNum= (Integer) object;
            String[] array = ruleValue.split(",");
            Integer num=Integer.parseInt(array[0]);
            switch (relation) {
                case LESS:
                    result=totalNum< num;
                    break;
                case EQUAL:
                    result=totalNum== num;
                    break;
                case GREATER:
                    result=totalNum> num;
                    break;
                case LESS_EQUAL:
                    result=totalNum<= num;
                    break;
                case GREATER_EQUAL:
                    result=totalNum>= num;
                    break;
                case BETWEEN:
                    if (array.length > 1) {
                        result=totalNum>=num && totalNum<=Integer.parseInt(array[1]);
                    }
                    break;
            }
        }
        if (!result) {
            ruleContext.addReason("项目数量须" + relation.getDesc() + ruleValue+"个");
        }
        log.debug("商品数量匹配{}",result);
        ruleContext.addResult(result);
    }
}
