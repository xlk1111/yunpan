package com.our.yun.mapper;

import com.our.yun.entity.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {
    @Insert("INSERT INTO user(username, password) VALUES (#{username}, #{password})")
    Boolean addUser(User user);

    @Select("SELECT * FROM user WHERE username = #{username}")
    User findUserByUsername(String username);

    @Select("select * from user where username = #{username} and password = #{password}")
    User findUser(User user);

    @Select("select countSize from user where username = #{username}")
    String getCountSize(String username);

    @Update("update user set countSize = #{formatSize} where username = #{username}")
    void reSize(@Param("username") String username, @Param("formatSize") String formatSize);
}
