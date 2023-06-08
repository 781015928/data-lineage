package com.crazypug.datalineage.parser;

import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.alibaba.druid.sql.ast.statement.SQLSelectQuery;
import com.crazypug.datalineage.model.TreeNode;

import java.util.List;

public interface SQLQueryParser<T> extends SQLParser<SQLSelectQuery> {

     void findColumn(TreeNode columnNode, String columnName,T t, SQLSelect select);


     void findColumnByIndex(TreeNode columnNode, int index,T t, SQLSelect select);


      List<SQLSelectItem> getSelectList(T t);

    int  findColumnIndex(String columnName, T sqlSelectQuery);
}
