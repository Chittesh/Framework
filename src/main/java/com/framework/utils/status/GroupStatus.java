package com.framework.utils.status;

public class GroupStatus {
	
	private String id;
	private TestStatus status;
	
	public GroupStatus(String groupId, TestStatus status) {
		this.id = groupId;
		this.status = status;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String groupId) {
		this.id = groupId;
	}
	public TestStatus getStatus() {
		return status;
	}
	public void setStatus(TestStatus status) {
		this.status = status;
	}
}
