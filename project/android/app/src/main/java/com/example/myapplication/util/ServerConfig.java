package com.example.myapplication.util;

import com.example.myapplication.entity.BillType;
import com.example.myapplication.entity.User;

import java.util.ArrayList;
import java.util.List;

public class ServerConfig {
    public final static String SERVER_HOME = "http://10.7.88.237:8080/schoolBillServer/";
    public final static String SERVER_HOME1 = "http://10.7.89.254:8080/schoolBillServer/";
    public static int USER_ID=1;
    public static List<BillType> BILL_TYPES=new ArrayList<>();
    public static double BUDGET;
    public static User USER_INFO = new User();
}
