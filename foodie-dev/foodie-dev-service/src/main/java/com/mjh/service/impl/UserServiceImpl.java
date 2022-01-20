package com.mjh.service.impl;

import com.mjh.enums.Sex;
import com.mjh.mapper.UsersMapper;
import com.mjh.pojo.Users;
import com.mjh.pojo.bo.UserBO;
import com.mjh.service.UserService;
import com.mjh.utils.DateUtil;
import com.mjh.utils.MD5Utils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;

/**
 * @PackageName: com.mjh.service.impl
 * @ClassName: UserServiceImpl
 * @Author: majiahuan
 * @Date: 2020/10/18 17:48
 * @Description:
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    public UsersMapper usersMapper;

    @Autowired
    private Sid sid; // 生成分布式id，全局唯一性

    //用户默认图像地址
    private static final String USER_FACE_IMAGE = "http://122.152.205.72:88/group1/M00/00/05/CpoxxFw_8_qAIlFXAAAcIhVPdSg994.png";

    //校验用户名是否存在
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean queryUsernameIsExist(String username) {
        //1. 创建用户表在mybatis中的example实例对象
        Example userExample = new Example(Users.class);
        //2. 创建实例对象的条件集合
        Example.Criteria userExampleCriteria = userExample.createCriteria();
        //3. 选取对应方法进行操作
        userExampleCriteria.andEqualTo("username", username);
        //4. 调用数据层的通用Mapper方法
        Users user = usersMapper.selectOneByExample(userExample);
        //5. 返回查询状态
        return user != null;
    }

    //根据前端提交的表单信息创建用户
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users createUserByForm(UserBO userBO) {
        //使用sid生成用户id
        String userId = sid.nextShort();
        //1. 创建用户对象用以封装表单参数
        Users user = new Users();

        //2. 使用表单参数对用户进行封装
        user.setId(userId);
        user.setUsername(userBO.getUsername());
        try {
            user.setPassword(MD5Utils.getMD5Str(userBO.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //2.3 设置用户昵称，默认和用户相同
        user.setNickname(userBO.getUsername());
        //2.4 设置默认头像
        user.setFace(USER_FACE_IMAGE);
        //2.5 设置默认生日
        user.setBirthday(DateUtil.stringToDate("1990-01-01"));
        //2.6 设置性别
        user.setSex(Sex.secret.type);
        //2.7 设置创建时间和更新时间
        user.setCreatedTime(new Date());
        user.setUpdatedTime(new Date());

        //3. 使用数据访问层的添加方法保存创建的用户数据
        usersMapper.insert(user);
        return user;
    }

    // 用于登录
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUserForLogin(String username, String password) {
        //1. 创建用户表在mybatis中的example实例对象
        Example userExample = new Example(Users.class);
        //2. 创建实例对象的条件集合
        Example.Criteria userExampleCriteria = userExample.createCriteria();
        //3. 选取对应方法进行操作
        userExampleCriteria.andEqualTo("username", username);
        userExampleCriteria.andEqualTo("password", password);
        //4. 调用数据层的通用Mapper方法
        Users user = usersMapper.selectOneByExample(userExample);
        return user;
    }
}
