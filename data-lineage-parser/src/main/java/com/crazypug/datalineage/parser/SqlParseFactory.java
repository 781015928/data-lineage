package com.crazypug.datalineage.parser;

import com.crazypug.datalineage.core.BeanFactory;
import com.crazypug.datalineage.metadata.MetaDataProvider;
import com.crazypug.datalineage.parser.annotation.Parser;
import com.crazypug.datalineage.parser.sqlselect.SqlSelectParser;
import com.crazypug.datalineage.core.PackageScanner;

import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;


public class SqlParseFactory {

    //容器
    private static final Map<Type, SQLStatementParser> SQL_STATEMENT_PARSER_MAP = new HashMap<>();
    private static final Map<Type, SQLExprParser> SQL_EXPR_PARSER_MAP = new HashMap<>();
    private static final Map<Type, SQLQueryParser> SQL_QUERY_PARSER_MAP = new HashMap<>();
    private static final Map<Type, AbsSqlSelectParser> SQL_SELECT_PARSER_MAP = new HashMap<>();
    private static final Map<Type, SQLTableSourceParser> SQL_TABLE_SOURCE_PARSER_MAP = new HashMap<>();


    private static final Map<Type, AbsSQLSelectItemParser> SQL_SELECT_ITEM_PARSER_MAP = new HashMap<>();


    static {
        BeanFactory beanFactory = BeanFactory.getInstance();

        beanFactory.load("com.crazypug.datalineage");
        PackageScanner packageScanner = new PackageScanner();
        List<Class> astClasses = packageScanner.scanPackage("com.alibaba.druid.sql");

        MetaDataProvider metaDataProvider = beanFactory.getBean(MetaDataProvider.class);

        metaDataProvider.reload();

        List<Object> beans = beanFactory.getBeans();

        List<SQLParser> parsers = beans.stream()
                .filter(it -> it.getClass().getAnnotation(Parser.class) != null)
                .map(it -> (SQLParser) it)
                .collect(Collectors.toList());

        for (SQLParser instance : parsers) {
            Parser parser = instance.getClass().getAnnotation(Parser.class);
            Class targetType = parser.value();
            List<Class> childClass = findChildClass(targetType, astClasses);

            if (instance instanceof SQLStatementParser) {
                putParser(SQL_STATEMENT_PARSER_MAP, childClass, targetType, instance);
            } else if (instance instanceof SQLExprParser) {
                putParser(SQL_EXPR_PARSER_MAP, childClass, targetType, instance);
            } else if (instance instanceof SQLQueryParser) {
                putParser(SQL_QUERY_PARSER_MAP, childClass, targetType, instance);
            } else if (instance instanceof SqlSelectParser) {
                putParser(SQL_SELECT_PARSER_MAP, childClass, targetType, instance);
            } else if (instance instanceof SQLTableSourceParser) {
                putParser(SQL_TABLE_SOURCE_PARSER_MAP, childClass, targetType, instance);
            } else if (instance instanceof AbsSQLSelectItemParser) {
                putParser(SQL_SELECT_ITEM_PARSER_MAP, childClass, targetType, instance);

            }

        }
        SQL_SELECT_ITEM_PARSER_MAP.toString();

    }

    /**
     * 该过程保证一个父类ast节点的解析器支持所有子类ast节点的解析，子类ast节点的解析器优先级高于父类ast节点的解析器
     * 例如：SQLSelectParser支持SQLSelectQueryBlock,SQLSelectQuery,SQLSelectStatement,SQLSelect
     * @param maps
     * @param childTypes
     * @param targetType
     * @param sqlParser
     */
    public static void putParser(Map maps, List<Class> childTypes, Class targetType, Object sqlParser) {
        maps.put(targetType, sqlParser);
        for (Class childType : childTypes) {
            Object childTypeParser = maps.get(childType);
            if (childTypeParser != null) {
                Class<?> childTypeParserClass = childTypeParser.getClass();
                Parser parser = childTypeParserClass.getAnnotation(Parser.class);
                Class statement = parser.value();
                //已经存在更加明确的子类,所以不需要替换
                if (statement == childType) {
                    return;
                }
                //如果已经存在的子类是父类的子类,则替换
                if (targetType.isAssignableFrom(statement)) {
                    maps.put(childType, sqlParser);
                }
            } else {
                maps.put(childType, sqlParser);
            }
        }
    }

    /**
     * 查找父类的所有子类
     *
     * @param parentClass
     * @param classes
     * @return
     */
    public static List<Class> findChildClass(Class<?> parentClass, List<Class> classes) {
        List<Class> result = new ArrayList<>();
        for (Class clazz : classes) {
            if (parentClass.isAssignableFrom(clazz) && !parentClass.equals(clazz)) {
                result.add(clazz);
            }
        }
        return result;
    }

    public static SQLStatementParser getStatement(Type clazz) {

        SQLStatementParser sqlStatementParser = SQL_STATEMENT_PARSER_MAP.get(clazz);
        if (Objects.isNull(sqlStatementParser)) {
            throw new UnsupportedOperationException(clazz.getTypeName());
        }
        return sqlStatementParser;
    }

    public static AbsSqlSelectParser getSqlSelect(Type clazz) {

        AbsSqlSelectParser sqlSelectParser = SQL_SELECT_PARSER_MAP.get(clazz);
        if (Objects.isNull(sqlSelectParser)) {
            throw new UnsupportedOperationException(clazz.getTypeName());
        }
        return sqlSelectParser;
    }

    public static SQLTableSourceParser getSqlSelectSource(Type clazz) {
        SQLTableSourceParser sqlTableSourceParser = SQL_TABLE_SOURCE_PARSER_MAP.get(clazz);
        if (Objects.isNull(sqlTableSourceParser)) {
            throw new UnsupportedOperationException(clazz.getTypeName());
        }
        return sqlTableSourceParser;
    }

    public static AbsSQLSelectItemParser getSqlSelectItem(Type clazz) {

        AbsSQLSelectItemParser sqlSelectParser = SQL_SELECT_ITEM_PARSER_MAP.get(clazz);
        if (Objects.isNull(sqlSelectParser)) {
            throw new UnsupportedOperationException(clazz.getTypeName());
        }
        return sqlSelectParser;
    }

    public static SQLQueryParser getSqlSelectQuery(Type clazz) {

        SQLQueryParser sqlQueryParser = SQL_QUERY_PARSER_MAP.get(clazz);
        if (Objects.isNull(sqlQueryParser)) {
            throw new UnsupportedOperationException(clazz.getTypeName());
        }
        return sqlQueryParser;
    }

    public static SQLExprParser getSQLExpr(Type clazz) {

        SQLExprParser sqlSelectParser = SQL_EXPR_PARSER_MAP.get(clazz);
        if (Objects.isNull(sqlSelectParser)) {
            throw new UnsupportedOperationException(clazz.getTypeName());
        }
        return sqlSelectParser;
    }
}
