package com.fate.api.merchant.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fate.api.merchant.dto.*;
import com.fate.api.merchant.query.MerchantUserQuery;
import com.fate.api.merchant.query.UserUpdateQuery;
import com.fate.api.merchant.util.CurrentUserUtil;
import com.fate.common.dao.*;
import com.fate.common.entity.*;
import com.fate.common.enums.ResponseInfo;
import com.fate.common.enums.UserRoleType;
import com.fate.common.util.BeanUtil;
import com.fate.common.util.CurrentMerchantUtil;
import com.fate.common.util.PingYinUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @program: parent
 * @description: 商户用户角色
 * @author: lpx
 **/
@Service
@Slf4j
public class MerchantUserRoleService {
    private static final String CAMERA = "camera.jpg";
    @Value("${cdn.domain}")
    private String cdnDomain;

    @Resource
    MerchantUserDao merchantUserDao;
    @Resource
    private MerchantUserRoleDao merchantUserRoleDao;
    @Resource
    private MerchantPostTitleDao merchantPostTitleDao;
    @Autowired
    MerchantService merchantService;

    public  PageDto<MerchantUserDto> getByMerchantByRoleType(Integer pageIndex, Integer pageSize, Integer roleType) {
        IPage<MerchantUser> page= merchantUserDao.queryMerchantUsersByRoleType(pageIndex,pageSize,roleType);
        List<MerchantUser> merchantUsers=null;
        if (page!=null && CollectionUtils.isNotEmpty(page.getRecords())) {
             merchantUsers = page.getRecords();
        }
        List<Long> postTitleIdsList=new ArrayList<Long>();
        for(MerchantUser merchantUser:merchantUsers){
            postTitleIdsList.add(Long.valueOf(String.valueOf(merchantUser.getPostTitleId())));
        }
        List<Map<String,Object>> titleId2NameMap= merchantPostTitleDao.getPostTitlesByIds(postTitleIdsList);
        List<MerchantUserDto> result= Lists.emptyList();
        result = merchantUsers.stream().map(merchantUser -> {
            MerchantUserDto  merchantUserDto=BeanUtil.mapper(merchantUser,MerchantUserDto.class);
            for(Map<String,Object> map:titleId2NameMap){
                String postTitleId= merchantUser.getPostTitleId().toString();
                if(map.get("id").toString().equals(postTitleId)){
                    merchantUserDto.setPostTitle(map.get("name").toString());
                }
            }
            if(null==merchantUserDto.getPostTitle() || merchantUserDto.getPostTitle().equals("")){
                merchantUserDto.setPostTitle("");
            }
            return merchantUserDto;
        }).collect(Collectors.toList());
        return new PageDto<>(pageIndex, pageSize, page.getTotal(), page.getPages(), result);

    }


    /**设置员工角色
     *
     * @param merchantUserIds
     * @param roleType
     * @param shopId
     */
    public void setMerchantUserInShopByRole(String merchantUserIds, Integer roleType,Long shopId) {
        //merchantUserIds 格式校验
        String[] userIds= merchantUserIds.split(",");
        for(String userId:userIds){
            Long oneUserId=Long.valueOf(userId);
            MerchantUserRole merchantUserRole=new MerchantUserRole();
            merchantUserRole.setRoleType(UserRoleType.getEnum(roleType).get());
            merchantUserRole.setShopId(shopId);
            merchantUserRole.setUserId(oneUserId);
            Assert.isTrue(merchantUserRole.insert(),"创建失败！");
        }
    }

    /**
     * 修改员工角色
     * @param userId
     * @param roleType
     * @param shopId
     */
    public void updateMerchantUserRole(Long userId, Integer roleType,Long shopId) {
        MerchantUserRole merchantUserRole= merchantUserRoleDao.getShopRoleByShopAndUserId(shopId,userId);
        Assert.notNull(merchantUserRole,"此员工不存在!");
        merchantUserRole.setRoleType(UserRoleType.getEnum(roleType).get());
        Assert.isTrue(merchantUserRole.updateById(),"编辑失败！");
    }

    /**
     *查找某角色的候选人
     * @param roleType
     * @param shopId
     * @return
     */
    public List<MerchantUserDto> queryCandidatesByRole(Integer roleType, Long shopId) {
        Assert.notNull(roleType,"角色不能为空！");
        Assert.notNull(shopId,"店铺id不能为空！");
         List<Long> userIds=merchantUserRoleDao.queryCandidatesByRole(roleType,shopId);
         List<MerchantUserDto> result= Lists.emptyList();
         if(userIds.size()>0){
             List<MerchantUser> merchantUserList= merchantUserDao.queryUsersByIds(userIds);
             result = merchantUserList.stream().map(merchantUser -> {
                 MerchantUserDto merchantUserDto = new MerchantUserDto();
                 merchantUserDto.setName(merchantUser.getName());
                 merchantUserDto.setId(merchantUser.getId());

                 return merchantUserDto;
             }).collect(Collectors.toList());
         }

         return result;

    }


    /**
     *查找某角色的员工
     * @param roleType
     * @param shopId
     * @return
     */
    public List<MerchantUserDto> queryUsersByRole(Integer roleType, Long shopId) {
        Assert.notNull(roleType,"角色不能为空！");
        Assert.notNull(shopId,"店铺id不能为空！");
        List<Long> userIds=merchantUserRoleDao.queryUsersByRole(roleType,shopId);
        List<MerchantUserDto> result= Lists.emptyList();
        if(userIds.size()>0){
            List<MerchantUser> merchantUserList= merchantUserDao.queryUsersByIds(userIds);
            result = merchantUserList.stream().map(merchantUser -> {
                MerchantUserDto merchantUserDto = new MerchantUserDto();
                merchantUserDto.setName(merchantUser.getName());
                merchantUserDto.setId(merchantUser.getId());

                return merchantUserDto;
            }).collect(Collectors.toList());
        }
        return result;

    }
}



