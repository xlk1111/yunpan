package com.our.yun.service;

import com.our.yun.entity.FileCustom;
import com.our.yun.entity.RecycleFile;
import com.our.yun.entity.SummaryFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;

public interface FileService {

    //上传文件
    void uploadFilePath(HttpServletRequest request, MultipartFile[] files, String currentPath) throws Exception;

    //删除文件压缩包
    void deleteDownPackage(File downloadFile);

    //下载文件打包
    File downPackage(HttpServletRequest request, String currentPath, String[] fileNames, String username) throws Exception;

    //获取文件根路径
    String getRootPath(HttpServletRequest request);

    //获取回收站根路径
    public String getRecyclePath(HttpServletRequest request);

    //获取文件路径
    String getFileName(HttpServletRequest request, String fileName);

    //根据用户名获取文件路径
    String getFileName(HttpServletRequest request, String fileName, String username);

    //获取路径下的文件类别
    List<FileCustom> listFile(String realPath);

    //查找文件
    List<FileCustom> searchFile(HttpServletRequest request, String currentPath, String reg, String regType);

    //移动文件列表
    SummaryFile summarylistFile(String realPath, int number);

    //新建文件夹
    boolean addDirectory(HttpServletRequest request, String currentPath, String directoryName);

    //回收站显示所有删除文件
    List<RecycleFile> recycleFiles(HttpServletRequest request) throws Exception;


    //删除回收站的文件
    @Transactional
    void delRecycle(HttpServletRequest request, int[] fileId) throws Exception;


    //依次遍历recycle下各个文件，并删除
    @Transactional
    void delAllRecycle(HttpServletRequest request) throws Exception;

    //删除文件
    @Transactional
    void delDirectory(HttpServletRequest request, String currentPath, String[] directoryName) throws Exception;

    //还原文件
    @Transactional
    void revertDirectory(HttpServletRequest request, int[] fileId) throws Exception;


    //重命名文件
    boolean renameDirectory(HttpServletRequest request, String currentPath, String srcName, String destName);


    //新用户新建文件
    void addNewNameSpace(HttpServletRequest request, String namespace);


    //复制文件
    void copyDirectory(HttpServletRequest request, String currentPath, String[] directoryName,
                       String targetdirectorypath) throws Exception;

    //移动文件
    void moveDirectory(HttpServletRequest request, String currentPath, String[] directoryName,
                       String targetdirectorypath) throws Exception;

    //响应文件流
    void respFile(HttpServletResponse response, HttpServletRequest request, String currentPath, String fileName,
                  String type) throws IOException;


    //打开文档文件
    String openOffice(HttpServletRequest request, String currentPath, String fileName) throws Exception;

}
