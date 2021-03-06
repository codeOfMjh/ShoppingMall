package com.mjh.controller;

import com.mjh.pojo.UserAddress;
import com.mjh.pojo.bo.AddressBO;
import com.mjh.service.UserAddressService;
import com.mjh.utils.MJHJSONResult;
import com.mjh.utils.MobileEmailUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "地址相关", tags = {"地址相关的api接口"})
@RequestMapping("address")
@RestController
public class UserAddressController {

    /**
     * 用户在确认订单页面，可以针对收货地址做如下操作：
     * 1. 查询用户的所有收货地址列表
     * 2. 新增收货地址
     * 3. 删除收货地址
     * 4. 修改收货地址
     * 5. 设置默认地址
     */

    // 注入业务层
    @Autowired
    private UserAddressService userAddressService;

    @ApiOperation(value = "根据用户id查询收货地址列表", notes = "根据用户id查询收货地址列表", httpMethod = "POST")
    @PostMapping("/list")
    public MJHJSONResult list(
            @RequestParam String userId) {

        if (StringUtils.isBlank(userId)) {
            return MJHJSONResult.errorMsg("");
        }

        List<UserAddress> list = userAddressService.queryAllUserAddress(userId);
        return MJHJSONResult.ok(list);
    }

    @ApiOperation(value = "用户新增地址", notes = "用户新增地址", httpMethod = "POST")
    @PostMapping("/add")
    public MJHJSONResult add(@RequestBody AddressBO addressBO) {

        MJHJSONResult checkRes = checkAddress(addressBO);
        if (checkRes.getStatus() != 200) {
            return checkRes;
        }

        userAddressService.addNewUserAddress(addressBO);

        return MJHJSONResult.ok();
    }

    @ApiOperation(value = "用户修改地址", notes = "用户修改地址", httpMethod = "POST")
    @PostMapping("/update")
    public MJHJSONResult update(@RequestBody AddressBO addressBO) {

        if (StringUtils.isBlank(addressBO.getAddressId())) {
            return MJHJSONResult.errorMsg("修改地址错误：addressId不能为空");
        }

        MJHJSONResult checkRes = checkAddress(addressBO);
        if (checkRes.getStatus() != 200) {
            return checkRes;
        }

        userAddressService.updateUserAddress(addressBO);

        return MJHJSONResult.ok();
    }

    @ApiOperation(value = "用户删除地址", notes = "用户删除地址", httpMethod = "POST")
    @PostMapping("/delete")
    public MJHJSONResult delete(
            @RequestParam String userId,
            @RequestParam String addressId) {

        if (StringUtils.isBlank(userId) || StringUtils.isBlank(addressId)) {
            return MJHJSONResult.errorMsg("");
        }

        userAddressService.deleteUserAddress(userId, addressId);
        return MJHJSONResult.ok();
    }

    @ApiOperation(value = "用户设置默认地址", notes = "用户设置默认地址", httpMethod = "POST")
    @PostMapping("/setDefalut")
    public MJHJSONResult setDefalut(
            @RequestParam String userId,
            @RequestParam String addressId) {

        if (StringUtils.isBlank(userId) || StringUtils.isBlank(addressId)) {
            return MJHJSONResult.errorMsg("");
        }

        userAddressService.updateUserAddressToBeDefault(userId, addressId);
        return MJHJSONResult.ok();
    }

    // 对用户地址信息对象进行健壮性判断返回正常的地址信息对象
    private MJHJSONResult checkAddress(AddressBO addressBO) {
        String receiver = addressBO.getReceiver();
        if (StringUtils.isBlank(receiver)) {
            return MJHJSONResult.errorMsg("收货人不能为空");
        }
        if (receiver.length() > 12) {
            return MJHJSONResult.errorMsg("收货人姓名不能太长");
        }

        String mobile = addressBO.getMobile();
        if (StringUtils.isBlank(mobile)) {
            return MJHJSONResult.errorMsg("收货人手机号不能为空");
        }
        if (mobile.length() != 11) {
            return MJHJSONResult.errorMsg("收货人手机号长度不正确");
        }
        boolean isMobileOk = MobileEmailUtils.checkMobileIsOk(mobile);
        if (!isMobileOk) {
            return MJHJSONResult.errorMsg("收货人手机号格式不正确");
        }

        String province = addressBO.getProvince();
        String city = addressBO.getCity();
        String district = addressBO.getDistrict();
        String detail = addressBO.getDetail();
        if (StringUtils.isBlank(province) ||
                StringUtils.isBlank(city) ||
                StringUtils.isBlank(district) ||
                StringUtils.isBlank(detail)) {
            return MJHJSONResult.errorMsg("收货地址信息不能为空");
        }

        return MJHJSONResult.ok();
    }
}
