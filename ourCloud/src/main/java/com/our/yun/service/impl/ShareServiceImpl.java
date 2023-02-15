package com.our.yun.service.impl;

import com.our.yun.entity.Share;
import com.our.yun.entity.ShareFile;
import com.our.yun.entity.User;
import com.our.yun.mapper.ShareMapper;
import com.our.yun.service.ShareService;
import com.our.yun.utils.FileUtils;
import com.our.yun.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ShareServiceImpl implements ShareService {


    @Autowired
    private ShareMapper shareMapper;

    /**
     * 查找分享
     *
     * @param request
     * @param shareUrl 分享url
     * @return
     * @throws Exception
     */
    public List<ShareFile> findShare(HttpServletRequest request, String shareUrl) throws Exception {
        Share share = new Share();
        share.setShareUrl(shareUrl);
        share.setStatus(Share.PUBLIC);
        List<Share> shares = shareMapper.findShare(share);
        return getShareFile(request, shares);
    }

    /**
     * 查找分享文件
     *
     * @param request
     * @param status  分享文件状态
     * @return
     * @throws Exception
     */
    public List<ShareFile> findShareByName(HttpServletRequest request, int status) throws Exception {
        List<Share> shares = shareMapper.findShareByName(UserUtils.getUsername(request), status);
        return getShareFile(request, shares);
    }

    /**
     * 获取分享文件列表
     *
     * @param request
     * @param shares  分享文件
     * @return
     */
    private List<ShareFile> getShareFile(HttpServletRequest request, List<Share> shares) {
        List<ShareFile> files = null;
        if (shares != null) {
            files = new ArrayList<>();
            String rootPath = request.getSession().getServletContext().getRealPath("/") + FileServiceImpl.PREFIX;
            for (Share share : shares) {
                File file = new File(rootPath + share.getShareUser(), share.getPath());
                ShareFile shareFile = new ShareFile();
                shareFile.setFileType(FileUtils.getFileType(file));
                shareFile.setFileName(file.getName());
                shareFile.setFileSize(file.isFile() ? FileUtils.getDataSize(file.length()) : "-");
                shareFile.setLastTime(FileUtils.formatTime(share.getShareTime()));
                shareFile.setShareUser(share.getShareUser());
                shareFile.setUrl(share.getShareUrl());
                shareFile.setFilePath(share.getPath());
                files.add(shareFile);
            }
        }
        return files;
    }

    /**
     * 分享文件
     *
     * @param request
     * @param currentPath
     * @param shareFile
     * @return
     * @throws Exception
     */
    public String shareFile(HttpServletRequest request, String currentPath, String[] shareFile) throws Exception {
        String username = (String) request.getSession().getAttribute(User.NAMESPACE);
        String shareUrl = FileUtils.getUrl8();
        for (String file : shareFile) {
            Share share = new Share();
            share.setPath(currentPath + File.separator + file);
            share.setShareUser(username);
            share.setShareUrl(shareUrl);
            share.setShareTime(new Date());
            shareMapper.shareFile(share);
        }
        return shareUrl;
    }

    /**
     * 取消分享
     *
     * @param url      分享url
     * @param filePath 分享路径
     * @param status   分享状态
     * @return
     * @throws Exception
     */
    public String cancelShare(String url, String filePath, int status) throws Exception {
        if (Share.CANCEL == status) {
            shareMapper.cancelShare(url, filePath, Share.DELETE);
            return "删除成功";
        } else {
            shareMapper.cancelShare(url, filePath, Share.CANCEL);
            return "取消成功";
        }
    }

}
