package com.crazypug.datalineage.parser.sqlexpr;

import com.alibaba.druid.sql.ast.expr.SQLNullExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.crazypug.datalineage.parser.annotation.Parser;
import com.crazypug.datalineage.model.TreeNode;
import com.crazypug.datalineage.parser.SQLExprParser;
@Parser(SQLNullExpr.class)
public class SQLNullExprParser implements SQLExprParser<SQLNullExpr> {


    @Override
    public void findExpr(TreeNode columnNode, String columnName, SQLSelectItem selectItem, SQLNullExpr sqlExpr, SQLSelect select) {


    }
}