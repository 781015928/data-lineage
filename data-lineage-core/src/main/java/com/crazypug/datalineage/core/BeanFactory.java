package com.crazypug.datalineage.core;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BeanFactory {
    public Map<Class, Object> classForBeans = new HashMap<Class, Object>();
    public Map<String, Object> nameForBeans = new HashMap<String, Object>();

    public List<Object> listForBean = new ArrayList<>();

    public static BeanFactory getInstance() {
        return Holder.instance;
    }

    public List<Object> getBeans() {
        return listForBean;
    }

    public void load(String... packageName) {
        PackageScanner packageScanner = new PackageScanner();
        List<Object> beans = Stream.of(packageName)
                .map(packageScanner::scanPackage)
                .flatMap(Collection::stream)
                .filter(it -> isBean(it))
                .map(this::createBean)
                .collect(Collectors.toList());

        beans.stream()
                .forEach(it -> {
                    nameForBeans.put(it.getClass().getSimpleName(), it);
                });

        Map<Class, Object> beanMaps = beans.stream()
                .map(it -> new AbstractMap.SimpleEntry<Object, List<Class>>(it, getSupportClassType(it.getClass())))
                .flatMap(it -> it
                        .getValue()
                        .stream().map(clazz -> new AbstractMap.SimpleEntry<Class, Object>(clazz, it.getKey())))
                .collect(Collectors.toMap(it -> it.getKey(), it -> it.getValue(), new BinaryOperator<Object>() {
                    @Override
                    public Object apply(Object o, Object o2) {
                        return o;
                    }
                }));


        beans.stream()
                .forEach(it -> {
                    getInjectFields(it.getClass().getDeclaredFields())
                            .stream()
                            .forEach(field -> {
                                Class fieldType = field.getType();
                                Object bean = beanMaps.get(fieldType);
                                if (bean == null) {
                                    throw new RuntimeException("can not find bean for " + fieldType);
                                }
                                inject(field, it, bean);
                            });
                });

        listForBean.addAll(beans);
        this.classForBeans.putAll(beanMaps);

    }

    public void inject(Field field, Object obj, Object fieldObj) {
        boolean accessible = field.isAccessible();
        field.setAccessible(true);
        try {
            field.set(obj, fieldObj);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        field.setAccessible(accessible);
    }


    public boolean isBean(Class clazz) {
        return clazz.getAnnotation(Bean.class) != null ||
                Stream.of(clazz.getAnnotations())
                        .anyMatch(it -> it.annotationType().getAnnotation(Bean.class) != null);

    }


    public <T> T getBean(Class<T> clazz) {
        return (T) classForBeans.get(clazz);
    }

    public <T> T getBean(String beanName) {
        return (T) classForBeans.get(beanName);
    }

    public <T> T createBean(Class clazz) {
        try {
            return (T) clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


    public List<Class> getSupportClassType(Class clazz) {
        List<Class> classes = new ArrayList<>();
        Class temp = clazz;
        while (temp != null && temp != Object.class) {
            classes.add(temp);
            Class[] interfaces = temp.getInterfaces();
            classes.addAll(Arrays.asList(interfaces));
            temp = temp.getSuperclass();
        }
        return classes;
    }


    public static List<Field> getInjectFields(Field[] fields) {
        return Stream.of(fields)
                .filter(it ->
                        Objects.nonNull(it.getAnnotation(Inject.class))
                )
                .collect(Collectors
                        .toList());
    }

    static class Holder {
        static BeanFactory instance = new BeanFactory();
    }


}
