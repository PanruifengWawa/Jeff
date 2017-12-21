package cn.jiang.garden.controller;


import cn.jiang.garden.model.TUserEntity;
import cn.jiang.garden.service.UserService;
import cn.jiang.garden.utils.DataWrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value="api/user")
public class UserController {
    @Autowired
    UserService userService;

    //登录 api/user/login?userName=x&password=x     [GET]
    @RequestMapping(value="login",method= RequestMethod.GET)
    @ResponseBody
    public DataWrapper<Void> login(
            @RequestParam(value="userName",required=true) String userName,
            @RequestParam(value = "password",required=true) String password
    ) {
        return userService.login(userName,password);
    }

    //管理员添加用户 api/user/register?token=x    [GET]
    /*
     * userName String 用户名;
     * password	String	密码
     * applicationId	Long	职位id
     * type		Integer		0管理员，1-员工，这里默认为1，不用填
     * registerDate	Date	注册日期，不用填
     * */
    @RequestMapping(value="register",method= RequestMethod.POST)
    @ResponseBody
    public DataWrapper<Void> register(
            @ModelAttribute TUserEntity userEntity,
            @RequestParam(value = "token",required=true) String token
    ) {
        return userService.register(userEntity,token);
    }

}
