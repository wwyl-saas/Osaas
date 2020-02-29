package com.fate.api.merchant.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fate.api.merchant.dto.*;
import com.fate.api.merchant.query.MerchantUserQuery;
import com.fate.api.merchant.query.UserUpdateQuery;
import com.fate.api.merchant.util.CurrentUserUtil;
import com.fate.common.dao.GoodsMerchantUserDao;
import com.fate.common.dao.MerchantPostTitleDao;
import com.fate.common.dao.MerchantUserDao;
import com.fate.common.dao.MerchantUserRoleDao;
import com.fate.common.entity.*;
import com.fate.common.enums.ResponseInfo;
import com.fate.common.enums.UserRoleType;
import com.fate.common.util.BeanUtil;
import com.fate.common.util.CurrentMerchantUtil;
import com.fate.common.util.PingYinUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
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
 * @description: 商户用户
 * @author: lpx
 **/
@Service
@Slf4j
public class MerchantUserService {
    private static final String CAMERA="camera.jpg";
    @Value("${cdn.domain}")
    private String cdnDomain;

    @Resource
    MerchantUserDao merchantUserDao;
    @Resource
    private MerchantUserRoleDao merchantUserRoleDao;
    @Resource
    private MerchantPostTitleDao merchantPostTitleDao;
    @Autowired
    private CommentService commentService;
    @Resource
    GoodsMerchantUserDao goodsMerchantUserDao;
    @Autowired
    MerchantService merchantService;

