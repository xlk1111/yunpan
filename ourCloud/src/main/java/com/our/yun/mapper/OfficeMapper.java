package com.our.yun.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface OfficeMapper {
    /**
     * 增加百度云解码Id
     *
     * @param officeId  解码id
     * @param officeMd5 文件md5值
     * @throws Exception
     */
    @Insert("insert into office(officeid, officeMd5) values(#{officeId}, #{officeMd5})")
    void addOffice(@Param("officeId") String officeId, @Param("officeMd5") String officeMd5) throws Exception;

    /**
     * 查找文件解析id
     *
     * @param officeMd5 文件md5
     * @return
     * @throws Exception
     */
    @Select("select officeid from office where officeMd5 = #{officeMd5}")
    String getOfficeId(String officeMd5) throws Exception;

}
