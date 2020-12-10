package com.xxx.schoolBillServer.util;

import java.util.Comparator;
import java.util.Date;

import com.xxx.schoolBillServer.entity.BillItem;

public class ComparatorMonth implements Comparator{
	public static final String TAG = "ComparatorMonth";
	public int compare(Object obj1, Object obj2) {
        BillItem t1 = (BillItem) obj1;
        BillItem t2 = (BillItem) obj2;
     //   return t1.getTradetime().compareTo(t2.getTradetime());  // 时间格式不好，不然可以直接这样比较
        if (t1.getMonth()>t2.getMonth()) {
            return 1;
        } else {
            return -1;
        }
	}
}
