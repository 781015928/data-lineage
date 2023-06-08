package com.crazypug.datalineage.metadata;

public class ContextHolder {

    private static ThreadLocal<Context> schemataThreadLocal = new ThreadLocal<>();


    public static void setContext(Context schemata) {
        schemataThreadLocal.set(schemata);
    }

    public static Context getContext() {
        return schemataThreadLocal.get();
    }


    public static void clearContext() {
        schemataThreadLocal.remove();
    }

}
