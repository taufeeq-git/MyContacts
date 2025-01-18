package com.taufeeq.web.model;

public class Group {
	private int groupId;
	private int userId;
	private int contactId;
	private long created_time;
	private long modified_time;
	private String groupName;

	public Group() {

	}

	public Group(int groupId, int userId, String groupName) {
		this.groupId = groupId;
		this.groupName = groupName;
	}

	public Group(int userId, String groupName) {
		this.userId = userId;
		this.groupName = groupName;
	}

	public int getContactId() {
		return contactId;
	}

	public void setContactId(int contactId) {
		this.contactId = contactId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getGroupId() {
		return groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public int getUserId() {
		return userId;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public long getCreated_time() {
		return created_time;
	}

	public void setCreated_time(long created_time) {
		this.created_time = created_time;
	}

	public long getModified_time() {
		return modified_time;
	}

	public void setModified_time(long modified_time) {
		this.modified_time = modified_time;
	}

	@Override
	public String toString() {
		return "Group [groupId=" + groupId + ", userId=" + userId + ", contactId=" + contactId + ", created_time="
				+ created_time + ", modified_time=" + modified_time + ", groupName=" + groupName + "]";
	}
}
