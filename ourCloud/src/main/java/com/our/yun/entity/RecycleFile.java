package com.our.yun.entity;

import lombok.Data;

import java.util.Date;

/**
 * 回收站文件实体类
 * @author Frank
 *
 */

public class RecycleFile extends FileCustom {
	private Integer fileId;
	private Date deleteTime;

	public Integer getFileId() {
		return fileId;
	}

	public void setFileId(Integer fileId) {
		this.fileId = fileId;
	}

	public Date getDeleteTime() {
		return deleteTime;
	}

	public void setDeleteTime(Date deleteTime) {
		this.deleteTime = deleteTime;
	}
}
