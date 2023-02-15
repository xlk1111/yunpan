package com.our.yun.controller;


import com.our.yun.entity.User;
import com.our.yun.service.FileService;
import com.our.yun.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private FileService fileService;


    @RequestMapping("/login")
    public String login(HttpServletRequest request, User user) {
        User exsitUser = userService.findUser(user);
        if (exsitUser != null) {
            HttpSession session = request.getSession();
            session.setAttribute(User.NAMESPACE, exsitUser.getUsername());
            session.setAttribute("totalSize", exsitUser.getTotalSize());
            return "redirect:/index.action";
        } else {
            request.setAttribute("msg", "用户名或密码错误");
            return "login";
        }
    }

    @RequestMapping("/register")
    public String register(HttpServletRequest request, User user) {
        //System.out.println(user.getUsername()+"-------"+user.getPassword());
        if (user.getUsername() == null || user.getPassword() == null || user.getUsername().equals("") || user.getPassword().equals("")) {
            request.setAttribute("msg", "请输入用户名和密码");
            return "register";
        } else {
            boolean isSuccess = userService.addUser(user);
            if (isSuccess) {
                fileService.addNewNameSpace(request, user.getUsername());
                request.setAttribute("msg", "用户名已存在");
                return "login";
            } else {
                request.setAttribute("msg", "注册失败");
                return "register";
            }
        }
    }

    @RequestMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:/user/login.action";
    }

}
