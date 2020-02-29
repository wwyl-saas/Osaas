package com.fate.common.util;

import org.apache.commons.collections4.MapUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;

/**
 * @program: parent
 * @description: 模板渲染工具
 * @author: chenyixin
 * @create: 2019-06-16 10:55
 **/
public class TemplateUtil {
    private static VelocityEngine ve = new VelocityEngine();

    static {
        Properties p = new Properties();
        p.setProperty("input.encoding", "UTF-8");
        p.setProperty("output.encoding", "UTF-8");
        ve.init(p);
    }

    /**
     * 渲染模板
     *
     * @param template
     * @param param
     * @return
     */
    public static String render(String template, Map<String, Object> param) {
        if (MapUtils.isNotEmpty(param)) {
            VelocityContext context = new VelocityContext();
            for (Map.Entry<String, Object> entry : param.entrySet()) {
                context.put(entry.getKey(), entry.getValue());
            }
            StringWriter writer = new StringWriter();
            ve.evaluate(context, writer, "veUtil", template);
            return writer.toString();
        }
        return "";
    }
}
