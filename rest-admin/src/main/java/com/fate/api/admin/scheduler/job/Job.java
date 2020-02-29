package com.fate.api.admin.scheduler.job;


/**
 * 分布式条件下可运行的job
 */
public abstract class Job {
    private String key;

    public void setJobKey(){
        this.key=key;
    }

    abstract void work();

    public void execute(){
        //todo 分布式锁判断
        work();
    };
}
