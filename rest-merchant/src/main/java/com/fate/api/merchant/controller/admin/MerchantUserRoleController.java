package com.fate.api.merchant.controller.admin;


import com.fate.api.merchant.dto.MerchantUserDto;
import com.fate.api.merchant.dto.PageDto;
import com.fate.api.merchant.service.MerchantUserRoleService;
import com.fate.common.model.StandardResponse;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;

/**
 * @description:
 * @author:
 * @create: 2019-05-22 11:41
 **/
@Api(value = "API - merchantUser", description = "商户员工角色管理")
@RestController("adminMerchantUserRole")
    @RequestMapping("/api/admin/merchantUser/role")
@Slf4j
public class MerchantUserRoleController {

    @Autowired
    private MerchantUserRoleService merchantUserRoleService;


    /**
     * 根据角色查询员工
     */
    @ApiOperation(value = "员工角色管理", notes = "员工角色管理-根据角色查询员工列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "页码", defaultValue = "1", required = false, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "页大小", defaultValue = "10", required = false, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "roleType", value = "角色类型code", required = false, dataType = "Integer", paramType = "query"),


    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @GetMapping("/list")
    public PageDto<MerchantUserDto> getMerchantUserListByRoleOrNull(@RequestParam(defaultValue = "1", required = false) Integer pageIndex,
                                                                    @RequestParam(defaultValue = "10", required = false) Integer pageSize,
                                                                    @RequestParam (required = false) Integer roleType
                          ){

        return merchantUserRoleService.getByMerchantByRoleType(pageIndex, pageSize,roleType);
    }



    /**
     * 生成员工角色
     */
    @ApiOperation(value = "员工角色管理", notes = "员工角色管理-生成员工角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "merchantUserIds", value = "员工集合以，分隔",  required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "roleType", value = "角色code", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "shopId", value = "当前店铺id", required = true, dataType = "Long", paramType = "query"),


    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @PostMapping("/create")
    public StandardResponse generateRoleForMerchantUsersInOneShop(String merchantUserIds,Integer roleType,Long shopId
    ){

         merchantUserRoleService.setMerchantUserInShopByRole(merchantUserIds,roleType,shopId);
         return StandardResponse.success();

    }

    /**
     * 修改员工角色
     */
    @ApiOperation(value = "员工角色管理", notes = "员工角色管理-修改员工角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "shopId", value = "所属店铺",  required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "roleType", value = "角色code", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "merchantUserId", value = "员工id",required = true, dataType = "Long", paramType = "query"),


    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @PostMapping("/update")
    public StandardResponse updateMerchantUserRole(Long shopId,Integer roleType,Long merchantUserId
    ){

        merchantUserRoleService.updateMerchantUserRole(merchantUserId,roleType,shopId);
        return StandardResponse.success();

    }

    /**
     *获取角色候选人
     */
    @ApiOperation(value = "员工角色管理", notes = "员工角色管理-获取某角色候选人")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "shopId", value = "所属店铺id", required = true,dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "roleType", value = "角色code", required = true,dataType = "Integer", paramType = "query")


    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @GetMapping("/candidates/get")
    public List<MerchantUserDto> queryCandidatesByRole(Long shopId, Integer roleType
    ){
        return  merchantUserRoleService.queryCandidatesByRole(roleType,shopId);
    }

    /**
     *获取某角色员工
     */
    @ApiOperation(value = "员工角色管理", notes = "员工角色管理-获取某角色员工")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "shopId", value = "所属店铺id", required = true,  dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "roleType", value = "角色code", required = true, dataType = "Integer", paramType = "query")



    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @GetMapping("/get")
    public List<MerchantUserDto> queryMerchantUsersByRole(Long shopId, Integer roleType
    ){
        return  merchantUserRoleService.queryUsersByRole(roleType,shopId);
    }














}
