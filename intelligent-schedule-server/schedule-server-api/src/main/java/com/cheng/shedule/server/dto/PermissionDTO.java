package com.cheng.shedule.server.dto;

import lombok.Data;


@Data
public class PermissionDTO implements java.io.Serializable {
	Long groupId;
	String userId;
}
