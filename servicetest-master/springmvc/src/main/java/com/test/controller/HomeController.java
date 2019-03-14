package com.test.controller;

import com.test.dao.UserDao;
import com.test.po.Account;
import com.test.po.ResultMoudel;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.logging.Logger;

@Controller
@RequestMapping("/home")
public class HomeController {
    //日志器
    private static final Logger logger = Logger.getLogger("HomeController");
    @Resource(name = "userDao")
    private UserDao userDao;
    //映射一个action
    @RequestMapping("/index.do")
    public String index(){
        logger.info("哈哈哈");
        return "index";
    }
    @RequestMapping(value = "/json.do",method = {RequestMethod.POST})
    @ResponseBody
    public Account json(@RequestParam("username") String username, @RequestParam("pwd") String pwd){
        Account account = new Account();
        account.setUsername(username);
        account.setPwd(pwd);
        return account;
    }
    @RequestMapping(value = "/add.do")
    @ResponseBody
    public String addUser(String username,String pwd){
        username = "aasdd";
        pwd = "ssdddds";
        Account account = new Account();
        account.setUsername(username);
        account.setPwd(pwd);
        userDao.addUser(account);
        return "all";
    }

    @RequestMapping(value = "/getuser.do")
    @ResponseBody
    public List<Account> getUser(){
        List<Account> accounts = userDao.queryAll();
        System.out.println(StringUtils.join(accounts.toArray(),";"));
        return accounts;
    }
    @RequestMapping(value = "/login.do")
    @ResponseBody
    public ResultMoudel login(String username,String pwd){
        ResultMoudel objectResultMoudel = new ResultMoudel<>();
        List<Account> accounts = userDao.getAccountByUserName(username);
        if(accounts.size()>0){
            if(!pwd.equals(accounts.get(0).getPwd())){
                objectResultMoudel.setResultFlag("error");
                objectResultMoudel.setBody("用户名或密码错误");
                return objectResultMoudel;
            }else {
                objectResultMoudel.setResultFlag("success");
                objectResultMoudel.setBody("用户名");
                SecurityUtils.getSecurityManager().logout(SecurityUtils.getSubject());
                // 登录后存放进shiro token
                UsernamePasswordToken token = new UsernamePasswordToken(username, pwd);
                Subject subject = SecurityUtils.getSubject();
                subject.login(token);
                return objectResultMoudel;
            }
        }
        return objectResultMoudel;
    }
}
