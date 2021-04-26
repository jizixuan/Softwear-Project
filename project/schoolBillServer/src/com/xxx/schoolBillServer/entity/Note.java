package com.xxx.schoolBillServer.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "note")
public class Note {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;         //�ʼ�ID
    private String content; //�ʼ�����
    private String createTime; //�ʼǴ���ʱ��
    private String title;  //   �����title ָ����  �ʼǵĵ�һ�� һ�㶼�Ǹ�Ҫ ������ʾ��Ҫ
    private String subContent; //�����subContentָ���� �ʼǵĵڶ��У����ڷ�Ӧ�����û��Ŀ�ͷ   �൱�����ݵ���д
    @Column(name = "user_id")
    private int userId;
    
    
	public Note() {
		super();
	}
	public Note(int id, String content, String createTime, String title, String subContent) {
		super();
		this.id = id;
		this.content = content;
		this.createTime = createTime;
		this.title = title;
		this.subContent = subContent;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSubContent() {
		return subContent;
	}
	public void setSubContent(String subContent) {
		this.subContent = subContent;
	}
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	@Override
	public String toString() {
		return "Note [id=" + id + ", content=" + content + ", createTime=" + createTime + ", title=" + title
				+ ", subContent=" + subContent + "]";
	}
    
}
