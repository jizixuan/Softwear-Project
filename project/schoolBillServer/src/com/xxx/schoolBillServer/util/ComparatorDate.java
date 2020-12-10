package com.xxx.schoolBillServer.util;

import java.util.Comparator;
import java.util.Date;

import java.text.ParseException;


import com.xxx.schoolBillServer.entity.BillItem;

public class ComparatorDate implements Comparator{
	public static final String TAG = "ComparatorDate";
	public int compare(Object obj1, Object obj2) {
        BillItem t1 = (BillItem) obj1;
        BillItem t2 = (BillItem) obj2;
     //   return t1.getTradetime().compareTo(t2.getTradetime());  // 时间格式不好，不然可以直接这样比较
        Date d1, d2;
        d1 = t1.getDate();
		d2 = t2.getDate();
        if (d1.before(d2)) {
            return -1;
        } else {
            return 1;
        }
	}
}
