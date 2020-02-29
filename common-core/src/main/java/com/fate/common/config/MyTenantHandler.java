package com.fate.common.config;

import com.baomidou.mybatisplus.extension.plugins.tenant.TenantHandler;
import com.fate.common.util.CurrentMerchantUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.List;

/**
 * @program: parent
 * @description: 多租户相关
 * @author: chenyixin
 * @create: 2019-06-02 18:24
 **/
@Slf4j
public class MyTenantHandler implements TenantHandler {
    private static final String SYSTEM_TENANT_ID = "merchant_id";

    static final List<String> IGNORE_TENANT_TABLES= Arrays.asList("t_company_saler","t_merchant");//查询不需要添加租户字段的表

    @Override
    public Expression getTenantId() {
        Long merchantId = CurrentMerchantUtil.getMerchant().getId();
        Assert.notNull(merchantId,"默认区分租户字段为空");
        return new LongValue(merchantId);
    }

    @Override
    public String getTenantIdColumn() {
        return SYSTEM_TENANT_ID;
    }

    /**
     * 代表某些表可以不需要租户过滤
     * @param tableName
     * @return
     */
    @Override
    public boolean doTableFilter(String tableName) {
        return IGNORE_TENANT_TABLES.stream().anyMatch((e) -> e.equalsIgnoreCase(tableName));
    }
}
