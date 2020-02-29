package com.fate.api.customer;

import com.fate.common.entity.*;
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
 * @create: 2019-06-12 17:13
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
@Slf4j
public class GoodsTest {

//    @Test
    public void test1() {
//        CurrentInfoUtil.addMerchantApplication(new MerchantApplication().setMerchantId(1L));
        List<Integer> list = Arrays.asList(1, 2, 3, 4);
//        list.stream().forEach(a -> {
//            GoodsAttribute attribute = new GoodsAttribute();
//            attribute.setGoodsId(1138394767014825986L);
//            attribute.setName("时长");
//            attribute.setSortOrder(1);
//            attribute.setValue(a * 10 + "分钟");
//            attribute.insert();
//        });
//
//        GoodsShop goodsShop=new GoodsShop();
//        goodsShop.setGoodsId(1138394767014825986L);
//        goodsShop.setShopId(1138394767358758914L);
//        goodsShop.insert();
//        goodsShop=new GoodsShop();
//        goodsShop.setGoodsId(1138394767014825986L);
//        goodsShop.setShopId(1138394769007120386L);
//        goodsShop.insert();
//        goodsShop=new GoodsShop();
//        goodsShop.setGoodsId(1138394767014825986L);
//        goodsShop.setShopId(1138394770064084994L);
//        goodsShop.insert();
//        goodsShop=new GoodsShop();
//        goodsShop.setGoodsId(1138394767014825986L);
//        goodsShop.setShopId(1138394771272044545L);
//        goodsShop.insert();
//
//        list.stream().forEach(a -> {
//            Comment comment=new Comment();
//            comment.setGoodsId(1138394767014825986L);
//            comment.setAvatar("acator");
//            comment.setCustomerName("客户名称"+a);
//            comment.setGrade(18);
//            comment.setMerchantUserId(839283L);
//            comment.setOrderId(0L);
//            comment.setPictureUrls("1.png,2.jpg");
//            comment.setRemark("这是一个评价");
//            comment.setShopId(1138394771272044545L);
//            comment.insert();
//        });

        list.stream().forEach(a -> {
            GoodsIssue issue = new GoodsIssue();
            issue.setGoodsId(1138394767014825986L);
            issue.setQuestion("这是一个问题" + a);
            issue.setAnswer("回答" + a);
            issue.insert();
        });


    }

    @Test
    public void test(){

    }
}
