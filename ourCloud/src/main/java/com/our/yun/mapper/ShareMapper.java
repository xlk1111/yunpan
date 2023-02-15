package com.our.yun.mapper;

import com.our.yun.entity.Share;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
@Mapper
@Repository
public interface ShareMapper {

    @Select("select * from share where shareUrl = #{shareUrl} and status = #{status}")
    List<Share> findShare(Share share) throws Exception;

    @Select("select * from share where shareUser = #{username, jdbcType=VARCHAR} and status = #{status, jdbcType=TINYINT}")
    List<Share> findShareByName(@Param("username") String username, @Param("status") int status) throws Exception;

    @Insert("insert into share(shareUrl, path, shareUser, shareTime) values(#{shareUrl},#{path},#{shareUser},#{shareTime})")
    void shareFile(Share share) throws Exception;

    @Update("update share set status = #{status} where shareUrl = #{url} and path=#{filePath}")
    void cancelShare(@Param("url") String url, @Param("filePath") String filePath, @Param("status") int status) throws Exception;


}
