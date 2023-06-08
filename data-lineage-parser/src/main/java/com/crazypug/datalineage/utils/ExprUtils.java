package com.crazypug.datalineage.utils;

public class ExprUtils {

    public static String toExpr(String expr) {
        return expr.replace('`', ' ').trim();
    }


    public static boolean equals(Object a, Object b) {
        return (a == b) || (a != null && a.equals(b));
    }


    public static boolean equalsIgnoreCase(String a, String b) {
        return (a == b) || (a != null && a.equalsIgnoreCase(b))
                ||b!=null&&a!=null
                &&a.replaceAll("`","").equalsIgnoreCase(b.replaceAll("`",""))
                ;
    }
}
