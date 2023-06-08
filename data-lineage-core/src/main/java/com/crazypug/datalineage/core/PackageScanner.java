package com.crazypug.datalineage.core;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;

public class PackageScanner {


    public PackageScanner() {
    }

    // scanPackage方法的重载
    public List<Class> scanPackage(Class<?> klass) {
        return scanPackage(klass.getPackage().getName());
    }

    public List<Class> scanPackage(String packageName) {
        // 将包名称转换为路径名称的形式
        String packagePath = packageName.replace(".", "/");
        List<Class> classes = new ArrayList<>();
        try {
            // 由类加载器得到URL的枚举
            Enumeration<URL> resources = Thread.currentThread()
                    .getContextClassLoader()
                    .getResources(packagePath);

            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                // 处理jar包
                if (url.getProtocol().equals("jar")) {
                    List<Class> fileClasses = parse(url,packageName);
                    classes.addAll(fileClasses);
                } else {
                    File file = new File(url.toURI());
                    if (file.exists()) {
                        // 处理普通包
                        List<Class> fileClasses = parse(file, packageName);
                        classes.addAll(fileClasses);
                    }
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return classes;
    }

    // jar包的扫描
    private List<Class> parse(URL url, String packageName) throws IOException {
        Enumeration<JarEntry> jarEntries = ((JarURLConnection) url.openConnection())
                .getJarFile().entries();
        List<Class> classes = new ArrayList<>();
        while (jarEntries.hasMoreElements()) {
            JarEntry jarEntry = jarEntries.nextElement();
            String jarName = jarEntry.getName();

            if (!jarEntry.isDirectory() && jarName.endsWith(".class")) {
                // 将文件路径名转换为包名称的形式
                String className = jarName.replace("/", ".").replace(".class", "");
                if (className.startsWith(packageName)) {
                    Class clazz = dealClassName(className);
                    if (clazz != null) {
                        classes.add(clazz);
                    }
                }

            }
        }

        return classes;
    }

    // 普通包的扫描
    private List<Class> parse(File curFile, String packageName) {
        File[] fileList = curFile.listFiles(new FileFilter() {
            // 筛选文件夹和class文件，其余文件不处理
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory() || pathname.getName().endsWith(".class");
            }
        });

        List<Class> classes = new ArrayList<>();

        // 目录就是一颗树，对树进行递归，找到class文件
        for (File file : fileList) {
            String fileName = file.getName();
            if (file.isDirectory()) {
                List<Class> parse = parse(file, packageName + "." + fileName);
                classes.addAll(parse);
            } else {
                String className = packageName + "." + fileName.replace(".class", "");
                Class clazz = dealClassName(className);
                if (clazz != null) {
                    classes.add(clazz);
                }

            }
        }
        return classes;
    }

    // 将找到的class文件生成类对象
    private Class dealClassName(String className) {
        try {
            Class<?> klass = Class.forName(className);

            // 注解、接口、枚举、原始类型不做处理
            if (!klass.isAnnotation()
                    && !klass.isInterface()
                    && !klass.isEnum()
                    && !klass.isPrimitive()) {
                return klass;
            }
        } catch (Throwable e) {
            e.printStackTrace(System.out);
        }
        return null;
    }
}
