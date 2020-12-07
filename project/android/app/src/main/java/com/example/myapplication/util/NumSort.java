package com.example.myapplication.util;

import com.example.myapplication.entity.IconItem;

import java.util.Comparator;

public class NumSort implements Comparator<IconItem> {
    @Override
    public int compare(IconItem iconItem, IconItem t1) {
        return (int) -(iconItem.getNum()-t1.getNum());
    }
}
