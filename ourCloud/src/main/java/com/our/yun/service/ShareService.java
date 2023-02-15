package com.our.yun.service;

import com.our.yun.entity.ShareFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ShareService {

    //查找分享
    List<ShareFile> findShare(HttpServletRequest request, String shareUrl) throws Exception;


    //查找分享文件
    List<ShareFile> findShareByName(HttpServletRequest request, int status) throws Exception;


    //分享文件
    String shareFile(HttpServletRequest request, String currentPath, String[] shareFile) throws Exception;

    //取消分享
    String cancelShare(String url, String filePath, int status) throws Exception;




}
