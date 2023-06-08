package com.crazypug.datalineage.parser.tablesource;

import com.alibaba.druid.sql.ast.statement.*;
import com.crazypug.datalineage.parser.annotation.Parser;
import com.crazypug.datalineage.model.TreeNode;
import com.crazypug.datalineage.parser.SQLTableSourceParser;
import com.crazypug.datalineage.parser.SqlParseFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Parser(SQLJoinTableSource.class)
public class SQLJoinTableSourceParser implements SQLTableSourceParser<SQLJoinTableSource> {
    @Override
    public void findColumn(TreeNode columnNode, String owner, String columnName, SQLSelectItem selectItem, SQLJoinTableSource tableSource, SQLWithSubqueryClause withSubqueryClause,SQLSelect select) {

        if (owner != null) {//有别名
            //找到所有子查询表和物理表
            List<SQLTableSource> allTableSource = getAllTableSource(tableSource);

            SQLTableSource sqlTableSource = allTableSource.stream()
                    .filter(it -> Objects.equals(it.getAlias(), owner) || Objects.equals(it.getAlias(), owner) || it instanceof SQLExprTableSource && Objects.equals(((SQLExprTableSource) it).getTableName(), owner)).findFirst()
                    .orElseThrow(() -> new RuntimeException(String.format("%s reference table %s does not exist \n%s", selectItem, owner, tableSource)));
            SqlParseFactory.getSqlSelectSource(sqlTableSource.getClass())
                    .findColumn(columnNode, owner, columnName, selectItem, sqlTableSource, withSubqueryClause,select);

        } else {
            String ownerName = findOwner(columnName, selectItem, tableSource, withSubqueryClause);
            if (ownerName == null) {
                    // 字段未使用表名.字段名的方式引用,以及别名.字段名的方式引用，所以无法直接找到表名，需要遍历所有表，找到所有字段名相同的表，从第一个表中找到字段
                List<SQLTableSource> allTableSource = getAllTableSource(tableSource);
                for (SQLTableSource sqlTableSource : allTableSource) {
                    if (SqlParseFactory.getSqlSelectSource(sqlTableSource.getClass()).haveColumn(columnName, sqlTableSource, withSubqueryClause)) {
                        SqlParseFactory.getSqlSelectSource(sqlTableSource.getClass())
                                .findColumn(columnNode, ownerName, columnName, selectItem, sqlTableSource, withSubqueryClause,select);
                        break;
                    }
                }



            } else {

                findColumn(columnNode, ownerName, columnName, selectItem, tableSource, withSubqueryClause,select);

            }


        }


    }

    @Override
    public boolean haveColumn(String columnName, SQLJoinTableSource tableSource, SQLWithSubqueryClause withSubqueryClause) {
        List<SQLTableSource> allTableSource = getAllTableSource(tableSource);
        for (SQLTableSource sqlTableSource : allTableSource) {
            if (SqlParseFactory.getSqlSelectSource(sqlTableSource.getClass()).haveColumn(columnName, sqlTableSource, withSubqueryClause)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String findOwner(String columnName, SQLSelectItem selectItem, SQLJoinTableSource tableSource, SQLWithSubqueryClause withSubqueryClause) {
        List<String> ownerNames = getAllTableSource(tableSource)
                .stream()
                .map(it -> SqlParseFactory.getSqlSelectSource(it.getClass()).findOwner(columnName, selectItem, it, withSubqueryClause))
                .filter(it -> it != null)
                .collect(Collectors.toList());


        if (ownerNames.size() > 1) {
            throw new RuntimeException(String.format("column %s is ambiguous \n%s", columnName, tableSource));
        }
        if (ownerNames.size() == 0) {
            return null;
        }
        return ownerNames.get(0);
    }

    public List<SQLTableSource> getAllTableSource(SQLTableSource tableSource) {
        if (!(tableSource instanceof SQLJoinTableSource)) {
            return Arrays.asList(tableSource);
        }

        List<SQLTableSource> tableSources = new ArrayList<SQLTableSource>();

        SQLTableSource left = ((SQLJoinTableSource) tableSource).getLeft();
        List<SQLTableSource> sqlTableLeftSources = getAllTableSource(left);

        tableSources.addAll(sqlTableLeftSources);
        SQLTableSource right = ((SQLJoinTableSource) tableSource).getRight();

        List<SQLTableSource> sqlTableRightSources = getAllTableSource(right);
        tableSources.addAll(sqlTableRightSources);


        return tableSources;
    }

}