    /**
     * 查询门店员工列表
     * @param pageIndex
     * @param pageSize
     * @param shopId
     * @return
     */
    public PageDto<MerchantUserDto> getUserList(Integer pageIndex, Integer pageSize,Long shopId) {
        IPage<MerchantUser> page = merchantUserDao.getPageByShopId(pageIndex,pageSize,shopId, CurrentMerchantUtil.getMerchant().getId());
        List<MerchantUserDto> result = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(page.getRecords())){
            result=page.getRecords().stream().map(merchantUser -> BeanUtil.mapper(merchantUser,MerchantUserDto.class)).collect(Collectors.toList());
        }
        return new PageDto<>(pageIndex,pageSize,page.getTotal(),page.getPages(),result);
    }

    /**
     * 用户详情
     * @param userId
     * @return
     */
    public MerchantUserDto detail(Long userId,Long shopId) {
        MerchantUser merchantUser = merchantUserDao.getById(userId);
        Assert.notNull(merchantUser, "该员工不存在");
        MerchantUserDto dto = BeanUtil.mapper(merchantUser, MerchantUserDto.class);
        MerchantUserRole merchantUserRole=merchantUserRoleDao.getShopRoleByShopAndUserId(shopId,userId);
        Assert.notNull(merchantUserRole, "该员工岗位职称不存在");
        dto.setRoleTypeCode(merchantUserRole.getRoleType().getCode());
        if (StringUtils.isNotBlank(dto.getProfileUrl())){
            dto.setProfileUrl(cdnDomain+dto.getProfileUrl());
        }
        List<GoodsMerchantUser> goodsMerchantUsers=goodsMerchantUserDao.getByMerchantUserId(userId);
        if (CollectionUtils.isNotEmpty(goodsMerchantUsers)){
            List<Long> goodsIds=goodsMerchantUsers.stream().map(goodsMerchantUser -> goodsMerchantUser.getGoodsId()).collect(Collectors.toList());
            dto.setGoodsIds(goodsIds);
        }
        return dto;
    }

    /**
     * 新增用户或者总店引入
     * @param query
     */
    public MerchantUserDto save(MerchantUserQuery query) {
        MerchantUser merchantUser=null;
        if (query.getId()!=null){//总店引入用户
            merchantUser=merchantUserDao.getById(query.getId());
            Assert.notNull(merchantUser,"用户不存在");
        }else{//直接新增
            merchantUser =merchantUserDao.getByMobile(query.getMobile());
            Assert.isNull(merchantUser,"用户手机号已存在注册");
            merchantUser=new MerchantUser()
                    .setName(query.getName())
                    .setNickname(StringUtils.isNotBlank(query.getNickname())?query.getNickname():PingYinUtil.getPinYinHeadChar(query.getName()))
                    .setPingYin(PingYinUtil.getPinYinHeadChar(query.getName()))
                    .setEnabled(true)
                    .setGrade(35)
                    .setIfAppointment(query.getIfAppointment())
                    .setMobile(query.getMobile())
                    .setPassword("123456")
                    .setPostTitleId(query.getPostTitleId());
            Assert.isTrue(merchantUser.insert(),"插入数据失败");
        }
        MerchantUserRole merchantUserRole = new MerchantUserRole();
        merchantUserRole.setRoleType(UserRoleType.getEnum(query.getRoleTypeCode()).get());
        merchantUserRole.setUserId(merchantUser.getId());
        merchantUserRole.setShopId(query.getShopId());
        Assert.isTrue(merchantUserRole.insert(),"插入数据失败");

        Long userId=merchantUser.getId();
        //todo 门店的修改行为改变全局的配置
        if (CollectionUtils.isNotEmpty(query.getGoodsIds())){
            goodsMerchantUserDao.deleteByUserId(merchantUser.getId());
            List<GoodsMerchantUser> goodsMerchantUsers=new ArrayList<>();
            query.getGoodsIds().stream().forEach(goodsId->{
                GoodsMerchantUser goodsMerchantUser=new GoodsMerchantUser();
                goodsMerchantUser.setGoodsId(goodsId)
                        .setMerchantUserId(userId);
                goodsMerchantUsers.add(goodsMerchantUser);
            });
            goodsMerchantUserDao.saveBatch(goodsMerchantUsers);
        }

        MerchantUserDto result=BeanUtil.mapper(merchantUser,MerchantUserDto.class);
        result.setRoleType(merchantUserRole.getRoleType());
        return result;
    }



    /**
     * admin新增用户
     * @param query
     */
    public MerchantUserDto insert(MerchantUserQuery query) {
        MerchantUser merchantUser=null;
        if (query.getId()!=null){//总店引入用户
            merchantUser=merchantUserDao.getById(query.getId());
            Assert.notNull(merchantUser,"用户不存在");
            merchantUser=new MerchantUser()
                    .setName(query.getName())
                    .setNickname(StringUtils.isNotBlank(query.getNickname())?query.getNickname():PingYinUtil.getPinYinHeadChar(query.getName()))
                    .setPingYin(PingYinUtil.getPinYinHeadChar(query.getName()))
                    .setEnabled(true)
                    .setGrade(35)
                    .setIfAppointment(query.getIfAppointment())
                    .setMobile(query.getMobile())
                    .setPassword("123456")
                    .setPostTitleId(query.getPostTitleId());
            Assert.isTrue(merchantUser.insert(),"插入数据失败");
        }
        MerchantUserRole merchantUserRole = new MerchantUserRole();
        merchantUserRole.setRoleType(UserRoleType.getEnum(query.getRoleTypeCode()).get());
        merchantUserRole.setUserId(merchantUser.getId());
        merchantUserRole.setShopId(query.getShopId());
        Assert.isTrue(merchantUserRole.insert(),"插入数据失败");

        Long userId=merchantUser.getId();
        //todo 门店的修改行为改变全局的配置
        if (CollectionUtils.isNotEmpty(query.getGoodsIds())){
            goodsMerchantUserDao.deleteByUserId(merchantUser.getId());
            List<GoodsMerchantUser> goodsMerchantUsers=new ArrayList<>();
            query.getGoodsIds().stream().forEach(goodsId->{
                GoodsMerchantUser goodsMerchantUser=new GoodsMerchantUser();
                goodsMerchantUser.setGoodsId(goodsId)
                        .setMerchantUserId(userId);
                goodsMerchantUsers.add(goodsMerchantUser);
            });
            goodsMerchantUserDao.saveBatch(goodsMerchantUsers);
        }

        MerchantUserDto result=BeanUtil.mapper(merchantUser,MerchantUserDto.class);
        result.setRoleType(merchantUserRole.getRoleType());
        return result;
    }

    /**
     * 删除员工
     * @param userId
     */
    public void remove(Long userId,Long shopId) {
        Assert.isTrue(merchantUserDao.removeById(userId),"删除失败");
        merchantUserRoleDao.removeByUserId(userId,shopId);
    }

    /**
     * 通过openID查询用户
     * @param openId
     * @return
     */
    public MerchantUser getByAppletOpenId(String openId) {
       return merchantUserDao.getByAppletOpenId(openId);
    }

    public MerchantUser getByPhone(String phone) {
        return merchantUserDao.getByMobile(phone);
    }

    public MerchantUser getById(Long userId) {
        return merchantUserDao.getByUserId(userId);
    }


    /**
     * 获取角色
     * @param shopId
     * @param userId
     * @return
     */
    @Cacheable(value="MerchantUserRoles",unless="#result == null", key="T(String).valueOf(#shopId).concat(':').concat(#userId)")
    public MerchantUserRole getRoleByShopAndUserId(Long shopId, Long userId) {
        return merchantUserRoleDao.getShopRoleByShopAndUserId(shopId,userId);
    }

    /**
     * 批量查询商户用户
     * @param merchantUserIds
     * @return
     */
    public List<MerchantUser> getMerchantUserByIds(List<Long> merchantUserIds) {
        return merchantUserDao.getMerchantUserByIds(merchantUserIds);
    }

    /**
     * 个人中心
     * @return
     */
    public UserCenterDto personalCenter(Long shopId) {
        //todo 优化缓存的问题
        MerchantUser merchantUser = merchantUserDao.getById(CurrentUserUtil.getMerchantUser().getId());
        UserCenterDto dto =BeanUtil.mapper(merchantUser, UserCenterDto.class);
        if (merchantUser.getGrade()!=null){
            BigDecimal grade=BigDecimal.valueOf(merchantUser.getGrade().longValue()).divide(BigDecimal.TEN).setScale(1,BigDecimal.ROUND_HALF_UP);
            dto.setGradeString(grade.toPlainString());
        }
        if (StringUtils.isNotBlank(dto.getProfileUrl())){
            dto.setProfileUrl(cdnDomain+dto.getProfileUrl());
        }else {
            dto.setProfileUrl(cdnDomain+CAMERA);
        }

        MerchantUserRole userRole = merchantUserRoleDao.getShopRoleByShopAndUserId(shopId, merchantUser.getId());
        if (userRole!=null){
            dto.setRoleType(userRole.getRoleType());
        }
        MerchantPostTitle title = merchantPostTitleDao.getById(merchantUser.getPostTitleId());
        if (title!=null){
            dto.setPostTitle(title.getName());
        }

        Integer count = commentService.countByShopIdAndUserId();
        dto.setCommentCount(count);
        List<Comment> comments=commentService.getUserLastCommentsLimit(merchantUser.getId(),4);
        List<String> avatars=null;
        if (CollectionUtils.isNotEmpty(comments)){
            avatars=comments.stream().map(comment -> comment.getAvatar()).collect(Collectors.toList());
        }
        dto.setCommentAvatarUrls(avatars);
        return dto;
    }

    /**
     * 修改密码
     * @param psw
     * @param confirmPsw
     */
    public void changePsw(String psw, String confirmPsw) {
        /** 修改密码 **/
        Assert.isTrue(StringUtils.isEmpty(psw), ResponseInfo.PARAM_NULL.getMsg());
        Assert.isTrue(!Objects.equals(psw, confirmPsw), ResponseInfo.PSW_CONFIRMPSW_NOTEQ.getMsg());
        merchantUserDao.updatePsw(CurrentUserUtil.getMerchantUser().getId(), psw);
    }


    /**
     * 通过ID查询merchantUser
     * @param merchantUserId
     * @return
     */
    public MerchantUser getMerchantUserById(Long merchantUserId) {
        return merchantUserDao.getById(merchantUserId);
    }

    /**
     * 获取店铺预约消息通知人员
     * @param shopId
     * @return
     */
    public List<MerchantUser> getNoticeEventMerchantUser(Long shopId) {
        return getByShopId(shopId);
    }

    /**
     * 根据店铺获取员工列表
     * @param shopId
     * @return
     */
    public List<MerchantUser> getByShopId(Long shopId) {
        Assert.notNull(shopId,"shopid不能为空");
        List<MerchantUser> merchantUsers= org.assertj.core.util.Lists.emptyList();
        //获取店铺所有技师
        List<Long> shopUserIds=getMerchantUsersIdsByShopId(shopId);
        if (CollectionUtils.isNotEmpty(shopUserIds)){
            merchantUsers= (List<MerchantUser>) merchantUserDao.listByIds(shopUserIds);
        }
        return merchantUsers;
    }


    /**
     * 根据店铺ID获取商户用户权限信息
     * @param shopId
     * @return
     */
    public  List<Long> getMerchantUsersIdsByShopId(Long shopId){
        return merchantUserRoleDao.getMerchantUserIdsByShopId(shopId);
    }

    /**
     * 更新用户，注意此方法适用于无currentInfo的情况下
     * @param user
     */
    public void updateUser(MerchantUser user) {
        merchantUserDao.updateUser(user);
    }


    /**
     * 根据商品获取技师列表
     * @param goodsId
     * @return
     */
    public List<GoodsMerchantUser> getByGoodsId(Long goodsId){
        return goodsMerchantUserDao.getByGoodsId(goodsId);
    }

    /**
     * 根据预约商品ID获取技师Id
     * @param goodsId
     * @return
     */
    public List<Long> getMerchantUserIdsByGoodsId(Long goodsId){
        List<Long> goodsMerchantUserIds= Lists.newArrayList();
        if (goodsId!=null){
            List<GoodsMerchantUser> goodsMerchantUsers=getByGoodsId(goodsId);
            if (CollectionUtils.isNotEmpty(goodsMerchantUsers)){
                goodsMerchantUserIds=goodsMerchantUsers.stream().map(GoodsMerchantUser::getMerchantUserId).collect(Collectors.toList());
            }
        }
        return goodsMerchantUserIds;
    }

    /**
     * 更新用户信息
     * @param query
     */
    public MerchantUserDto update(MerchantUserQuery query) {
        MerchantUser merchantUser=merchantUserDao.getById(query.getId());
        Assert.notNull(merchantUser,"更新的用户不存在");
        merchantUser.setMobile(query.getMobile());
        merchantUser.setName(query.getName());
        merchantUser.setPingYin(PingYinUtil.getPinYinHeadChar(query.getName()));
        merchantUser.setPostTitleId(query.getPostTitleId());
        merchantUser.setNickname(StringUtils.isNotBlank(query.getNickname())?query.getNickname():PingYinUtil.getPinYinHeadChar(query.getName()));
        merchantUser.setIfAppointment(query.getIfAppointment());
        Assert.isTrue(merchantUser.updateById(),"数据更新失败");

        merchantUserRoleDao.removeByUserId(query.getId(),query.getShopId());
        MerchantUserRole merchantUserRole = new MerchantUserRole();
        merchantUserRole.setRoleType(UserRoleType.getEnum(query.getRoleTypeCode()).get());
        merchantUserRole.setUserId(merchantUser.getId());
        merchantUserRole.setShopId(query.getShopId());
        Assert.isTrue(merchantUserRole.insert(),"插入数据失败");

        Long userId=merchantUser.getId();
        //todo 门店的修改行为改变全局的配置
        if (CollectionUtils.isNotEmpty(query.getGoodsIds())){
            goodsMerchantUserDao.deleteByUserId(merchantUser.getId());
            List<GoodsMerchantUser> goodsMerchantUsers=new ArrayList<>();
            query.getGoodsIds().stream().forEach(goodsId->{
                GoodsMerchantUser goodsMerchantUser=new GoodsMerchantUser();
                goodsMerchantUser.setGoodsId(goodsId)
                        .setMerchantUserId(userId);
                goodsMerchantUsers.add(goodsMerchantUser);
            });
            goodsMerchantUserDao.saveBatch(goodsMerchantUsers);
        }

        MerchantUserDto result=BeanUtil.mapper(merchantUser,MerchantUserDto.class);
        result.setRoleType(merchantUserRole.getRoleType());
        return result;
    }

    /**
     * 个人信息更新
     * @param query
     */
    public void updateSelf(UserUpdateQuery query) {
        MerchantUser merchantUser = CurrentUserUtil.getMerchantUser();
        if (StringUtils.isNotBlank(query.getNickname())){
            merchantUser.setNickname(query.getNickname());
        }
        if (StringUtils.isNotBlank(query.getProfileUrl())){
            merchantUser.setProfileUrl(query.getProfileUrl());
        }
        if (StringUtils.isNotBlank(query.getUserBrief())){
            merchantUser.setUserBrief(query.getUserBrief());
        }
        if (query.getIfAppointment()!=null){
            merchantUser.setIfAppointment(query.getIfAppointment());
        }
        Assert.isTrue(merchantUser.updateById(),"更新数据失败");
    }

    /**
     * 获取门店员工列表
     * @param shopId
     * @return
     */
    public List<SelectListDto> userList(Long shopId) {
        List<SelectListDto> result=Lists.newArrayList();
        List<MerchantUser> users=merchantUserDao.getByShopId(shopId,CurrentMerchantUtil.getMerchant().getId());
        if (CollectionUtils.isNotEmpty(users)){
            result=users.stream().map(merchantUser ->  SelectListDto.builder().code(merchantUser.getId()).name(merchantUser.getName()).build()).collect(Collectors.toList());
        }
        return result;
    }

    /**
     * 获取员工信息和公司级别角色
     * @param userId
     * @return
     */
    @Cacheable(value = "merchantUsers" ,unless="#result == null", key="#userId")
    public MerchantUser getByUserId(Long userId) {
        MerchantUser merchantUser=merchantUserDao.getByUserId(userId);
        return merchantUser;
    }

    /**
     * 返回会员索引列表
     * @param shopId
     * @return
     */
    public List<MerchantUserIndexDto> getUserIndexList(Long shopId,String userName) {
        List<MerchantUserIndexDto> result = Lists.newArrayList();
        List<MerchantUser> userList=merchantUserDao.getByShopIdAndName(shopId,userName,CurrentMerchantUtil.getMerchant().getId());
        Map<String,List<MerchantUserDto>> map=null;
        if (CollectionUtils.isNotEmpty(userList)){
            map=userList.stream().map(merchantUser ->{
                MerchantUserDto dto=BeanUtil.mapper(merchantUser,MerchantUserDto.class);
                if (StringUtils.isNotBlank(dto.getProfileUrl())){
                    dto.setProfileUrl(cdnDomain+dto.getProfileUrl());
                }
                String pingyin=dto.getPingYin();
                if (StringUtils.isNotBlank(pingyin)){
                    dto.setPingYin(String.valueOf(pingyin.charAt(0)).toUpperCase());
                }else{
                    dto.setPingYin("A");
                }
                return dto;
            }).collect(Collectors.groupingBy(MerchantUserDto::getPingYin));
        }
        if (MapUtils.isNotEmpty(map)){
            for(Map.Entry<String,List<MerchantUserDto>> entry:map.entrySet()){
                MerchantUserIndexDto indexDto= MerchantUserIndexDto.builder().indexLetter(entry.getKey()).userList(entry.getValue()).build();
                result.add(indexDto);
            }
        }
        result=result.stream().sorted(Comparator.comparing(indexDto->indexDto.getIndexLetter())).collect(Collectors.toList());
        return result;
    }

    /**
     * 获取用户门店角色map
     * @param userId
     * @return
     */
    @Cacheable(value="userShopRoleMap",unless="#result == null", key="#userId")
    public Map<String, UserRoleType> getRolesByUserId(Long userId) {
        Map<String, UserRoleType> roleTypeMap=new HashMap<>();
        MerchantUserRole userRole=merchantUserRoleDao.getMerchantRoleByUserId(userId);
        if (userRole!=null){
            List<MerchantShop> merchantShops=merchantService.getShopList();
            Assert.notEmpty(merchantShops,"商户门店未设置");
            merchantShops.stream().forEach(merchantShop -> roleTypeMap.put(merchantShop.getId().toString(),userRole.getRoleType()));
        }else{
            List<MerchantUserRole> roles=merchantUserRoleDao.getByUserId(userId);
            Assert.notEmpty(roles,"用户商户角色未设置");
            roles.stream().forEach(merchantUserRole->roleTypeMap.put(merchantUserRole.getShopId().toString(),merchantUserRole.getRoleType()));
        }
        return  roleTypeMap;
    }

    /**
     * 手机号查询用户详情信息
     * @param mobile
     * @return
     */
    public MerchantUserDto detailByMobile(String mobile,Long shopId) {
        MerchantUser merchantUser=merchantUserDao.getByMerchantMobile(mobile);
        Assert.notNull(merchantUser, "该手机号对应总店员工不存在");
        MerchantUserRole merchantUserRole=merchantUserRoleDao.getShopRoleByShopAndUserId(shopId,merchantUser.getId());
        Assert.isNull(merchantUserRole,"此用户已经是门店员工，无须引入");
        MerchantUserDto result = BeanUtil.mapper(merchantUser, MerchantUserDto.class);
        if (StringUtils.isNotBlank(result.getProfileUrl())){
            result.setProfileUrl(cdnDomain+result.getProfileUrl());
        }
        List<GoodsMerchantUser> goodsMerchantUsers=goodsMerchantUserDao.getByMerchantUserId(merchantUser.getId());
        if (CollectionUtils.isNotEmpty(goodsMerchantUsers)){
            List<Long> goodsIds=goodsMerchantUsers.stream().map(goodsMerchantUser -> goodsMerchantUser.getGoodsId()).collect(Collectors.toList());
            result.setGoodsIds(goodsIds);
        }
        return result;
    }

    /**
     * 手机号和姓名查询用户信息
     * @param mobile
     * @return
     */
    public PageDto<MerchantUserDto> getByMerchantByMobileAndName(Integer pageIndex, Integer pageSize, String mobile, String merchantUserName) {
        MerchantUser currentMerchantUser = CurrentUserUtil.getMerchantUser();
        IPage<MerchantUser> page = merchantUserDao.getPageByMobileAndName(pageIndex,pageSize,mobile, merchantUserName,currentMerchantUser.getMerchantId());
        List<Long> postTitleIdsList=new ArrayList<Long>();

        List<MerchantUser>  merchantUsers=new ArrayList<MerchantUser>();
        if (CollectionUtils.isNotEmpty(page.getRecords())){
            merchantUsers=page.getRecords();
        }
        for(MerchantUser merchantUser:merchantUsers){
            postTitleIdsList.add(Long.valueOf(String.valueOf(merchantUser.getPostTitleId())));
        }
        List<Map<String,Object>> titleId2NameMap= merchantPostTitleDao.getPostTitlesByIds(postTitleIdsList);

        List<MerchantUserDto> result = Lists.newArrayList();

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

        return new PageDto<>(pageIndex,pageSize,page.getTotal(),page.getPages(),result);



    }

    /**
     * 获取管理层
     * @return
     */
    public List<MerchantUser> getManagerUser() {
        List<MerchantUser> result=Lists.newArrayList();
        List<UserRoleType> roles=new ArrayList<>();
        roles.add(UserRoleType.MANAGER);
        roles.add(UserRoleType.MANAGER_ASSISTANT);
        List<MerchantUserRole> merchantUserRoles=merchantUserRoleDao.getByRoleType(roles);
        if (CollectionUtils.isNotEmpty(merchantUserRoles)){
            List<Long> merchantUserIds=merchantUserRoles.stream().map(role->role.getUserId()).collect(Collectors.toList());
            result=merchantUserDao.getMerchantUserByIds(merchantUserIds);
        }
        return result;
    }


    /**
     * 根据unionId获取用户信息
     * @param wxUnoinid
     * @return
     */
    public MerchantUser getByUnionId(String wxUnoinid) {
       return merchantUserDao.getByUnionId(wxUnoinid);
    }

    /**
     * 获取反馈事件的通知用户
     * @return
     */
    public List<MerchantUser> getFeedbackNoticeUser() {
        List<UserRoleType> userRoleTypes=new ArrayList<>();
        userRoleTypes.add(UserRoleType.SHOP_OWNER);
        userRoleTypes.add(UserRoleType.MANAGER_ASSISTANT);
        userRoleTypes.add(UserRoleType.MANAGER);
        List<MerchantUserRole> userRoles=merchantUserRoleDao.getByRoleType(userRoleTypes);
        if (CollectionUtils.isNotEmpty(userRoles)){
            List<Long> userIds=userRoles.stream().map(merchantUserRole -> merchantUserRole.getUserId()).collect(Collectors.toList());
            return merchantUserDao.getByUserIds(userIds);
        }
        return Lists.newArrayList();
    }
}
