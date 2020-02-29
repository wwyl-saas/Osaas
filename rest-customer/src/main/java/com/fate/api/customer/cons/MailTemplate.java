package com.fate.api.customer.cons;

/**
 * @program: parent
 * @description: 模板
 * @author: chenyixin
 * @create: 2019-06-25 00:37
 **/
public class MailTemplate {

    public static final String FEEDBACK_TEMPLATE="<div>小程序应用问题反馈</div><br/><hr/>" +
            "<div>$applicationName</div><br/><hr/>" +
            "<div>$customerName</div><br/><hr/>" +
            "<div>$remark</div><br/><hr/>"+
            "#foreach($element in $pics) </div>图片：<img src='cid:$element'></img></div> #end";

}
