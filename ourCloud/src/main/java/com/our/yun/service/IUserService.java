package com.our.yun.service;

import com.our.yun.entity.User;

public interface IUserService {

    User findUser(User user);

    Boolean addUser(User user);

    User findUserByUsername(String username);

    String getCountSize(String username);
    void reSize(String username, String formatSize);
}
