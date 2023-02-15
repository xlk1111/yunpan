package com.our.yun.mapper;


import com.our.yun.entity.RecycleFile;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Mapper
@Repository
public interface FileMapper {

    @Insert("insert into File(userName, filePath, recyclePath,deleteTime) values(#{userName}, #{filePath}, #{recyclePath},#{deleteTime})")
    void insertFiles(@Param("userName") String userName, @Param("filePath") String filePath, @Param("recyclePath") String recyclePath, @Param("deleteTime")Date deleteTime) throws Exception;

    @Delete("delete from File where userName = #{userName}")
    void deleteFiles(@Param("userName") String userName) throws Exception;

    @Delete("delete from File where fileId = #{fileId} and userName = #{userName}")
    void deleteFile(@Param("fileId") int fileId, @Param("userName") String userName) throws Exception;

    @Select("select * from File where userName = #{userName}")
    List<RecycleFile> selectFiles(@Param("userName") String userName) throws Exception;

    @Select("select * from File where fileId = #{fileId}")
    RecycleFile selectFile(@Param("fileId") int fileId) throws Exception;

}
