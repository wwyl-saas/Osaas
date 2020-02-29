package com.fate.api.customer;

import com.fate.common.entity.*;
import com.fate.common.enums.UserRoleType;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

/**
 * @program: parent
 * @description:
 * @author: chenyixin
 * @create: 2019-06-12 19:27
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
@Slf4j
public class AppointmentTest {

//    @Test
    public void test1() {
//        CurrentInfoUtil.addMerchantApplication(new MerchantApplication().setMerchantId(1L));
        List<Integer> list = Arrays.asList(0,1, 2, 3);
        List<Long> shops = Arrays.asList(1138394767358758914L, 1138394769007120386L, 1138394770064084994L, 1138394771272044545L);
        list.stream().forEach(a->{

            MerchantPostTitle merchantPostTitle=new MerchantPostTitle();
            merchantPostTitle.setName("高级技师");
            merchantPostTitle.setDescription("做广告的技师");
            merchantPostTitle.insert();

            MerchantUser merchantUser=new MerchantUser();
            merchantUser.setAvatarUrl("avtor").setEnabled(true).setGrade(100).setIfAppointment(true)
                    .setMobile("177982320"+a).setName("LadyGaga"+a).setPostTitleId(merchantPostTitle.getId()).setProfileUrl("1.png")
                    .setUserBrief("这是一个牛逼的理发师").setWxApOpenid("").setWxMpOpenid("").setWxUnionid("");
            merchantUser.insert();

            GoodsMerchantUser goodsMerchantUser=new GoodsMerchantUser();
            if (a%2==0){
                goodsMerchantUser.setGoodsId(1138394767014825986L).setMerchantUserId(merchantUser.getId());
                goodsMerchantUser.insert();
            }else {
                goodsMerchantUser.setGoodsId(1138394768357003266L).setMerchantUserId(merchantUser.getId());
                goodsMerchantUser.insert();
            }

            MerchantUserRole merchantUserRole=new MerchantUserRole();
            merchantUserRole.setUserId(merchantUser.getId()).setRoleType(UserRoleType.MANAGER).setShopId(shops.get(a));
            merchantUserRole.insert();

            GoodsShop goodsShop=new GoodsShop();
            goodsShop.setShopId(shops.get(a)).setGoodsId(1138394767014825986L);
            goodsShop.insert();

            goodsShop=new GoodsShop();
            goodsShop.setShopId(shops.get(a)).setGoodsId(1138394769707569154L);
            goodsShop.insert();

            goodsShop=new GoodsShop();
            goodsShop.setShopId(shops.get(a)).setGoodsId(1138666527152992258L);
            goodsShop.insert();


            goodsShop=new GoodsShop();
            goodsShop.setShopId(shops.get(a)).setGoodsId(1138666532827885569L);
            goodsShop.insert();

        });
        MerchantUser merchantUser=new MerchantUser();
        merchantUser.setAvatarUrl("avtor").setEnabled(true).setGrade(100).setIfAppointment(false)
                .setMobile("1779823206").setName("Lily").setPostTitleId(10000L).setProfileUrl("1.png")
                .setUserBrief("这是一个牛逼的理发师").setWxApOpenid("").setWxMpOpenid("").setWxUnionid("");
        merchantUser.insert();



    }

    @Test
    public void test(){

    }
}
