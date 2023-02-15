package com.our.yun.entity;

import lombok.Data;

/**
 * 文件对象实体类
 * @author Frank
 *
 */
@Data
public class FileCustom {
	private String fileName;
	private String fileType;
	private String fileSize;
	private String lastTime;
	private String filePath;
	private String currentPath;
	private String recyclePath;

}
