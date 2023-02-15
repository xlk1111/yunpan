package com.our.yun.utils;

import com.baidubce.BceClientConfiguration;
import com.baidubce.auth.DefaultBceCredentials;
import com.baidubce.services.doc.DocClient;
import org.springframework.util.DigestUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.apache.commons.io.FileUtils.*;


public class FileUtils {
    public static String getDataSize(long size) {
        DecimalFormat formater = new DecimalFormat("####.0");
        if (size < 1024) {
            return size + "B";
        } else if (size < 1024 * 1024) {
            float kbsize = size / 1024f;
            return formater.format(kbsize) + "KB";
        } else if (size < 1024 * 1024 * 1024) {
            float mbsize = size / 1024f / 1024f;
            return formater.format(mbsize) + "MB";
        } else if (size < 1024 * 1024 * 1024 * 1024) {
            float gbsize = size / 1024f / 1024f / 1024f;
            return formater.format(gbsize) + "GB";
        } else {
            return "-";
        }
    }

    public static String formatTime(long time) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(time));
    }
    public static String formatTime(Date time) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time);
    }

    public static String getUrl8() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }

    public final static Map<String, String> FILE_TYPE_MAP = new HashMap<String, String>();

    static {
        FILE_TYPE_MAP.put("jpg", "image"); //JPEG (jpg)
        FILE_TYPE_MAP.put("png", "image"); //PNG (png)
        FILE_TYPE_MAP.put("gif", "image"); //GIF (gif)
        FILE_TYPE_MAP.put("tif", "image"); //TIFF (tif)
        FILE_TYPE_MAP.put("bmp", "image"); //16色位图(bmp)
        FILE_TYPE_MAP.put("bmp", "image"); //24位位图(bmp)
        FILE_TYPE_MAP.put("bmp", "image"); //256色位图(bmp)

        FILE_TYPE_MAP.put("html", "docum"); //HTML (html)
        FILE_TYPE_MAP.put("htm", "docum"); //HTM (htm)
        FILE_TYPE_MAP.put("css", "docum"); //css
        FILE_TYPE_MAP.put("js", "docum"); //js
        FILE_TYPE_MAP.put("ini", "docum");
        FILE_TYPE_MAP.put("txt", "docum");
        FILE_TYPE_MAP.put("jsp", "docum");//jsp文件
        FILE_TYPE_MAP.put("sql", "docum");//xml文件
        FILE_TYPE_MAP.put("xml", "docum");//xml文件
        FILE_TYPE_MAP.put("java", "docum");//java文件
        FILE_TYPE_MAP.put("bat", "docum");//bat文件
        FILE_TYPE_MAP.put("mxp", "docum");//bat文件
        FILE_TYPE_MAP.put("properties", "docum");//bat文件

        FILE_TYPE_MAP.put("doc", "office"); //MS Excel 注意：word、msi 和 excel的文件头一样
        FILE_TYPE_MAP.put("docx", "office");//docx文件
        FILE_TYPE_MAP.put("vsd", "office"); //Visio 绘图
        FILE_TYPE_MAP.put("mdb", "office"); //MS Access (mdb)
        FILE_TYPE_MAP.put("pdf", "office"); //Adobe Acrobat (pdf)
        FILE_TYPE_MAP.put("xlsx", "office");//docx文件
        FILE_TYPE_MAP.put("xls", "office");//docx文件
        FILE_TYPE_MAP.put("pptx", "office");//docx文件
        FILE_TYPE_MAP.put("ppt", "office");//docx文件
        FILE_TYPE_MAP.put("wps", "office");//WPS文字wps、表格et、演示dps都是一样的

        FILE_TYPE_MAP.put("mov", "video");
        FILE_TYPE_MAP.put("rmvb", "video"); //rmvb/rm相同
        FILE_TYPE_MAP.put("flv", "video"); //flv与f4v相同
        FILE_TYPE_MAP.put("mp4", "video");
        FILE_TYPE_MAP.put("avi", "video");
        FILE_TYPE_MAP.put("wav", "video"); //Wave (wav)
        FILE_TYPE_MAP.put("wmv", "video"); //wmv与asf相同
        FILE_TYPE_MAP.put("mpg", "video"); //

        FILE_TYPE_MAP.put("mp3", "audio");
        FILE_TYPE_MAP.put("mid", "audio"); //MIDI (mid)

        FILE_TYPE_MAP.put("zip", "comp");
        FILE_TYPE_MAP.put("rar", "comp");
        FILE_TYPE_MAP.put("gz", "comp");//gz文件

//		FILE_TYPE_MAP.put("class", "file");//bat文件
//		FILE_TYPE_MAP.put("jar", "file"); 
//		FILE_TYPE_MAP.put("exe", "file");//可执行文件
//		FILE_TYPE_MAP.put("mf", "file");//MF文件
//		FILE_TYPE_MAP.put("chm", "file");//bat文件
//		FILE_TYPE_MAP.put("torrent", "file");
//		FILE_TYPE_MAP.put("wpd", "file"); //WordPerfect (wpd)   
//		FILE_TYPE_MAP.put("dbx", "file"); //Outlook Express (dbx)     
//		FILE_TYPE_MAP.put("pst", "file"); //Outlook (pst)      
//		FILE_TYPE_MAP.put("qdf", "file"); //Quicken (qdf)     
//		FILE_TYPE_MAP.put("pwl", "file"); //Windows Password (pwl)         
//		FILE_TYPE_MAP.put("ram", "file"); //Real Audio (ram)
    }

    public static String getFileType(File file) {
        if (file.isDirectory()) {
            return "folder-open";
        }
        String fileName = file.getPath();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        String fileType = FILE_TYPE_MAP.get(suffix);
        return fileType == null ? "file" : fileType;
    }

    private static DocClient docClient = null;

    public static DocClient getDocClient() {

        String ak = "1286c5fe300546de866ef2df1932986c";
        String sk = "be83fe1143ce413a8e93515d0796abe3";
        if (docClient == null) {
            BceClientConfiguration config = new BceClientConfiguration();
            config.setConnectionTimeoutInMillis(3000);
            config.setSocketTimeoutInMillis(2000);
            config.setCredentials(new DefaultBceCredentials(ak, sk));
            docClient = new DocClient(config);
        }
        return docClient;
    }

    public static String MD5(File file) {
        byte[] bys = null;
        try {
            bys = org.apache.commons.io.FileUtils.readFileToByteArray(file);
            return DigestUtils.md5DigestAsHex(bys).toUpperCase();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    ///////////////////////////////////    移动文件夹或或文件到另一个目录

    public static void moveToDirectory(File src, File destDir, boolean createDestDir, String fileName) throws IOException {
        if (src == null) {
            throw new NullPointerException("Source must not be null");
        } else if (destDir == null) {
            throw new NullPointerException("Destination must not be null");
        } else if (!src.exists()) {
            throw new FileNotFoundException("Source '" + src + "' does not exist");
        } else {
            if (src.isDirectory()) {
                moveDirectoryToDirectory(src, destDir, createDestDir, fileName);
            } else {
                moveFileToDirectory(src, destDir, createDestDir, fileName);
            }

        }
    }


    public static void moveDirectoryToDirectory(File src, File destDir, boolean createDestDir, String fileName) throws IOException {
        if (src == null) {
            throw new NullPointerException("Source must not be null");
        } else if (destDir == null) {
            throw new NullPointerException("Destination directory must not be null");
        } else {
            if (!destDir.exists() && createDestDir) {
                destDir.mkdirs();
            }

            if (!destDir.exists()) {
                throw new FileNotFoundException("Destination directory '" + destDir + "' does not exist [createDestDir=" + createDestDir + "]");
            } else if (!destDir.isDirectory()) {
                throw new IOException("Destination '" + destDir + "' is not a directory");
            } else {
                moveDirectory(src, new File(destDir, fileName));
            }
        }
    }

    public static void moveDirectory(File srcDir, File destDir) throws IOException {
        if (srcDir == null) {
            throw new NullPointerException("Source must not be null");
        } else if (destDir == null) {
            throw new NullPointerException("Destination must not be null");
        } else if (!srcDir.exists()) {
            throw new FileNotFoundException("Source '" + srcDir + "' does not exist");
        } else if (!srcDir.isDirectory()) {
            throw new IOException("Source '" + srcDir + "' is not a directory");
        } else {
            boolean rename = srcDir.renameTo(destDir);
            if (!rename) {
                if (destDir.getCanonicalPath().startsWith(srcDir.getCanonicalPath())) {
                    throw new IOException("Cannot move directory: " + srcDir + " to a subdirectory of itself: " + destDir);
                }

                copyDirectory(srcDir, destDir);
                deleteDirectory(srcDir);
                if (srcDir.exists()) {
                    throw new IOException("Failed to delete original directory '" + srcDir + "' after copy to '" + destDir + "'");
                }
            }

        }
    }

    public static void moveFileToDirectory(File srcFile, File destDir, boolean createDestDir, String fileName) throws IOException {
        if (srcFile == null) {
            throw new NullPointerException("Source must not be null");
        } else if (destDir == null) {
            throw new NullPointerException("Destination directory must not be null");
        } else {
            if (!destDir.exists() && createDestDir) {
                destDir.mkdirs();
            }

            if (!destDir.exists()) {
                throw new FileNotFoundException("Destination directory '" + destDir + "' does not exist [createDestDir=" + createDestDir + "]");
            } else if (!destDir.isDirectory()) {
                throw new IOException("Destination '" + destDir + "' is not a directory");
            } else {
                moveFile(srcFile, new File(destDir, fileName));
            }
        }
    }


    public static void moveFile(File srcFile, File destFile) throws IOException {
        if (srcFile == null) {
            throw new NullPointerException("Source must not be null");
        } else if (destFile == null) {
            throw new NullPointerException("Destination must not be null");
        } else if (!srcFile.exists()) {
            throw new FileNotFoundException("Source '" + srcFile + "' does not exist");
        } else if (srcFile.isDirectory()) {
            throw new IOException("Source '" + srcFile + "' is a directory");
        } else if (destFile.isDirectory()) {
            throw new IOException("Destination '" + destFile + "' is a directory");
        } else {


//            File[] files = destFile.getParentFile().listFiles();
//
//            for (int i = 0; i < files.length; i++) {
//                if (files[i].getName().equals(destFile.getName())) {
//                    String fileName = destFile.getName();
////                    fileName = "xlk(1).mp3";
//                    fileName = test01(fileName);
//                    destFile = new File(destFile.getParentFile().getPath(), fileName);
//                }
//            }

            boolean rename = srcFile.renameTo(destFile);


            if (!rename) {
                copyFile(srcFile, destFile);
                if (!srcFile.delete()) {
                    deleteQuietly(destFile);
                    throw new IOException("Failed to delete original file '" + srcFile + "' after copy to '" + destFile + "'");
                }
            }

        }
    }

///////////////////////////////////////////////////////////////////

    //重复文件名加后缀
    public static String updateFileName(String fileName, int fileCount, File src) {
//        String fileName = "xlk.mp3";
        if (fileCount == 0) {
            return fileName;
        }
        if (src.isDirectory()) {
            fileName = fileName + "(" + fileCount + ")";
            return fileName;
        }

        String[] split = fileName.split("\\.");
        int k = split.length;
        split[k - 2] = split[k - 2] + "(" + fileCount + ")";
        fileName = "";
        for (int i = 0; i < k - 1; i++) {
            fileName = fileName + split[i] + ".";
        }
        fileName = fileName + split[k - 1];

        return fileName;
    }


}
