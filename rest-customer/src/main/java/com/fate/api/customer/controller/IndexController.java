package com.fate.api.customer.controller;

import com.fate.api.customer.dto.*;
import com.fate.api.customer.service.BannerService;
import com.fate.api.customer.service.GoodsService;
import com.fate.api.customer.service.MerchantService;
import com.fate.common.entity.Banner;
import com.fate.common.entity.MerchantInfo;
import com.fate.common.enums.BannerPoisition;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: parent
 * @description: 首页相关
 * @author: chenyixin
 * @create: 2019-05-22 23:12
 **/
@Api(value = "API - index", description = "小程序首页相关接口")
@RestController
@RequestMapping("/api/v1/index")
@Slf4j
public class IndexController {
    @Autowired
    BannerService bannerService;
    @Autowired
    GoodsService goodsService;
    @Autowired
    MerchantService merchantService;

    /**
     * 获取首页轮播图
     */
    @ApiOperation(value="获取首页轮播图", notes="")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @GetMapping("/banner")
    public List<Banner> banner(){
        List<Banner> banners=bannerService.getBanners(BannerPoisition.INDEX);
        return banners;
    }


    /**
     * 获取首页热销商品数据，推荐商品数据，活动数据
     */
    @ApiOperation(value="获取首页商品信息,商铺信息", notes="热销商品数据，推荐商品数据，活动数据")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @GetMapping("/data")
    public IndexDto data(){
        List<GoodsDto> newGoods=goodsService.getOnSaleNewGoodsLimit(4);
        IndexGoodsDto newIndexGoods=IndexGoodsDto.builder().name("新品上市").descArray(new String[]{"选用最新科技","瞄准时尚前沿","服务全面升级"}).img("http://cdn.wanwuyoulian.com/bj1.png").goodsList(newGoods).build();

        List<GoodsDto> hotGoods=goodsService.getOnSaleHotGoodsLimit(4);
        IndexGoodsDto hotIndexGoods=IndexGoodsDto.builder().name("热销单品").descArray(new String[]{"上万粉丝热捧","专注客户体验","大师级作品"}).img("http://cdn.wanwuyoulian.com/bj2.png").goodsList(hotGoods).build();

        List<GoodsDto> recommendGoods=goodsService.getOnSaleRecomendGoods(4);
        IndexGoodsDto recommendIndexGoods=IndexGoodsDto.builder().name("强烈推荐").descArray(new String[]{"超高性价比","旗舰店首推作品"}).img("http://cdn.wanwuyoulian.com/bj3.png").goodsList(recommendGoods).build();

        return IndexDto.builder()
                .newGoods(newIndexGoods)
                .hotGoods(hotIndexGoods)
                .recommendGoods(recommendIndexGoods)
                .build();
    }

    /**
     * 获取门店信息
     */
    @ApiOperation(value="获取门店信息", notes="获取首页商铺信息和位置信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "longitude", value = "用户经度", dataType = "double", paramType = "query"),
            @ApiImplicitParam(name = "latitude", value = "用户维度",  dataType = "double", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @GetMapping("/shop")
    public MerchantIndexDto shop(@RequestParam(required = false) Double longitude,
                         @RequestParam(required = false) Double latitude){
        MerchantInfo merchantInfo=merchantService.getMerchantInfo();
        MerchantIndexDto merchantIndexDto=MerchantIndexDto.builder().name(merchantInfo.getBriefName()).desc(merchantInfo.getDescription()).build();

        List<MerchantShopDto> shopDtos=merchantService.getMerchantShopDtos(longitude,latitude);
        merchantIndexDto.setShops(shopDtos);
        merchantIndexDto.setWorkTime("全周营业 9:00至21:00");
        return merchantIndexDto;
    }

}
