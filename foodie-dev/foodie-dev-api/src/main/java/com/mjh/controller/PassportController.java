package com.mjh.controller;

import com.mjh.pojo.Users;
import com.mjh.pojo.bo.UserBO;
import com.mjh.service.UserService;
import com.mjh.utils.CookieUtils;
import com.mjh.utils.JsonUtils;
import com.mjh.utils.MD5Utils;
import com.mjh.utils.MJHJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @PackageName: com.mjh.controller
 * @ClassName: HelloController
 * @Author: majiahuan
 * @Date: 2020/10/6 21:23
 * @Description:
 */

@Api(value = "注册登录", tags = {"用于注册登录的相关接口"}) //用于生成api文档说明
@RestController//声明当前类为前端访问控制层(即所有请求都必须经过该类)
@RequestMapping("passport")//设置当前类资源的访问uri标识
public class PassportController {

    //注入service业务逻辑层
    @Autowired
    private UserService userService;

    // 用于为当前方法生成API文档注释
    @ApiOperation(value = "用户名是否存在", notes = "用户名是否存在", httpMethod = "GET")
    @GetMapping("/usernameIsExist")
    public MJHJSONResult usernameIsExist(@RequestParam/*表示该参数是一个请求类型的参数*/ String username) {

        //1. 如果用户名为空或者空串直接返回错误码
        if (StringUtils.isBlank(username)) {
            return MJHJSONResult.errorMsg("用户名不能为空");
        }
        //2. 判断用户名是否存在
        boolean flag = userService.queryUsernameIsExist(username);
        if (flag) {
            return MJHJSONResult.errorMsg("用户名已经存在，请重新输入");
        }
        //3. 请求成功，用户名没有重复
        return MJHJSONResult.ok();
    }

    // 用于为当前方法生成API文档注释
    @ApiOperation(value = "用户注册", notes = "用户注册", httpMethod = "POST")
    @PostMapping("/register")
    public MJHJSONResult register(@RequestBody/*将前端传递的json数据接收映射为Java类*/ UserBO userBO,
                                HttpServletRequest request,
                                HttpServletResponse response) {
        // 1. 获取用户信息
        String username = userBO.getUsername();
        String password = userBO.getPassword();
        String confirmPassword = userBO.getConfirmPassword();
        // 2. 用户名密码进行非空校验
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password) || StringUtils.isBlank(confirmPassword)) {
            return MJHJSONResult.errorMsg("用户名或密码不能为空");
        }
        // 3. 检查用户名是否存在
        boolean flag = userService.queryUsernameIsExist(username);
        if (flag) {
            return MJHJSONResult.errorMsg("用户名已经存在，请重新输入");
        }
        // 4. 密码长度校验
        if (password.length() < 6) {
            return MJHJSONResult.errorMsg("密码长度不能少于6位");
        }
        // 5. 密码一致性校验
        if (!password.equals(confirmPassword)) {
            return MJHJSONResult.errorMsg("两次输入的密码不一致");
        }
        // 5. 实现注册
        Users user = userService.createUserByForm(userBO);

        // 8. 将用户信息写入cookie中，用于数据回显
        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(user),true);

        // todo 生成用户token，存入redis会话
        // todo 同步用户购物车数据

        return MJHJSONResult.ok();
    }

    // 用于为当前方法生成API文档注释
    @ApiOperation(value = "用户登录", notes = "用户登录", httpMethod = "POST")
    @PostMapping("/login")
    public MJHJSONResult login(@RequestBody/*将前端传递的json数据接收映射为Java类*/ UserBO userBO,
                               HttpServletRequest request,
                               HttpServletResponse response
    ) throws Exception {
        //1. 获取用户信息
        String username = userBO.getUsername();
        String password = userBO.getPassword();
        //2. 用户名密码进行非空校验
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return MJHJSONResult.errorMsg("用户名或密码不能为空");
        }
        //4. 对前端密码进行加密，因为数据库密码是密文形式，所以加密后进行比对
        String md5Password = MD5Utils.getMD5Str(password);
        //5. 实现登录登录校验
        Users user = userService.queryUserForLogin(username, md5Password);
        //6. 进行返回值校验
        if (user == null) {
            return MJHJSONResult.errorMsg("用户不存在或者密码输入错误");
        }

        // 7. 将用户关键信息置为null，以免cookie劫持；真实场景要进行加密
        user = setUserProperty(user);
        // 8. 将用户信息写入cookie中，用于数据回显
        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(user),true);

        // todo 生成用户token，存入redis会话
        // todo 同步用户购物车数据

        return MJHJSONResult.ok(user);
    }

    // 退出登录
    @ApiOperation(value = "用户退出登录", notes = "用户退出登录", httpMethod = "POST")
    @PostMapping("/logout")
    public MJHJSONResult logout(@RequestParam String userId,
                                HttpServletRequest request,
                                HttpServletResponse response){
        // 清除用户信息的相关Cookie
        CookieUtils.deleteCookie(request,response,"user");

        // todo 用户退出登录需要清空购物车
        // todo 分布式会话中需要清除用户数据

        return MJHJSONResult.ok();
    }

    // 将用户关键属性置为null
    private Users setUserProperty(Users user) {
        user.setPassword(null);
        user.setRealname(null);
        user.setMobile(null);
        user.setEmail(null);
        user.setBirthday(null);
        user.setCreatedTime(null);
        user.setUpdatedTime(null);
        return user;
    }
}
