package com.our.yun.service.impl;

import com.our.yun.entity.User;
import com.our.yun.mapper.UserMapper;
import com.our.yun.service.IUserService;
import com.our.yun.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User findUser(User user) {
        try {
            user.setPassword(UserUtils.MD5(user.getPassword()));
            User exsitUser = userMapper.findUser(user);
            return exsitUser;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Boolean addUser(User user) {
        try {
            //用户名不能重复
            if(userMapper.findUserByUsername(user.getUsername())==null){
                user.setPassword(UserUtils.MD5(user.getPassword()));
                userMapper.addUser(user);
                return true;
            }else return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public User findUserByUsername(String username) {
        return null;
    }

    @Override
    public String getCountSize(String username) {
        String countSize = null;
        try {
            countSize = userMapper.getCountSize(username);
        } catch (Exception e) {
            e.printStackTrace();
            return countSize;
        }
        return countSize;
    }

    @Override
    public void reSize(String username, String formatSize) {

    }


}
