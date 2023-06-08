package com.crazypug.datalineage.parser.sqlexpr;

import com.alibaba.druid.sql.ast.expr.SQLQueryExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.crazypug.datalineage.model.TreeNode;
import com.crazypug.datalineage.parser.SQLExprParser;
import com.crazypug.datalineage.parser.annotation.Parser;

@Parser(SQLQueryExpr.class)
public class SQLQueryExprParser implements SQLExprParser<SQLQueryExpr> {
    @Override
    public void findExpr(TreeNode columnNode, String columnName, SQLSelectItem selectItem, SQLQueryExpr sqlExpr, SQLSelect select) {
        SQLSelect subQuery = sqlExpr.getSubQuery();
        //TODO 该语法暂不支持
    }
}
