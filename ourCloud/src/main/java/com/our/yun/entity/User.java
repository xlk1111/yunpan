package com.our.yun.entity;

import lombok.Data;

@Data
public class User {
	public static final String NAMESPACE = "username";
	
	public static final String RECYCLE = "recycle";
	private Integer id;
	private String username;
	private String password;
	private String countSize;
	private String totalSize;


}
