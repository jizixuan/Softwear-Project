package com.example.myapplication.util;

import com.example.myapplication.entity.IconItem;
import com.example.myapplication.entity.IoDetail;

import java.util.Comparator;

public class NumSortInOrOut implements Comparator<IoDetail> {
    @Override
    public int compare(IoDetail ioDetail, IoDetail t1) {
        return (int) -(ioDetail.getNum()-t1.getNum());
    }
}
