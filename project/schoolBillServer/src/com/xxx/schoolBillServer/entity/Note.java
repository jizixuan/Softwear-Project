package com.xxx.schoolBillServer.entity;

public class Note {
	private int id;         //�ʼ�ID
    private String content; //�ʼ�����
    private String createTime; //�ʼǴ���ʱ��
    private String title;  //   �����title ָ����  �ʼǵĵ�һ�� һ�㶼�Ǹ�Ҫ ������ʾ��Ҫ
    private String subContent; //�����subContentָ���� �ʼǵĵڶ��У����ڷ�Ӧ�����û��Ŀ�ͷ   �൱�����ݵ���д
    
    
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
	@Override
	public String toString() {
		return "Note [id=" + id + ", content=" + content + ", createTime=" + createTime + ", title=" + title
				+ ", subContent=" + subContent + "]";
	}
    
}
