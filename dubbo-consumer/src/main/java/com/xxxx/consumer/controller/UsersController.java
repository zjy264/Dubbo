package com.xxxx.consumer.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xxxx.api.entity.Person;
import com.xxxx.api.entity.Users;
import com.xxxx.api.entity.dto.LoginToken;
import com.xxxx.api.mapper.GithubMapper;
import com.xxxx.api.mapper.PersonMapper;
import com.xxxx.api.mapper.QqMapper;
import com.xxxx.api.mapper.UsersMapper;
import com.xxxx.api.service.*;
import com.xxxx.api.uilt.Result;
import com.xxxx.api.uilt.SendMessageUtil;
import com.xxxx.api.uilt.VerifyCodeUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author write by zjy
 * @since 2021-04-22
 */
@RestController
@RequestMapping("/users")
public class UsersController {
    @Reference(interfaceClass = UsersService.class)
    private UsersService usersService;
    @Autowired(required = false)
    private UsersMapper usersMapper;
    @Reference(interfaceClass = UserTokenService.class)
    private static UserTokenService userTokenService;

    @Autowired(required = false)
    private QqMapper qqUserMapper;
    @Reference(interfaceClass = QqService.class)
    private QqService qqUserService;
    @Reference(interfaceClass = PersonService.class)
    private PersonService personService;
    @Autowired(required = false)
    private PersonMapper personMapper;
    @Reference(interfaceClass = GithubService.class)
    private GithubService githubUserService;
    @Autowired(required = false)
    private GithubMapper githubUserMapper;
    @Autowired(required = false)
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired(required = false)
    private RabbitTemplate rabbitTemplate;
//    生成验证码
    @RequestMapping(value = "/getImage**", method = RequestMethod.GET)
    public Map authImage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            response.setContentType("image/jpeg");
            // 生成随机字串
            String verifyCode = VerifyCodeUtils.generateVerifyCode(4);
            // 生成图片
            int w = 120, h = 40;
            OutputStream out = response.getOutputStream();
            Object[] objs = VerifyCodeUtils.outputImage(w, h, out, verifyCode);
            BufferedImage image = (BufferedImage) objs[1];
            //转base64
            BASE64Encoder encoder = new BASE64Encoder();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();//io流
            ImageIO.write(image, "png", baos);//写入流中
            byte[] bytes = baos.toByteArray();//转换成字节
            String png_base64 = encoder.encodeBuffer(bytes).trim();//转换成base64串
            //删除 \r\n
            png_base64 = png_base64.replaceAll("\n", "").replaceAll("\r", "");

            Map map = new HashMap<>();
            map.put("base64", "data:image/png;base64," + png_base64);
            map.put("validateCode", verifyCode);
            return map;

        } catch (Exception e) {

        }
        return null;
    }
    //
    @PostMapping("/checkUsername")
    public Result checkUsername(@RequestBody Map<String, String> username) {
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username.get("username"));
        Result result = new Result();
        if (usersMapper.selectList(queryWrapper).size() > 0) {
            result.setData("用户名已存在");
            result.setMsg("注册失败");
            result.setCode("500");
        } else {
            result.setData("用户名可用");
            result.setCode("200");
        }
        return result;
    }

    @PostMapping("/register")
    public Result registerUser(@RequestBody Map<String, String> registerUser) {
        Result result = new Result();
        Users user = new Users();
        user.setUsername(registerUser.get("username"));
        user.setNike(registerUser.get("nike"));
        // 注册的时候把密码加密一下
        user.setPassword(bCryptPasswordEncoder.encode(registerUser.get("password")));
        user.setRole("ROLE_USER");
        Boolean save2 = usersService.save(user);
        Person person = new Person();
        person.setUsername(registerUser.get("username"));
        person.setNike(registerUser.get("nike"));
        personService.save(person);
        if (save2) {
            result.setData("注册成功");
            result.setMsg("注册成功");
            result.setCode("200");
        } else {
            result.setData("网络延迟请重试");
            result.setMsg("注册失败功");
            result.setCode("400");
        }
        return result;
    }

    @PostMapping("/phone")
    public Result sendPhone(@RequestBody Map<String, String> phone) {
        String yzm=VerifyCodeUtils.generatePhoneVerifyCode(6);
        Result result=new Result();
        result.setCode("200");
        result.setData(yzm);
        phone.put("yzm",yzm);
        phone.put("time","5");
        rabbitTemplate.convertAndSend("topicExchange","msg.msg",phone);
        return result;
    }
//    token操作
    public static void saveToken(LoginToken loginToken){
        userTokenService.saveToken(loginToken);
    }
    public static LoginToken getToken(String username){
        return userTokenService.getToken(username);
    }
}
