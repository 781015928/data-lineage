package com.crazypug.datalineage.parser;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.crazypug.datalineage.model.TreeNode;

public interface SQLExprParser<T extends SQLExpr> extends SQLParser<T> {

    void findExpr(TreeNode columnNode, String columnName, SQLSelectItem selectItem, T sqlExpr, SQLSelect select);

}

