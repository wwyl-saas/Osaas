package com.fate.common.rule.filter;

import com.fate.common.rule.RuleContext;

public interface RuleFilter {

    void match(RuleContext ruleContext);

}
