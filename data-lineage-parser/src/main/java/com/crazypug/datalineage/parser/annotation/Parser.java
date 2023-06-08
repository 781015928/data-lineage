package com.crazypug.datalineage.parser.annotation;


import com.crazypug.datalineage.core.Bean;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Bean
public @interface Parser {

    /**
     * 语法树节点类型
     *
     * @return
     */
    Class value();
}
