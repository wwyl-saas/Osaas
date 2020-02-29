package com.fate.common.rule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: parent
 * @description: 规则上下文
 * @author: chenyixin
 * @create: 2019-09-24 20:06
 **/
public class RuleContext {
    private Map<String, Object> facts = new HashMap<>();
    private List<String> reasons = new ArrayList<>();
    private boolean result=true;

    public void addFact(String key, Object value) {
        this.facts.put(key, value);
    }

    public void addReason(String reason) {
        this.reasons.add(reason);
    }

    public void addResult(boolean result){
        this.result=this.result && result;
    }

    public void clear() {
        this.result=true;
        this.reasons=new ArrayList<>();
    }

    public Object getValue(String key){
        return this.facts.get(key);
    }

    public List<String> getReasons(){
        return this.reasons;
    }

    public boolean getResult(){
        return this.result;
    }
}
