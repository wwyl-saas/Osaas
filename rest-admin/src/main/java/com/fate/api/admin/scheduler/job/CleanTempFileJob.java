package com.fate.api.admin.scheduler.job;

import com.fate.common.cons.Const;
import com.fate.common.util.ClassUtil;
import com.fate.common.util.QiNiuUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

/**
 * @program: parent
 * @description: 定时清理临时文件目录
 * @author: chenyixin
 * @create: 2019-06-13 10:26
 **/
//@Component
@Slf4j
public class CleanTempFileJob {
    private static long Time= 5*60*1000;
    public void test() {
        String path= Const.TEMP_PATH;
        List<File> files= ClassUtil.getFiles(path);
        List<String> list= Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(files)){
            files.stream().filter(file->StringUtils.isNumeric(file.getName())).filter(file ->ifBigThenTimeMinutes(Long.parseLong(file.getName()))).forEach(file -> {
                list.add(file.getName());
                try {
                    if (file.exists())
                        file.delete();
                }catch (Exception e){
                    log.error("删除临时文件报错",e);
                }
            });
        }
        log.info("删除临时文件:{}",list.toString());
    }

    /**
     * 判断是否大于时间间隔
     * @param timestamp
     * @return
     */
    private boolean ifBigThenTimeMinutes(Long timestamp){
        Long t=System.currentTimeMillis()-timestamp;
        if (t>Time){
            return true;
        }
        return false;
    }


}
