package com.mjh.service.impl;

import com.mjh.enums.YesOrNo;
import com.mjh.mapper.UserAddressMapper;
import com.mjh.pojo.UserAddress;
import com.mjh.pojo.bo.AddressBO;
import com.mjh.service.UserAddressService;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @PackageName: com.mjh.service.impl
 * @ClassName: UserAddressServiceImpl
 * @Author: majiahuan
 * @Date: 2020/12/14 22:27
 * @Description: 用户地址接口类
 */

@Service
public class UserAddressServiceImpl implements UserAddressService {

    // 注入数据访问层
    @Autowired
    private UserAddressMapper userAddressMapper;

    // 注入Id算法
    @Autowired
    private Sid sid;

    // 查询用户所有地址信息列表
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<UserAddress> queryAllUserAddress(String userId) {
        // 1. 创建用户地址对象
        UserAddress userAddress = new UserAddress();
        // 2. 为用户地址对象设置Id
        userAddress.setUserId(userId);
        // 3. 根据用户Id数据库检索用户地址信息列表返回
        return userAddressMapper.select(userAddress);
    }

    // 添加用户地址信息列表
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void addNewUserAddress(AddressBO addressBO) {
        // 1. 判断当前用户是否存在地址，如果没有，则新增为‘默认地址’；初始化默认地址标签为1
        Integer isDefault = 0;
        // 2. 获取默认地址列表信息
        List<UserAddress> addressList = this.queryAllUserAddress(addressBO.getUserId());
        if (addressList == null || addressList.isEmpty() || addressList.size() == 0) {
            // 3. 将新设地址默认字段置为1
            isDefault = 1;
        }

        // 4. 获取默认地址Id值
        String addressId = sid.nextShort();

        // 5. 保存地址到数据库
        // 5.1 创建用于封装地址信息的对象
        UserAddress newAddress = new UserAddress();
        // 5.2 将前端传递的BO对象转为地址对象的实例对象
        BeanUtils.copyProperties(addressBO, newAddress);
        // 5.3 封装地址信息
        newAddress.setId(addressId);
        newAddress.setIsDefault(isDefault);
        newAddress.setCreatedTime(new Date());
        newAddress.setUpdatedTime(new Date());

        // 6. 向数据库添加默认地址信息
        userAddressMapper.insert(newAddress);
    }

    // 跟新用户地址信息列表
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUserAddress(AddressBO addressBO) {
        //1. 获取用户地址信息Id
        String addressId = addressBO.getAddressId();
        //2. 创建用户地址信息对象
        UserAddress pendingAddress = new UserAddress();
        //3. 将接收的对象转为用户地址信息对象
        BeanUtils.copyProperties(addressBO, pendingAddress);
        //4. 为用户地址信息对象进行参数封装
        pendingAddress.setId(addressId);
        pendingAddress.setUpdatedTime(new Date());
        //5. 更新用户地址信息
        userAddressMapper.updateByPrimaryKeySelective(pendingAddress);
    }

    // 删除用户地址信息列表
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteUserAddress(String userId, String addressId) {
        UserAddress address = new UserAddress();
        // 1. 设置用户Id
        address.setId(addressId);
        // 2. 设置地址Id
        address.setUserId(userId);
        // 3. 删除指定Id的地址信息
        userAddressMapper.delete(address);
    }

    // 更新默认用户默认地址信息
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUserAddressToBeDefault(String userId, String addressId) {
        // 1. 查找默认地址，设置为不默认
        UserAddress queryAddress = new UserAddress();
        // 2. 设置用户Id
        queryAddress.setUserId(userId);
        // 3. 设置为默认地址
        queryAddress.setIsDefault(YesOrNo.YES.type);
        // 4. 获取到默认地址列表
        List<UserAddress> list = userAddressMapper.select(queryAddress);
        // 5. 遍历默认地址列表
        for (UserAddress ua : list) {
            // 5.1 将默认地址的默认字段值修改为NO-0
            ua.setIsDefault(YesOrNo.NO.type);
            // 5.2 为默认地址更新默认字段值
            userAddressMapper.updateByPrimaryKeySelective(ua);
        }

        // 6. 根据地址id修改为默认的地址
        UserAddress defaultAddress = new UserAddress();
        // 7. 设置默认地址Id
        defaultAddress.setId(addressId);
        // 8. 设置默认地址对应的用户Id
        defaultAddress.setUserId(userId);
        // 9. 设置默认地址对应的默认字段
        defaultAddress.setIsDefault(YesOrNo.YES.type);
        // 10. 更新默认地址信息
        userAddressMapper.updateByPrimaryKeySelective(defaultAddress);
    }

    // 根据用户id和地址id，查询具体的(一条)用户地址对象信息
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public UserAddress queryUserAddres(String userId, String addressId) {
        // 1. 创建地址信息对象
        UserAddress singleAddress = new UserAddress();
        // 2. 为地址信息对象设置地址Id
        singleAddress.setId(addressId);
        // 3. 为地址信息对象设置用户Id
        singleAddress.setUserId(userId);
        // 4. 返回查询对应地址对象
        return userAddressMapper.selectOne(singleAddress);
    }
}
