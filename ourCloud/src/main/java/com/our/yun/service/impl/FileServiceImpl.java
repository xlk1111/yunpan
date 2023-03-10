package com.our.yun.service.impl;


import com.our.yun.entity.FileCustom;
import com.our.yun.entity.RecycleFile;
import com.our.yun.entity.SummaryFile;
import com.our.yun.entity.User;
import com.our.yun.mapper.FileMapper;
import com.our.yun.mapper.OfficeMapper;
import com.our.yun.mapper.UserMapper;
import com.our.yun.service.FileService;
import com.our.yun.utils.FileUtils;
import com.our.yun.utils.UserUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


@Service
public class FileServiceImpl implements FileService {
    /**
     * 文件相对前缀
     */
    public static final String PREFIX = "WEB-INF" + File.separator + "file" + File.separator;
    /**
     * 新用户注册默认文件夹
     */
    public static final String[] DEFAULT_DIRECTORY = {"video", "music", "source", "image", User.RECYCLE};

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private FileMapper fileMapper;

    @Autowired
    private OfficeMapper officeMapper;


    /**
     * 上传文件
     *
     * @param request
     * @param files       文件
     * @param currentPath 当前路径
     * @throws Exception
     */
    public void uploadFilePath(HttpServletRequest request, MultipartFile[] files, String currentPath) throws Exception {
        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
            String filePath = getFileName(request, currentPath);
            File distFile = new File(filePath, fileName);

//            File file1 = new File(filePath);
//            File[] files1 = file1.listFiles();
//            for (int i = 0; i < files1.length; i++) {
//
//                System.out.println("----------------------------");
//                boolean b = org.apache.commons.io.FileUtils.contentEquals(distFile,files1[i]);
//                System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
//
//            }

            if (!distFile.exists()) {
                file.transferTo(distFile);
                if ("office".equals(FileUtils.getFileType(distFile))) {
                    try {
                        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
                        String documentId = FileUtils.getDocClient().createDocument(distFile, fileName, suffix)
                                .getDocumentId();
                        officeMapper.addOffice(documentId, FileUtils.MD5(distFile));
                    } catch (Exception e) {
                    }
                }
            }
        }
        reSize(request);
    }


    /**
     * 删除压缩文件包
     *
     * @param downloadFile
     */
    public void deleteDownPackage(File downloadFile) {
        if (downloadFile.getName().endsWith(".zip")) {
            downloadFile.delete();
        }
    }

    /**
     * 下载文件打包
     *
     * @param request
     * @param currentPath 当前路径
     * @param fileNames   文件名
     * @param username    用户名
     * @return 打包的文件对象
     * @throws Exception
     */
    public File downPackage(HttpServletRequest request, String currentPath, String[] fileNames, String username)
            throws Exception {
        File downloadFile = null;
        if (currentPath == null) {
            currentPath = "";
        }
        if (fileNames.length == 1) {
            downloadFile = new File(getFileName(request, currentPath, username), fileNames[0]);
            if (downloadFile.isFile()) {
                return downloadFile;
            }
        }
        String[] sourcePath = new String[fileNames.length];
        for (int i = 0; i < fileNames.length; i++) {
            sourcePath[i] = getFileName(request, currentPath, username) + File.separator + fileNames[i];
        }
        String packageZipName = packageZip(sourcePath);
        downloadFile = new File(packageZipName);
        return downloadFile;
    }

    /**
     * 压缩文件
     *
     * @param sourcePath
     * @return
     * @throws Exception
     */
    private String packageZip(String[] sourcePath) throws Exception {
        String zipName = sourcePath[0] + (sourcePath.length == 1 ? "" : "等" + sourcePath.length + "个文件") + ".zip";
        ZipOutputStream zos = null;             //例：我和我的祖国.mp3等2个文件.zip
        try {
            zos = new ZipOutputStream(new FileOutputStream(zipName));
            for (String string : sourcePath) {
                writeZos(new File(string), "", zos);
            }
        } finally {
            if (zos != null) {
                zos.close();
            }
        }
        return zipName;
    }

    /**
     * 写入文件到压缩包
     *
     * @param file
     * @param basePath
     * @param zos
     * @throws IOException
     */
    private void writeZos(File file, String basePath, ZipOutputStream zos) throws IOException {
        if (!file.exists()) {
            throw new FileNotFoundException();
        }
        if (file.isDirectory()) {
            File[] listFiles = file.listFiles();
            if (listFiles.length != 0) {
                for (File childFile : listFiles) {
                    writeZos(childFile, basePath + file.getName() + File.separator, zos);
                }
            }
        } else {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            ZipEntry entry = new ZipEntry(basePath + file.getName());
            zos.putNextEntry(entry);
            int count = 0;
            byte data[] = new byte[1024];
            while ((count = bis.read(data)) != -1) {
                zos.write(data, 0, count);
            }
            bis.close();
        }
    }

    /**
     * 获取文件根路径（"项目路径\src\main\webapp\WEB-INF\file\"）
     *
     * @param request
     * @return
     */
    public String getRootPath(HttpServletRequest request) {
        String rootPath = request.getSession().getServletContext().getRealPath("/") + PREFIX;
        return rootPath;
    }

    /**
     * 获取回收站跟路径
     *
     * @param request
     * @return
     */
    public String getRecyclePath(HttpServletRequest request) {
        return getFileName(request, User.RECYCLE);
    }

    /**
     * 获取文件路径
     *
     * @param request
     * @param fileName
     * @return
     */
    public String getFileName(HttpServletRequest request, String fileName) {
        ////////////////////////////////////////
//        fileName= fileName.replace("\\", "//");
        //////////////////////////////////////
        if (fileName == null) {
            fileName = "";
        }
        String username = UserUtils.getUsername(request);

        String filePath1 = getRootPath(request) + username + File.separator + fileName;

        return filePath1;

    }

    /**
     * 根据用户名获取文件路径
     *
     * @param request
     * @param fileName
     * @param username
     * @return
     */
    public String getFileName(HttpServletRequest request, String fileName, String username) {
        if (username == null) {
            return getFileName(request, fileName);
        }
        if (fileName == null) {
            fileName = "";
        }
        return getRootPath(request) + username + File.separator + fileName;
    }

    /**
     * 获取路径下的文件类别
     *
     * @param realPath 路径
     * @return
     */
    public List<FileCustom> listFile(String realPath) {
        //对文件操作  需要new出一个文件，代表指向该文件内存地址
        File[] files = new File(realPath).listFiles();
        List<FileCustom> lists = new ArrayList<FileCustom>();
        if (files != null) {
            for (File file : files) {
                if (!file.getName().equals(User.RECYCLE)) {
                    FileCustom custom = new FileCustom();
                    custom.setFileName(file.getName());
                    custom.setLastTime(FileUtils.formatTime(file.lastModified()));
                    /* 保存文件的删除前路径以及当前路径 */
                    // custom.setFilePath(prePath);
                    custom.setCurrentPath(realPath);
                    if (file.isDirectory()) {
                        custom.setFileSize("-");
                    } else {
                        custom.setFileSize(FileUtils.getDataSize(file.length()));
                    }
                    custom.setFileType(FileUtils.getFileType(file));
                    lists.add(custom);
                }
            }
        }
        return lists;
    }


    /**
     * 查找文件
     *
     * @param request
     * @param currentPath 当前路径
     * @param reg         文件名字
     * @param regType     文件类型
     * @return
     */
    public List<FileCustom> searchFile(HttpServletRequest request, String currentPath, String reg, String regType) {
        List<FileCustom> list = new ArrayList<>();
        matchFile(request, list, new File(getFileName(request, currentPath)), reg, regType == null ? "" : regType);
        return list;
    }

    private void matchFile(HttpServletRequest request, List<FileCustom> list, File dirFile, String reg,
                           String regType) {
        File[] listFiles = dirFile.listFiles();
        if (listFiles != null) {
            for (File file : listFiles) {
                if (file.isFile()) {
                    String suffixType = FileUtils.getFileType(file);
                    if (suffixType.equals(regType) || (reg != null && file.getName().contains(reg))) {
                        FileCustom custom = new FileCustom();
                        custom.setFileName(file.getName());
                        custom.setLastTime(FileUtils.formatTime(file.lastModified()));
                        String parentPath = file.getParent();
                        String prePath = parentPath.substring(
                                parentPath.indexOf(getFileName(request, null)) + getFileName(request, null).length());
                        custom.setCurrentPath(File.separator + prePath);
                        if (file.isDirectory()) {
                            custom.setFileSize("-");
                        } else {
                            custom.setFileSize(FileUtils.getDataSize(file.length()));
                        }
                        custom.setFileType(FileUtils.getFileType(file));
                        list.add(custom);
                    }
                } else {
                    matchFile(request, list, file, reg, regType);
                }
            }
        }
    }

    /**
     * 移动的文件列表
     *
     * @param realPath 路径
     * @param number   该路径下的文件数量
     * @return
     */
    public SummaryFile summarylistFile(String realPath, int number) {
        File file = new File(realPath);
        SummaryFile sF = new SummaryFile();
        List<SummaryFile> returnlist = new ArrayList<SummaryFile>();
        if (file.isDirectory()) {
            sF.setisFile(false);
            if (realPath.length() <= number) {
                sF.setfileName("yun盘");
                sF.setPath("");
            } else {
                String path = file.getPath();
                sF.setfileName(file.getName());
                //截取固定长度 的字符串，从number开始到value.length-number结束.
                sF.setPath(path.substring(number));
            }
            /* 设置抽象文件夹的包含文件集合 */
            for (File filex : file.listFiles()) {
                //获取当前文件的路径，构造该文件
                SummaryFile innersF = summarylistFile(filex.getPath(), number);
                if (!innersF.getisFile()) {
                    returnlist.add(innersF);
                }
            }
            sF.setListFile(returnlist);
            /* 设置抽象文件夹的包含文件夹个数 */
            sF.setListdiretory(returnlist.size());

        } else {
            sF.setisFile(true);
        }
        return sF;
    }

    /**
     * 新建文件夹
     *
     * @param request
     * @param currentPath   当前路径
     * @param directoryName 文件夹名
     * @return
     */
    public boolean addDirectory(HttpServletRequest request, String currentPath, String directoryName) {
        File file = new File(getFileName(request, currentPath), directoryName);
        return file.mkdir();
    }

    /*--回收站显示所有删除文件--*/
    public List<RecycleFile> recycleFiles(HttpServletRequest request) throws Exception {
        //将本用户所有删除文件获取
        List<RecycleFile> recycleFiles = fileMapper.selectFiles(UserUtils.getUsername(request));
        for (RecycleFile file : recycleFiles) {
            //一次实例化所有文件对象
            File f = new File(getRecyclePath(request), new File(file.getFilePath()).getName());
            //此时设置该文件名与文件最后删除的时间
            file.setFileName(f.getName());
            file.setLastTime(FileUtils.formatTime(file.getDeleteTime()));
            String dataSize = FileUtils.getDataSize(f.length());
            file.setFileSize(dataSize);
        }
        return recycleFiles;
    }

    /* 删除回收站的文件 */
    public void delRecycle(HttpServletRequest request, int[] fileId) throws Exception {
        /*for (int i = 0; i < fileId.length; i++) {
            //获取每个删除文件的id，同时获取该文件对象
            RecycleFile selectFile = fileMapper.selectFile(fileId[i]);
            //根据每个删除文件的相对路径拼接绝对路径
            File srcFile = new File(getRecyclePath(request), selectFile.getFilePath());
            //逐一删除数据库所存数据以及该文件
            fileMapper.deleteFile(fileId[i], UserUtils.getUsername(request));
            delFile(srcFile);
        }
        reSize(request);*/
        for (int i = 0; i < fileId.length; i++) {
            //获取每个删除文件的id，同时获取该文件对象
            RecycleFile selectFile = fileMapper.selectFile(fileId[i]);
            File file = new File(selectFile.getFilePath());
            //获取文件名
            String name = file.getName();
            //根据每个删除文件的相对路径拼接绝对路径
            File srcFile = new File(getRecyclePath(request), name);
//            File srcFile = new File(getRecyclePath(request), selectFile.getFilePath());
            //逐一删除数据库所存数据以及该文件
            fileMapper.deleteFile(fileId[i], UserUtils.getUsername(request));
            delFile(srcFile);
        }
        reSize(request);
    }

    /*--依次遍历recycle下各个文件，并删除--*/
    public void delAllRecycle(HttpServletRequest request) throws Exception {
        //获取回收站中的所有文件
        File file = new File(getRecyclePath(request));
        //便利文件夹下所有文件
        File[] inferiorFile = file.listFiles();
        for (File f : inferiorFile) {
            delFile(f);
        }
        //根据用户进行删除
        fileMapper.deleteFiles(UserUtils.getUsername(request));
        reSize(request);
    }

    /**
     * 删除文件
     *
     * @param request
     * @param currentPath   当前路径
     * @param directoryName 文件名
     * @throws Exception
     */
    public void delDirectory(HttpServletRequest request, String currentPath, String[] directoryName) throws Exception {
        for (String fileName : directoryName) {
            //拼接源文件的地址
            String srcPath = currentPath + File.separator + fileName;
            String recyclePath = srcPath;
            //根据源文件相对地址拼接 绝对路径
            File src = new File(getFileName(request, srcPath));//原文件地址
            File dest = new File(getRecyclePath(request));//回收站地址
            //调用commons  jar包中的moveToDirectory移动文件

            ///////////////////////////////已修改
            //获取当前用户名
            String username = UserUtils.getUsername(request);
            //当前用户回收站下的文件和文件夹
            List<RecycleFile> recycleFiles = fileMapper.selectFiles(username);

            int fileCount = 0;
            /*for (RecycleFile recycleFile : recycleFiles) {
                File filePathName = new File(getFileName(request, recycleFile.getFilePath()));
                String fileName1 = filePathName.getName();
                if (fileName1.equals(fileName)){
                    fileCount++;
                }
            }*/
            int n = recycleFiles.size();
            String fileName2 = fileName; //用来测试回收重复的文件应该用的后缀（）
            for (int i = 0; i < n; i++) {
                RecycleFile recycleFile = recycleFiles.get(i);
                File filePathName = new File(getFileName(request, recycleFile.getRecyclePath()));
                String fileName1 = filePathName.getName();
                if (fileName1.equals(fileName2)) {
                    fileCount++;
                    fileName2 = FileUtils.updateFileName(fileName, fileCount, src);
                    i = -1; //更新fileName2,重新循环比对 文件名是否重复
                }
            }
            fileName = FileUtils.updateFileName(fileName, fileCount, src);
            recyclePath = currentPath + File.separator + fileName;


            FileUtils.moveToDirectory(src, dest, true, fileName);
            fileMapper.insertFiles(UserUtils.getUsername(request), srcPath, recyclePath,new Date());

            /*--将删除文件移动到recycle目录下*/
            // moveDirectory(request,currentPath,directoryName,User.RECYCLE);
        }
        //重新计算文件大小
        reSize(request);
    }

    /* 还原文件 */
    //难点2.还原文件时不等同于移动文件到，因为还原文件需要保存多个地址，而还原只单纯保存一个地址
    //而且还原时需要判断父子文件是否都删除了，此时就需要新建立父文件，再建立子文件，而commons.io.FileUtils
    //则可以很好的解决问题
    public void revertDirectory(HttpServletRequest request, int[] fileId) throws Exception {
        for (int id : fileId) {
            //根据要还原的文件id获得文件
            RecycleFile file = fileMapper.selectFile(id);
            //获取该文件的文件名
            String fileName = new File(file.getFilePath()).getName();

            String recycleName = new File(file.getRecyclePath()).getName();
            //根据文件名获取源文件地址
            File src = new File(getRecyclePath(request), recycleName);
            //getFileName获取该文件删除时所保存的地址
            File dest = new File(getFileName(request, file.getFilePath()));
//            org.apache.commons.io.FileUtils.moveToDirectory(src, dest.getParentFile(), true);
            FileUtils.moveToDirectory(src, dest.getParentFile(), true, fileName);
            fileMapper.deleteFile(id, UserUtils.getUsername(request));
        }
    }

    /**
     * 删除文件
     *
     * @param srcFile 源文件
     * @throws Exception
     */
    private void delFile(File srcFile) throws Exception {
        /* 如果是文件，直接删除 */

        if (!srcFile.isDirectory()) {
            /* 使用map 存储删除的 文件路径，同时保存用户名 */
            srcFile.delete();
            return;
        }
        /* 如果是文件夹，再遍历 */
        File[] listFiles = srcFile.listFiles();
        for (File file : listFiles) {
            if (file.isDirectory()) {
                delFile(file);
            } else {
                if (file.exists()) {
                    file.delete();
                }
            }
        }
        if (srcFile.exists()) {
            srcFile.delete();
        }
    }

    /**
     * 重命名文件
     *
     * @param request
     * @param currentPath
     * @param srcName
     * @param destName
     * @return
     */
    public boolean renameDirectory(HttpServletRequest request, String currentPath, String srcName, String destName) {
        //根据源文件名  获取  源地址
        File file = new File(getFileName(request, currentPath), srcName);
        //同上
        File descFile = new File(getFileName(request, currentPath), destName);
        return file.renameTo(descFile);
        //如果被重命名的文件已存在，那么renameTo()不会成功
        //renameTo()成功后，原文件会被删除
    }

    /**
     * 新用户新建文件
     *
     * @param request
     * @param namespace
     */
    public void addNewNameSpace(HttpServletRequest request, String namespace) {
        String fileName = getRootPath(request);
        File file = new File(fileName, namespace);
        file.mkdir();
        for (String newFileName : DEFAULT_DIRECTORY) {
            File newFile = new File(file, newFileName);
            newFile.mkdir();
        }
    }

    /**
     * copy文件
     *
     * @param srcFile
     * @param targetFile
     * @throws IOException
     */
    private void copyfile(File srcFile, File targetFile) throws IOException {
        // TODO Auto-generated method stub
        if (!srcFile.isDirectory()) {
            /* 如果是文件，直接复制 */
            targetFile.createNewFile();
            FileInputStream src = (new FileInputStream(srcFile));
            FileOutputStream target = new FileOutputStream(targetFile);
            FileChannel in = src.getChannel();
            FileChannel out = target.getChannel();
            in.transferTo(0, in.size(), out);
            src.close();
            target.close();
        } else {
            /* 如果是文件夹，再遍历 */
            File[] listFiles = srcFile.listFiles();
            targetFile.mkdir();
            for (File file : listFiles) {
                File realtargetFile = new File(targetFile, file.getName());
                copyfile(file, realtargetFile);
            }
        }
    }

    public void copyDirectory(HttpServletRequest request, String currentPath, String[] directoryName,
                              String targetdirectorypath) throws Exception {
        // TODO Auto-generated method stub
        for (String srcName : directoryName) {
            File srcFile = new File(getFileName(request, currentPath), srcName);
            File targetFile = new File(getFileName(request, targetdirectorypath), srcName);
            /* 处理目标目录中存在同名文件或文件夹问题 */
            String srcname = srcName;
            String prefixname = "";
            String targetname = "";
            if (targetFile.exists()) {
                String[] srcnamesplit = srcname.split("\\)");
                if (srcnamesplit.length > 1) {
                    String intstring = srcnamesplit[0].substring(1);
                    Pattern pattern = Pattern.compile("[0-9]*");
                    Matcher isNum = pattern.matcher(intstring);
                    if (isNum.matches()) {
                        srcname = srcname.substring(srcnamesplit[0].length() + 1);
                    }
                }
                for (int i = 1; true; i++) {
                    prefixname = "(" + i + ")";
                    targetname = prefixname + srcname;
                    targetFile = new File(targetFile.getParent(), targetname);
                    if (!targetFile.exists()) {
                        break;
                    }
                }
                targetFile = new File(targetFile.getParent(), targetname);
            }
            /* 复制 */
            copyfile(srcFile, targetFile);
        }
    }

    /**
     * 移动文件
     *
     * @param request
     * @param currentPath         当前路径
     * @param directoryName       文件名
     * @param targetdirectorypath 目标路径
     * @throws Exception
     */
    public void moveDirectory(HttpServletRequest request, String currentPath, String[] directoryName,
                              String targetdirectorypath) throws Exception {
        // TODO Auto-generated method stub
        for (String srcName : directoryName) {
            File srcFile = new File(getFileName(request, currentPath), srcName);
            File targetFile = new File(getFileName(request, targetdirectorypath), srcName);
            /* 处理目标目录中存在同名文件或文件夹问题 */
            String srcname = srcName;
            String prefixname = "";
            String targetname = "";
            if (targetFile.exists()) {
                String[] srcnamesplit = srcname.split("\\)");
                if (srcnamesplit.length > 1) {
                    String intstring = srcnamesplit[0].substring(1);
                    Pattern pattern = Pattern.compile("[0-9]*");
                    Matcher isNum = pattern.matcher(intstring);
                    if (isNum.matches()) {
                        srcname = srcname.substring(srcnamesplit[0].length() + 1);
                    }
                }
                for (int i = 1; true; i++) {
                    prefixname = "(" + i + ")";
                    targetname = prefixname + srcname;
                    targetFile = new File(targetFile.getParent(), targetname);
                    if (!targetFile.exists()) {
                        break;
                    }
                }
                targetFile = new File(targetFile.getParent(), targetname);
            }
            /* 移动即先复制，再删除 */
            copyfile(srcFile, targetFile);
            delFile(srcFile);
        }
    }

    /**
     * 递归统计用户文件大小
     *
     * @param srcFile 位置
     * @return
     */
    private long countFileSize(File srcFile) {
        File[] listFiles = srcFile.listFiles();
        if (listFiles == null) {
            return 0;
        }
        long count = 0;
        for (File file : listFiles) {
            if (file.isDirectory()) {
                count += countFileSize(file);
            } else {
                count += file.length();
            }
        }
        return count;
    }

    /**
     * 统计用户文件大小
     *
     * @param request
     * @return
     */
    public String countFileSize(HttpServletRequest request) {
        long countFileSize = countFileSize(new File(getFileName(request, null)));
        return FileUtils.getDataSize(countFileSize);
    }

    /**
     * 重新计算文件大小
     *
     * @param request
     */
    public void reSize(HttpServletRequest request) {
        String userName = UserUtils.getUsername(request);
        try {
            userMapper.reSize(userName, countFileSize(request));
        } catch (Exception e) {
            e.printStackTrace();

        }
    }


    /**
     * 响应文件流
     *
     * @param response
     * @param request
     * @param currentPath 当前路径
     * @param fileName    文件名
     * @param type        文件类型
     * @throws IOException
     */
    public void respFile(HttpServletResponse response, HttpServletRequest request, String currentPath, String fileName,
                         String type) throws IOException {
        File file = new File(getFileName(request, currentPath), fileName);
        InputStream inputStream = new FileInputStream(file);
        if ("docum".equals(type)) {
            response.setCharacterEncoding("UTF-8");
            IOUtils.copy(inputStream, response.getWriter(), "UTF-8");
//            /////////////////////////////////已修改
            inputStream.close();
        } else {
            IOUtils.copy(inputStream, response.getOutputStream());
            inputStream.close();
        }
    }

    /**
     * 打开文档文件
     *
     * @param request
     * @param currentPath
     * @param fileName
     * @return
     * @throws Exception
     */
    public String openOffice(HttpServletRequest request, String currentPath, String fileName) throws Exception {
        return officeMapper.getOfficeId(FileUtils.MD5(new File(getFileName(request, currentPath), fileName)));
    }
}
