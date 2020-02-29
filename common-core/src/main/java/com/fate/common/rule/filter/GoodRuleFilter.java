package com.fate.common.rule.filter;

import com.fate.common.enums.RuleRelationType;
import com.fate.common.rule.RuleContext;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @program: parent
 * @description:
 * @author: chenyixin
 * @create: 2019-09-24 21:18
 **/
@Slf4j
@AllArgsConstructor
public class GoodRuleFilter implements RuleFilter {
    private RuleRelationType relation;

    private String ruleValue;

    @Override
    public void match(RuleContext ruleContext) {
        boolean result=false;
        Object goodsIds = ruleContext.getValue("goodsIds");
        if (goodsIds != null) {
            String goods = (String) goodsIds;
            String[] goodsArray = goods.split(",");
            switch (relation) {
                case INCLUDE:
                    for (String gid:goodsArray){
                        if (ruleValue.indexOf(gid)!=-1){
                            result=true;
                            break;
                        }
                    }
                    break;
                case EXCLUDE:
                    for (String gid:goodsArray){
                        if (ruleValue.indexOf(gid)==-1){
                            result=true;
                            break;
                        }
                    }
                    break;
            }
        }
        if (!result) {
            ruleContext.addReason("订单商品须" + relation.getDesc() + "特定商品");
        }
        ruleContext.addResult(result);
    }
}
