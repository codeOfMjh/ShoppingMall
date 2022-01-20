package com.mjh.service;

import com.mjh.pojo.Users;
import com.mjh.pojo.bo.UserBO;

public interface UserService {

    /**
     * @Description //检查用户名是否存在
     * @Date 17:47 2020/10/18
     * @Param [name]
     * @return boolean
     **/
    public boolean queryUsernameIsExist(String username);

    /**
     * @Description //根据前端表单信息创建用户
     * @Date 21:18 2020/10/20
     * @Param [userBO]
     * @return com.mjh.pojo.Users
     **/
    public Users createUserByForm(UserBO userBO);

    /**
     * @Description //检索用户名和密码是否匹配，用于登录
     * @Date 22:19 2020/10/25
     * @Param [username, password]
     * @return com.mjh.pojo.Users
     **/
    public Users queryUserForLogin(String username,String password);
}
