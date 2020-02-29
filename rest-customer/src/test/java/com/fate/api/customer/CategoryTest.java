package com.fate.api.customer;

import com.fate.common.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
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
 * @create: 2019-06-12 12:31
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
@Slf4j
public class CategoryTest {
//    @Before
    public void init(){
//        CurrentInfoUtil.addMerchantApplication(new MerchantApplication().setMerchantId(1L));
        List<Integer> list= Arrays.asList(1,2,3,4);
        list.stream().forEach(a->{
            Category category= Category.builder()
                    .enable(true)
                    .name("分类"+a)
                    .icon("Fu3P9AgpOo_0q_4KnJdn9ZXVimfD")
                    .sortOrder(a)
                    .build();
            category.insert();

            list.stream().forEach(b ->{
//                Goods goods=Goods.builder()
//                        .brifePicUrl("2.jpg")
//                        .categoryId(a.longValue())
//                        .counterPrice(BigDecimal.TEN)
//                        .goodsBrief("这是一个商品")
//                        .goodsDesc("拦截器那边会判断用户是否登录，所以这里注入一个用户")
//                        .isHot(a%2==0)
//                        .isNew(a%2==1)
//                        .isRecommend(a%3==0)
//                        .keywords("关键字吧")
//                        .listPicUrls("1.png,2.jpg")
//                        .marketPrice(BigDecimal.ONE)
//                        .name("商品"+a)
//                        .onSale(true)
//                        .primaryPicUrls("1.png,2.jpg")
//                        .sellVolume(99)
//                        .sortOrder(2)
//                        .build();
//                goods.insert();
            });


        });
    }

    @Test
    public void testData(){

    }
}
