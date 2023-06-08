package com.crazypug.datalineage.parser;

import com.alibaba.druid.sql.ast.statement.*;
import com.crazypug.datalineage.model.TreeNode;

//写注释

public interface SQLTableSourceParser<T extends SQLTableSource> extends SQLParser<T> {
    void findColumn(TreeNode columnNode, String owner, String columnName, SQLSelectItem selectItem, T tableSource, SQLWithSubqueryClause withSubqueryClause, SQLSelect select);


    boolean haveColumn(String columnName,  T tableSource, SQLWithSubqueryClause withSubqueryClause);

    String findOwner( String columnName, SQLSelectItem selectItem, T tableSource, SQLWithSubqueryClause withSubqueryClause);




}


