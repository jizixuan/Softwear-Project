package com.example.myapplication.note.entity;

import java.io.Serializable;

public class NoteItem implements Serializable {
    private int id;         //笔记ID
    private String content; //笔记内容
    private String createTime; //笔记创建时间
    private String title;  //   这里的title 指的是  笔记的第一行 一般都是纲要 用于显示纲要
    private String subContent; //这里的subContent指的是 笔记的第二行，用于反应除了用户的开头   相当于内容的缩写


    public NoteItem(int id, String content, String createTime, String title, String subContent) {
        this.id = id;
        this.content = content;
        this.createTime = createTime;
        this.title = title;
        this.subContent = subContent;
    }

    public NoteItem() {
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
        return "NoteItem{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", createTime='" + createTime + '\'' +
                ", title='" + title + '\'' +
                ", subContent='" + subContent + '\'' +
                '}';
    }
}
